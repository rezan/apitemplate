package company.api.settings;

import company.api.filters.GeneralFilter;
import company.api.storage.DBStorageManager;
import company.api.storage.dbclients.Database;
import company.api.utils.Utilities;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import javax.sql.DataSource;
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

  @Bean(destroyMethod = "close")
  public DataSource dataSource_apidb(FileSettings fileSettings) throws Exception {
    ComboPooledDataSource ds = new ComboPooledDataSource();

    ds.setDriverClass(fileSettings.getProperty("db.driver"));
    ds.setJdbcUrl(fileSettings.getProperty("db.url.apidb"));
    ds.setUser(fileSettings.getProperty("db.user"));
    ds.setPassword(fileSettings.getProperty("db.password"));
    ds.setIdleConnectionTestPeriod(300);

    return ds;
  }

  @Bean(destroyMethod = "close")
  public DataSource dataSource_apishard0(FileSettings fileSettings) throws Exception {
    ComboPooledDataSource ds = new ComboPooledDataSource();

    ds.setDriverClass(fileSettings.getProperty("db.driver"));
    ds.setJdbcUrl(fileSettings.getProperty("db.url.apishard0"));
    ds.setUser(fileSettings.getProperty("db.user"));
    ds.setPassword(fileSettings.getProperty("db.password"));
    ds.setIdleConnectionTestPeriod(300);

    return ds;
  }

  @Bean(destroyMethod = "close")
  public DataSource dataSource_apishard1(FileSettings fileSettings) throws Exception {
    ComboPooledDataSource ds = new ComboPooledDataSource();

    ds.setDriverClass(fileSettings.getProperty("db.driver"));
    ds.setJdbcUrl(fileSettings.getProperty("db.url.apishard1"));
    ds.setUser(fileSettings.getProperty("db.user"));
    ds.setPassword(fileSettings.getProperty("db.password"));
    ds.setIdleConnectionTestPeriod(300);

    return ds;
  }
}
