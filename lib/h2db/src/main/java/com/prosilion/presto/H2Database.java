package com.prosilion.presto;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import javax.sql.DataSource;

/**
 * A bean of this class instantiates in either one of two ways:
 * 1) If container doesn't already have one, H2DatabaseAutoConfiguration.java
 * will create one.
 * 2) Pre-existing by some other mechanism (currently does not occur)
 */
public class H2Database {
  private final H2DatabaseConfig h2DatabaseConfig;

  /**
   * currently, GreetingConfig parameter/object/bean DI/wired exclusively by H2DatabaseAutoConfiguration.java
   */
  public H2Database(H2DatabaseConfig h2DatabaseConfig) {
    this.h2DatabaseConfig = h2DatabaseConfig;
  }

  public DataSource getDataSource() {
    EmbeddedDatabase build = new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.H2)
        .addScript(h2DatabaseConfig.getProperty(H2DatabaseConfigParams.USERS_DDL))
        .build();
    return build;
  }

//  public DataSource getDataSource() {
//    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//    dataSourceBuilder.url(h2DatabaseConfig.getProperty(H2DatabaseConfigParams.URL));
//    dataSourceBuilder.driverClassName(h2DatabaseConfig.getProperty(H2DatabaseConfigParams.DRIVER_CLASSNAME));
//    dataSourceBuilder.username(h2DatabaseConfig.getProperty(H2DatabaseConfigParams.USERNAME));
//    dataSourceBuilder.password(h2DatabaseConfig.getProperty(H2DatabaseConfigParams.PASSWORD));
//    return dataSourceBuilder.build();
//  }
}
