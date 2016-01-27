package company.api.storage.file;

import company.api.utils.Utilities;
import company.api.exceptions.MsgCodeException;
import company.api.exceptions.WrappedException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;

/**
 *
 * @author Reza Naghibi
 */
public class S3 {

  private static final Logger log = Logger.getLogger(S3.class);

  private Mac mac;

  private String accessKey;

  private SimpleDateFormat dateFormat;

  private String s3Url;

  public S3() {
  }

  public synchronized void init() {
    if(mac == null) {
      throw new RuntimeException("S3 is not initialized");
    }

    String fmt = "EEE, dd MMM yyyy HH:mm:ss ";
    dateFormat = new SimpleDateFormat(fmt, Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

    log.info("S3 initialized, s3Url: " + getS3Url());
  }

  public void storeString(String bucket, String file, String content, String contentType) throws Exception {
    int tries = 0;

    Exception tex = new MsgCodeException("Too many s3 attempts, aborting", 400, "m2010");

    while(tries <= 4) {

      tries++;

      if(tries == 4) {
        throw tex;
      }

      try {
        store(bucket, file, content, null, 0, contentType, null, false);
      } catch (Exception ex) {
        log.error("S3 storeString error, going to retry: " + ex.toString());
        tex = ex;
        Thread.sleep(1000);
        continue;
      }

      break;
    }
  }

  public String storeMedia(String bucket, String file, InputStream in, int len, String contentType, String userId) throws Exception {
    return store(bucket, file, "", in, len, contentType, userId, true);
  }

  private String store(String bucket, String file, String content, InputStream in, int len, String contentType, String userId, boolean pub) throws Exception {
    long start = System.nanoTime();

    s3Request("PUT", bucket, file, content, in, len, contentType, userId, pub);

    String ret = getS3Url(bucket, file);

    long diff = System.nanoTime() - start;
    log.debug("S3 store() for asset '" + file + "' in bucket '" + bucket + "' took " + Utilities.getTime(diff));

    return ret;
  }

  public String getString(String bucket, String file) throws Exception {
    return get(bucket, file);
  }

  private String get(String bucket, String file) throws Exception {
    long start = System.nanoTime();

    HttpURLConnection http = s3Request("GET", bucket, file, "", null, 0, "", "", false);

    String ret = getResponse(http.getInputStream());

    long diff = System.nanoTime() - start;
    log.debug("S3 get() for asset '" + file + "' in bucket '" + bucket + "' took " + Utilities.getTime(diff));

    return ret;
  }

  public void deleteFile(String bucket, String file) throws Exception {
    delete(bucket, file);
  }

  private void delete(String bucket, String file) throws Exception {
    long start = System.nanoTime();

    s3Request("DELETE", bucket, file, "", null, 0, "", "", false);

    long diff = System.nanoTime() - start;
    log.debug("S3 delete() for asset '" + file + "' in bucket '" + bucket + "' took " + Utilities.getTime(diff));
  }

  private HttpURLConnection s3Request(String method, String bucket, String file, String content, InputStream in, int len,
          String contentType, String userId, boolean pub) throws Exception {

    String contentMD5 = "";

    if(!Utilities.empty(content)) {
      contentMD5 = Utilities.hashMD5_64(content);

      if(Utilities.empty(contentType)) {
        contentType = "text/plain; charset=UTF-8";
      }

      if(!contentType.contains("charset")) {
        contentType += "; charset=UTF-8";
      }
    } else {
      content = "";
    }

    if(Utilities.empty(contentType)) {
      contentType = "";
    }

    String date = dateFormat.format(new Date()) + "+0000";

    StringBuilder buf = new StringBuilder();
    buf.append(method).append("\n");
    buf.append(contentMD5).append("\n");
    buf.append(contentType).append("\n");
    buf.append(date).append("\n");
    if(pub) {
      buf.append("x-amz-acl:public-read\n");
    }
    if(!Utilities.empty(userId)) {
      buf.append("x-amz-meta-user:").append(userId).append("\n");
    }
    buf.append("/").append(bucket).append("/").append(file);

    String signature = sign(buf.toString());
    String awsAuth = "AWS " + getAccessKey() + ":" + signature;

    try {
      String lS3Url = getS3Url(bucket, file);

      log.debug("S3 s3Request for asset url: " + method + " '" + lS3Url + "'");

      URL url = new URL(lS3Url);

      HttpURLConnection http = (HttpURLConnection)url.openConnection();
      http.setConnectTimeout(10000);
      http.setReadTimeout(10000);
      http.setDoInput(true);
      http.setRequestMethod(method);
      http.setRequestProperty("Authorization", awsAuth);
      http.setRequestProperty("Date", date);
      http.setRequestProperty("Host", bucket + ".s3.amazonaws.com");

      if(in == null) {
        http.setRequestProperty("Content-Length", content.length() + "");
      } else {
        http.setRequestProperty("Content-Length", len + "");
      }

      if(!Utilities.empty(contentType)) {
        http.setRequestProperty("Content-Type", contentType);
      }

      if(pub) {
        http.setRequestProperty("x-amz-acl", "public-read");
      }

      if(!Utilities.empty(userId)) {
        http.setRequestProperty("x-amz-meta-user", userId);
      }

      if(!Utilities.empty(content)) {
        http.setRequestProperty("Content-MD5", contentMD5);
        http.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream());
        out.write(content);
        out.flush();
        out.close();
        log.debug("S3 s3Request wrote " + content.length() + " bytes");
      } else if(in != null) {
        http.setDoOutput(true);
        OutputStream out = http.getOutputStream();
        byte bbuf[] = new byte[10000];
        int rlen;
        int total = 0;
        while((rlen = in.read(bbuf)) > 0) {
          total += rlen;
          out.write(bbuf, 0, rlen);
        }
        out.flush();
        out.close();
        in.close();
        log.debug("S3 s3Request wrote " + total + " bytes");
      }

      http.connect();

      if(http.getResponseCode() < 200 || http.getResponseCode() >= 300) {
        log.error("S3 s3Request ERROR invalid response: " + http.getResponseMessage() + " (" + http.getResponseCode() + ")");
        log.error("S3 s3Request ERROR string to sign:\n" + buf);
        log.error("S3 s3Request ERROR auth string: " + awsAuth);
        try {
          String errorContent = getResponse(http.getErrorStream());
          log.error("S3 s3Request ERROR response:\n" + errorContent);
        } catch (Exception ex) {
          log.error("S3 s3Request ERROR no response content");
        }
        throw new MsgCodeException("S3 s3Request ERROR invalid response: " + http.getResponseMessage() + " (" + http.getResponseCode() + ")", 400, "m2010");
      }

      return http;
    } catch (Exception ex) {
      log.error("S3 s3Request ERROR exception: " + ex.toString(), ex);
      throw new WrappedException(ex, 400, "m2010");
    }
  }

