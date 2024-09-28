package com.prosilion.presto.config;

import com.prosilion.presto.model.entity.ExampleNostrUser;
import com.prosilion.presto.repository.ExampleNostrUserRepository;
import com.prosilion.presto.security.service.CustomizableAppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@Configuration
@EnableJpaRepositories(basePackageClasses = {ExampleNostrUserRepository.class})
@EntityScan(basePackageClasses = {ExampleNostrUser.class})
public class ExampleNostrUserConfig {

  @Bean
  public CustomizableAppUserService customizableAppUserService() {
    log.info("EXAMPLE USER CONFIG - Creating ExampleNostrUser");
    return new CustomizableAppUserService(new ExampleNostrUser());
  }
}
