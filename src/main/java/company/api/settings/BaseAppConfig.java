package company.api.settings;

import company.api.filters.GeneralFilter;
import company.api.storage.DBStorageManager;
import company.api.storage.dbclients.Database;
import company.api.utils.Utilities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Reza Naghibi
 */
@Configuration
public class BaseAppConfig {

  @Bean
  public Utilities utilities() {
    return new Utilities();
  }
  
  @Bean
  public GeneralFilter generalFilter() {
    return new GeneralFilter();
  }
  
  @Bean
  public DBStorageManager dbStorageManager() {
    return new DBStorageManager();
  }

  @Bean(initMethod = "init", destroyMethod = "destroy")
  public Database database() {
    return new Database();
  }

  @Bean(initMethod = "init")
  public FileSettings fileSettings() {
    return new FileSettings();
  }
}
