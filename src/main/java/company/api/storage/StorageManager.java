package company.api.storage;

import company.api.data.apishard.KeyValue;
import company.api.data.apidb.EmailSignup;
import company.api.settings.FileSettings;
import company.api.storage.db.Database;
import company.api.storage.db.DatabaseChooser;
import company.api.storage.file.S3;
import company.api.utils.Utilities;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Reza Naghibi
 */
public class StorageManager {

  private static final Logger log = Logger.getLogger(StorageManager.class);

  @Autowired
  private Database database;

  @Autowired
  private S3 s3;

  @Autowired
  private FileSettings fileSettings;

  public StorageManager() {
    log.info("StorageManager initialized.");
  }

  public KeyValue getKeyValue(String key) {
    long start = System.nanoTime();

    KeyValue.setShard(key);

    KeyValue keyValue = database.getKeyValue(key);

    long diff = System.nanoTime() - start;
    log.info("StorageManager getKey(" + key + ") got: " + keyValue);
    log.info("StorageManager getKey(" + key + ") took " + Utilities.getTime(diff));

    return keyValue;
  }

  public void storeKeyValue(KeyValue keyValue) {
    long start = System.nanoTime();

    KeyValue.setShard(keyValue.getKeyStr());

    database.storeKeyValue(keyValue);

    long diff = System.nanoTime() - start;
    log.info("StorageManager storeKeyValue(" + keyValue + ") took " + Utilities.getTime(diff));
  }

  public String getS3(String file) throws Exception {
    long start = System.nanoTime();

    String bucket = fileSettings.getProperty("s3.mediaBucket");

    String content = s3.getString(bucket, file);

    long diff = System.nanoTime() - start;
    log.info("StorageManager getS3(" + file + ") took " + Utilities.getTime(diff));

    return content;
  }

  public void storeS3(String file, String content) throws Exception {
    long start = System.nanoTime();

    String bucket = fileSettings.getProperty("s3.bucket");

    s3.storeString(bucket, file, content, "text/plain");

    long diff = System.nanoTime() - start;
    log.info("StorageManager storeS3(" + file + ") took " + Utilities.getTime(diff));
  }

  public void storeEmailSignup(EmailSignup emailSignup) {
    long start = System.nanoTime();

    DatabaseChooser.setDB("api");

    database.storeEmailSignup(emailSignup);

    long diff = System.nanoTime() - start;
    log.info("StorageManager storeEmailSignup(" + emailSignup + ") took " + Utilities.getTime(diff));
  }
}
