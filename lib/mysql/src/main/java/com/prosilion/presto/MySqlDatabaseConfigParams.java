package com.prosilion.presto;

public class MySqlDatabaseConfigParams {
  public static final String URL = "url";
  public static final String DRIVER_CLASSNAME = "driver.class.name";
  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String USERS_DDL = "user.schema.ddl.location";
}

/**
 * #spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
 * #spring.datasource.url=jdbc:mysql://localhost:3306/presto
 * #spring.datasource.username=nick
 * #spring.datasource.password=nickpass
 * #spring.jpa.hibernate.ddl-auto=update
 * #spring.jpa.show-sql=true
 * #spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
 */