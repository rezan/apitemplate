package company.api.storage.db;

import company.api.settings.Constants;
import company.api.settings.FileSettings;
import company.api.utils.Utilities;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 *
 * @author Reza Naghibi
 */
public class DatabaseChooser extends AbstractRoutingDataSource {

  private static final Logger log = Logger.getLogger(DatabaseChooser.class);

  private static final ThreadLocal<String> threadContext = new ThreadLocal<>();

  private static String updateSchemaDBs;

  @Autowired
  private FileSettings fileSettings;

  public void init() throws Exception {
    for(String db : getUpdateSchemaDBs()) {
      log.info("ThreadDBChooser Initializing DB: " + db);

      setDB(db);

      Configuration config = new Configuration();
      config.setProperty(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "create");
      config.setProperty(org.hibernate.cfg.Environment.DIALECT, fileSettings.getProperty("db.dialect"));
      config.setProperty(org.hibernate.cfg.Environment.URL, fileSettings.getProperty("db.url." + db));
      config.setProperty(org.hibernate.cfg.Environment.USER, fileSettings.getProperty("db.user"));
      config.setProperty(org.hibernate.cfg.Environment.PASS, fileSettings.getProperty("db.password"));
      config.setProperty(org.hibernate.cfg.Environment.DRIVER, fileSettings.getProperty("db.driver"));

      String pkg = Constants.PACKAGE_PREFIX + ".data." + trimShard(db);

      log.info("ThreadDBChooser scanning: " + pkg);

      for(Class cls : getClasses(pkg)) {
        log.info("ThreadDBChooser found class: " + cls.getName());
        config.addAnnotatedClass(cls);
      }

      SchemaUpdate update = new SchemaUpdate(config);
      update.execute(true, true);
    }

    defaultDB();

    log.info("ThreadDBChooser finished");
  }

  private Class[] getClasses(String packageName) throws Exception {
    String path = packageName.replace('.', '/');
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    Enumeration<URL> resources = classLoader.getResources(path);

    List<File> dirs = new ArrayList<>();

    while(resources.hasMoreElements()) {
      URL resource = resources.nextElement();
      dirs.add(new File(resource.getFile()));
    }

    ArrayList<Class> classes = new ArrayList<>();

    for(File directory : dirs) {
      classes.addAll(findClasses(directory, packageName));
    }

    return classes.toArray(new Class[classes.size()]);
  }

  private List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
    List<Class> classes = new ArrayList<>();

    if(!directory.exists()) {
      return classes;
    }

    File[] files = directory.listFiles();

    for(File file : files) {
      if(file.isDirectory()) {
        classes.addAll(findClasses(file, packageName + "." + file.getName()));
      } else if(file.getName().endsWith(".class")) {
        classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
      }
    }
    
    return classes;
  }

  private String trimShard(String db) {
    while(!db.isEmpty() && Character.isDigit(db.charAt(db.length() - 1))) {
      db = db.substring(0, db.length() - 1);
    }

    return db;
  }

  @Override
  protected Object determineCurrentLookupKey() {
    Object db = getDB();

    if(db == null) {
      log.info("ThreadDBChooser using default db");
    } else {
      log.info("ThreadDBChooser getting db: '" + db + "'");
    }

    return db;
  }

  public static void defaultDB() {
    threadContext.remove();
  }

  public static void setDB(String db) {
    threadContext.set(db);
  }

  public static String getDB() {
    return (String)(threadContext.get());
  }

  public List<String> getUpdateSchemaDBs() {
    if(Utilities.empty(updateSchemaDBs)) {
      return new ArrayList<>();
    }

    return Arrays.asList(updateSchemaDBs.split(" "));
  }

  public void setUpdateSchemaDBs(String aAllDBs) {
    updateSchemaDBs = aAllDBs;
  }
}