  private String getResponse(InputStream in) throws Exception {
    BufferedReader reader = null;

    try {
      reader = new BufferedReader(new InputStreamReader(in));
      StringBuilder buffer = new StringBuilder();
      int read;
      char[] chars = new char[1024];

      while((read = reader.read(chars)) != -1) {
        buffer.append(chars, 0, read);
      }

      in.close();

      log.debug("S3 getResponse read " + buffer.length() + " bytes");

      return buffer.toString();

    } catch (Exception ex) {
      log.error("S3 getResponse ERROR exception: " + ex.toString(), ex);
      throw new WrappedException(ex, 400, "m2010");
    } finally {
      if(reader != null) {
        try {
          reader.close();
        } catch (Exception ex1) {
        }
      }
    }
  }

  private String getS3Url(String bucket, String file) {
    String url = getS3Url();

    return url.replace("%BUCKET%", bucket).replace("%FILE%", file);
  }

  public void setSecretKey(String secreyKey) throws Exception {
    mac = Mac.getInstance("HmacSHA1");
    SecretKeySpec signingKey = new SecretKeySpec(secreyKey.getBytes("UTF-8"), "HmacSHA1");
    mac.init(signingKey);
  }

  private String sign(String data) throws Exception {
    Mac localMac = (Mac)mac.clone();
    byte[] signBytes = localMac.doFinal(data.getBytes("UTF8"));
    String signature = Utilities.base64Encode(signBytes);
    return signature;
  }

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getS3Url() {
    return s3Url;
  }

  public void setS3Url(String s3Url) {
    this.s3Url = s3Url;
  }
}
