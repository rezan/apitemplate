package company.api.storage;

import company.api.data.apishard.KeyValue;
import company.api.data.apidb.EmailSignup;
import company.api.storage.db.Database;
import company.api.storage.db.DatabaseChooser;
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

  public StorageManager() {
    log.info("StorageManager initialized.");
  }

  public KeyValue getKey(String key) {
    long start = System.nanoTime();

    KeyValue.setShard(key);

    KeyValue application = database.getKey(key);

    long diff = System.nanoTime() - start;
    log.info("StorageManager getKey(" + key + ") got: " + application);
    log.info("StorageManager getKey(" + key + ") took " + Utilities.getTime(diff));

    return application;
  }

  public void storeEmailSignup(EmailSignup emailSignup) {
    long start = System.nanoTime();

    DatabaseChooser.setDB("api");

    database.storeEmailSignup(emailSignup);

    long diff = System.nanoTime() - start;
    log.info("StorageManager storeEmailSignup(" + emailSignup + ") took " + Utilities.getTime(diff));
  }
}
