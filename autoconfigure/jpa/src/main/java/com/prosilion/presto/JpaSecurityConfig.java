package com.prosilion.presto;

import com.prosilion.presto.jpa.controller.JpaAuthController;
import com.prosilion.presto.security.service.AuthUserService;
import com.prosilion.presto.web.controller.AuthController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Slf4j
@AutoConfiguration
@EnableWebSecurity
public class JpaSecurityConfig {

  @Bean(name = "authController")
  @ConditionalOnMissingBean(name = "authController")
  AuthController authController(AuthUserService authUserService) {
    return new JpaAuthController(authUserService);
  }
}
