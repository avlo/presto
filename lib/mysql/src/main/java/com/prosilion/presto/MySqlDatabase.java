package com.prosilion.presto;

import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

/**
 * A bean of this class instantiates in either one of two ways:
 * 1) If container doesn't already have one, MySqlDatabaseAutoConfiguration.java
 * will create one.
 * 2) Pre-existing by some other mechanism (currently does not occur)
 */
public class MySqlDatabase {
  private final MySqlDatabaseConfig mySqlDatabaseConfig;

  /**
   * currently, GreetingConfig parameter/object/bean DI/wired exclusively by MySqlDatabaseAutoConfiguration.java
   */
  public MySqlDatabase(MySqlDatabaseConfig mySqlDatabase) {
    this.mySqlDatabaseConfig = mySqlDatabase;
  }

  public DataSource getDataSource() {
    return DataSourceBuilder
        .create()
        .url(mySqlDatabaseConfig.getProperty(MySqlDatabaseConfigParams.URL))
        .driverClassName(mySqlDatabaseConfig.getProperty(MySqlDatabaseConfigParams.DRIVER_CLASSNAME))
        .username(mySqlDatabaseConfig.getProperty(MySqlDatabaseConfigParams.USERNAME))
        .password(mySqlDatabaseConfig.getProperty(MySqlDatabaseConfigParams.PASSWORD)).build();
  }
}
