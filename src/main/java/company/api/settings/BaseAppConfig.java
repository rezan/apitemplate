package company.api.settings;

import company.api.filters.GeneralFilter;
import company.api.storage.StorageManager;
import company.api.storage.db.DatabaseChooser;
import company.api.storage.db.Database;
import company.api.storage.file.S3;
import company.api.utils.Utilities;

import com.jolbox.bonecp.BoneCPDataSource;
import java.util.HashMap;
import java.util.Map;
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
  public StorageManager dbStorageManager() {
    return new StorageManager();
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
    BoneCPDataSource ds = new BoneCPDataSource();

    ds.setDriverClass(fileSettings.getProperty("db.driver"));
    ds.setJdbcUrl(fileSettings.getProperty("db.url.apidb"));
    ds.setUser(fileSettings.getProperty("db.user"));
    ds.setPassword(fileSettings.getProperty("db.password"));
    ds.setIdleConnectionTestPeriodInSeconds(300);

    return ds;
  }

  @Bean(destroyMethod = "close")
  public DataSource dataSource_apishard0(FileSettings fileSettings) throws Exception {
    BoneCPDataSource ds = new BoneCPDataSource();

    ds.setDriverClass(fileSettings.getProperty("db.driver"));
    ds.setJdbcUrl(fileSettings.getProperty("db.url.apishard0"));
    ds.setUser(fileSettings.getProperty("db.user"));
    ds.setPassword(fileSettings.getProperty("db.password"));
    ds.setIdleConnectionTestPeriodInSeconds(300);

    return ds;
  }

  @Bean(destroyMethod = "close")
  public DataSource dataSource_apishard1(FileSettings fileSettings) throws Exception {
    BoneCPDataSource ds = new BoneCPDataSource();

    ds.setDriverClass(fileSettings.getProperty("db.driver"));
    ds.setJdbcUrl(fileSettings.getProperty("db.url.apishard1"));
    ds.setUser(fileSettings.getProperty("db.user"));
    ds.setPassword(fileSettings.getProperty("db.password"));
    ds.setIdleConnectionTestPeriodInSeconds(300);

    return ds;
  }

  @Bean(initMethod = "init")
  public DatabaseChooser shardedDataSource(DataSource dataSource_apidb, DataSource dataSource_apishard0,
          DataSource dataSource_apishard1, FileSettings fileSettings) {
    DatabaseChooser dbChooser = new DatabaseChooser();

    Map<Object, Object> sources = new HashMap<>();

    sources.put("apidb", dataSource_apidb);
    sources.put("apishard0", dataSource_apishard0);
    sources.put("apishard1", dataSource_apishard1);

    dbChooser.setTargetDataSources(sources);
    dbChooser.setDefaultTargetDataSource(dataSource_apidb);
    dbChooser.setUpdateSchemaDBs(fileSettings.getProperty("db.updateSchema"));

    return dbChooser;
  }

  @Bean(initMethod = "init")
  public S3 s3(FileSettings fileSettings) throws Exception {
    S3 s3 = new S3();

    s3.setSecretKey(fileSettings.getProperty("ec2.secretkey"));
    s3.setAccessKey(fileSettings.getProperty("ec2.accesskey"));
    s3.setS3Url(fileSettings.getProperty("s3.url"));

    return s3;
  }
}
