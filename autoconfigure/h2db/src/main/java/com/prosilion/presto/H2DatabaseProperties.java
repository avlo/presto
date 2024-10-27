package com.prosilion.presto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

/**
 * bean-mapper for external-to-module application.properties file
 *
 * note: this class does not know if application.properties file exists.  that responsibility resides in
 *  @EnableConfigurationProperties(H2DatabaseProperties.class) in H2DatabaseAutoConfiguration.java
 */
@ConfigurationProperties(prefix = "spring.datasource")
@Profile("local")
@Getter
@Setter
@NoArgsConstructor
public class H2DatabaseProperties {
  private String url;
  private String driverClassName;
  private String username;
  private String password;
  private String userSchemaDdlLocation;
}
