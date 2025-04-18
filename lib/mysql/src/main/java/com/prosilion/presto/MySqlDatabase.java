package com.prosilion.presto;

import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

/**
 * A bean of this class instantiates in either one of two ways:
 * 1) If container doesn't already have one, MySqlDatabaseAutoConfiguration.java
 * will create one based on selected spring-boot profile
 * 2) Pre-existing by some other mechanism (currently does not occur)
 */
public class MySqlDatabase {
  private final MySqlDatabaseConfigProperties mySqlDatabaseConfigProperties;

  /**
   * currently, GreetingConfig parameter/object/bean DI/wired exclusively by MySqlDatabaseAutoConfiguration.java
   */
  public MySqlDatabase(MySqlDatabaseConfigProperties mySqlDatabase) {
    this.mySqlDatabaseConfigProperties = mySqlDatabase;
  }

  public DataSource getDataSource() {
    return DataSourceBuilder
        .create()
        .url(mySqlDatabaseConfigProperties.getProperty(MySqlDatabaseConfigParams.URL))
        .driverClassName(mySqlDatabaseConfigProperties.getProperty(MySqlDatabaseConfigParams.DRIVER_CLASSNAME))
        .username(mySqlDatabaseConfigProperties.getProperty(MySqlDatabaseConfigParams.USERNAME))
        .password(mySqlDatabaseConfigProperties.getProperty(MySqlDatabaseConfigParams.PASSWORD)).build();
  }
}
