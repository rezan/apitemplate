package company.api.storage;

import company.data.apishard.KeyValue;
import company.data.apidb.EmailSignup;
import company.api.storage.dbclients.Database;
import company.api.storage.dbrouting.ThreadDBChooser;
import company.api.utils.Utilities;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Reza Naghibi
 */
public class DBStorageManager {

  private static final Logger log = Logger.getLogger(DBStorageManager.class);

  @Autowired
  private Database database;

  public DBStorageManager() {
    log.info("DBStorageManager initialized.");
  }

  public KeyValue getKey(String key) {
    long start = System.nanoTime();

    KeyValue.setShard(key);

    KeyValue application = database.getKey(key);

    long diff = System.nanoTime() - start;
    log.info("DBStorageManager getKey(" + key + ") got: " + application);
    log.info("DBStorageManager getKey(" + key + ") took " + Utilities.getTime(diff));

    return application;
  }

  public void storeEmailSignup(EmailSignup emailSignup) {
    long start = System.nanoTime();

    ThreadDBChooser.setDB("api");

    database.storeEmailSignup(emailSignup);

    long diff = System.nanoTime() - start;
    log.info("DBStorageManager storeEmailSignup(" + emailSignup + ") took " + Utilities.getTime(diff));
  }
}
