package company.api.storage.db;

import company.api.data.apishard.KeyValue;
import company.api.data.apidb.EmailSignup;
import company.api.utils.Utilities;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Reza Naghibi
 */
@Transactional
public class Database {

  private static final Logger log = Logger.getLogger(Database.class);

  @PersistenceContext
  private EntityManager manager;

  public Database() {
  }

  public void init() {
    log.info("Database initialized");
  }

  @Transactional(readOnly = true)
  public KeyValue getKey(String key) {
    try {
      long start = System.nanoTime();

      Criteria criteria = getSession().createCriteria(KeyValue.class);
      criteria.add(Restrictions.eq("keyStr", key));

      KeyValue keyValue = (KeyValue)criteria.uniqueResult();

      long diff = System.nanoTime() - start;
      log.info("Database getKey(" + key + ") took " + Utilities.getTime(diff));

      return keyValue;
    } catch (Exception ex) {
      log.error("ERROR Database getKey(" + key + "): " + ex.toString(), ex);
      throw ex;
    }
  }

  public void storeEmailSignup(EmailSignup emailSignup) {
    try {
      long start = System.nanoTime();

      getSession().persist(emailSignup);

      long diff = System.nanoTime() - start;
      log.debug("Database storeEmailSignup(" + emailSignup + ") took " + Utilities.getTime(diff));
    } catch (Exception ex) {
      log.error("ERROR Database storeEmailSignup(" + emailSignup + "): " + ex.toString(), ex);
      throw ex;
    }
  }

  public void destroy() {
    try {
      com.mysql.jdbc.AbandonedConnectionCleanupThread.shutdown();
    } catch (Exception ex) {
      log.error("ERROR Database destroy: " + ex.toString(), ex);
    }
    log.info("Database destroyed");
  }

  private Session getSession() {
    return ((Session)(getManager().getDelegate()));
  }

  private EntityManager getManager() {
    return manager;
  }

}
