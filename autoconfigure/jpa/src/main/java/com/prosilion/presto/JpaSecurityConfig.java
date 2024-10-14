package com.prosilion.presto;

import com.prosilion.presto.jpa.controller.JpaAuthController;
import com.prosilion.presto.security.service.AuthUserService;
import com.prosilion.presto.web.controller.AuthController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@Slf4j
@AutoConfiguration
public class JpaSecurityConfig {

//  TODO: inclusion of below causes nostr to fail
  @Bean
  AuthController authController(AuthUserService authUserService) {
    return new JpaAuthController(authUserService);
  }
}
