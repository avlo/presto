package com.prosilion.presto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import javax.sql.DataSource;

@AutoConfiguration
@ConditionalOnClass(MySqlDatabase.class)

/**
 *  MySqlDatabaseProperties bean created (and autowired into this class) with its fields in either one of two states:
 *    1) fields properly populated iff:
 *      a) an application.properties file exists
 *      b) requisite fields are defined within the file
 *  otherwise:
 *    2) field values set to null
 */
@EnableConfigurationProperties(MySqlDatabaseProperties.class)
public class MySqlDatabaseAutoConfiguration {
  //  TODO: H2 needs replace with MySql
  private static final String DEFAULT_DB = "jdbc:mysql://localhost:3306/presto_jpa_dev";
  private static final String DEFAULT_DRIVER_CLASSNAME = "com.mysql.cj.jdbc.Driver";
  private static final String DEFAULT_USERNAME = "serveruser";
  private static final String DEFAULT_PASSWORD = "serveruserpass";
  private static final String DEFAULT_DDL = JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION;

  private final MySqlDatabaseProperties dbProps;

  @Autowired
  public MySqlDatabaseAutoConfiguration(MySqlDatabaseProperties mySqlDatabaseProperties) {
    this.dbProps = mySqlDatabaseProperties;
  }

  /**
   * this instances field values are populated in either one of two ways:
   * 1) via MySqlDatabaseProperties bean non-null fields (handled by @EnableConfigurationProperties(MySqlDatabaseProperties.class), above)
   * otherwise, if any MySqlDatabaseProperties bean fields are null:
   * 2) set explicit/default string values as seen below
   *
   * careful/note: any MySqlDatabaseProperties bean with any/all fields pre-populated can only be done via EXTERNAL
   * application.properties file.  i.e., the "h2db-spring-boot-sample-app" application.properties file is EXTERNAL
   */
  @Bean
  @ConditionalOnMissingBean
  public MySqlDatabaseConfig mySqlDatabaseConfig() {
    MySqlDatabaseConfig mySqlDatabaseConfig = new MySqlDatabaseConfig();
    mySqlDatabaseConfig.put(MySqlDatabaseConfigParams.URL, go(dbProps.getUrl(), DEFAULT_DB));
    mySqlDatabaseConfig.put(MySqlDatabaseConfigParams.DRIVER_CLASSNAME, go(dbProps.getDriverClassName(), DEFAULT_DRIVER_CLASSNAME));
    mySqlDatabaseConfig.put(MySqlDatabaseConfigParams.USERNAME, go(dbProps.getUsername(), DEFAULT_USERNAME));
    mySqlDatabaseConfig.put(MySqlDatabaseConfigParams.PASSWORD, go(dbProps.getPassword(), DEFAULT_PASSWORD));
    mySqlDatabaseConfig.put(MySqlDatabaseConfigParams.USERS_DDL, go(dbProps.getUserSchemaDdlLocation(), DEFAULT_DDL));
    return mySqlDatabaseConfig;
  }

  private String go(String a, String b) {
    return StringUtils.isBlank(a) ? b : a;
  }

  @Bean
  @ConditionalOnMissingBean
  public MySqlDatabase mySqlDatabase(MySqlDatabaseConfig mySqlDatabaseConfig) {
    return new MySqlDatabase(mySqlDatabaseConfig);
  }

  @Bean
  @Primary
  public DataSource getDataSource(MySqlDatabase mySqlDatabase) {
    return mySqlDatabase.getDataSource();
  }
}
