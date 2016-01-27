package company.api.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class Utilities {

  private static final Logger log = Logger.getLogger(Utilities.class);

  public Utilities() {
    log.info("Utilities initialized");
  }

  public static String getTime(long diffn) {
    return diffn / (1000 * 1000 * 1000) + "s " + diffn / (1000 * 1000) % 1000
        + "ms " + diffn / 1000 % 1000 + "us " + diffn % 1000 + "ns";
  }

  public static boolean empty(String... args) {
    for(String arg : args) {
      if(arg == null || arg.isEmpty() || arg.trim().isEmpty()) {
        return true;
      }
    }

    return false;
  }

  public static String formatJSON(String s) {
    if(empty(s)) {
      return s;
    }

    return s.replace("\"", "\\\"").replace("\n", " ");
  }

  public static String base64Encode(byte[] bytes) {
    return new String(Base64.encodeBase64(bytes));
  }

  public static byte[] base64Decode(String s) throws IOException {
    return Base64.decodeBase64(s.getBytes());
  }

  public static String hash(String s) throws Exception {
    String ret = null;

    if(s == null || s.isEmpty()) {
      return ret;
    }

    MessageDigest hash = MessageDigest.getInstance("SHA-1");
    hash.reset();
    hash.update(s.getBytes("utf8"));

    ret = new BigInteger(1, hash.digest()).toString(16);

    return ret;
  }

  public static String hashMD5(String s) throws Exception {
    String ret = null;

    if(s == null || s.isEmpty()) {
      return ret;
    }

    MessageDigest hash = MessageDigest.getInstance("MD5");
    hash.reset();
    hash.update(s.getBytes("utf8"));

    ret = new BigInteger(1, hash.digest()).toString(16);

    return ret;
  }

  public static String hashMD5_64(String s) throws Exception {
    String ret = null;

    if(s == null || s.isEmpty()) {
      return ret;
    }

    MessageDigest hash = MessageDigest.getInstance("MD5");
    hash.reset();
    hash.update(s.getBytes("utf8"));

    ret = base64Encode(hash.digest());

    return ret;
  }
}
