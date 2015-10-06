package company.api.settings;

import company.api.utils.Utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

/**
 *
 * @author Reza Naghibi
 */
public class FileSettings {

  private static final Logger log = Logger.getLogger(FileSettings.class);

  private Properties prop;

  private Properties override;

  private String templatePath = "/templates/";

  public FileSettings() {
    prop = new Properties();
    override = new Properties();
  }

  public synchronized void init() throws Exception {
    InputStream is = FileSettings.class.getResourceAsStream(Constants.CONFIG_LOCATION);
    prop.load(is);
    is.close();

    try {
      String overridePath = getProperty("config.override");

      if(overridePath != null && !overridePath.isEmpty()) {
        is = new FileInputStream(overridePath);
        override.load(is);
        is.close();
      }
    } catch (Exception ex) {
      log.error("ERROR Configuration override init(): " + ex.toString(), ex);
      override = new Properties();
    }

    log.info("Configuration initialized: " + prop.keySet().size() + " entires");
    log.info("Configuration override initialized: " + override.keySet().size() + " entires");
    log.info("Configuration project name: " + getProperty("project.name"));
    log.info("Configuration project version: " + getProperty("project.version"));
    log.info("Configuration project environment: " + getProperty("env"));
    log.info("Configuration project build date: " + getProperty("project.build.date"));
  }

  public String getProperty(String key) {
    return getProperty(key, null);
  }

  public String getProperty(String key, String defaultVal) {
    String ret = override.getProperty(key);

    if(ret == null) {
      ret = prop.getProperty(key, defaultVal);
    }

    if(ret != null && ret.startsWith("@")) {
      ret = loadTemplate(ret.substring(1), key);
    }

    return ret;
  }

  private synchronized String loadTemplate(String file, String key) {
    if(Utilities.empty(file)) {
      override.setProperty(key, file);
      return null;
    }

    try {
      String path = templatePath + file;

      log.info("Configuration loadTemplate: " + path);

      InputStream is = this.getClass().getResourceAsStream(templatePath + file);
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      StringBuilder sb = new StringBuilder();
      String line;

      while((line = br.readLine()) != null) {
        sb.append(line);
      }

      is.close();

      override.setProperty(key, sb.toString());

      return sb.toString();
    } catch (Exception ex) {
      log.error("Configuration loadTemplate ERROR: " + ex.toString(), ex);
      override.setProperty(key, file);
      return null;
    }
  }

}
