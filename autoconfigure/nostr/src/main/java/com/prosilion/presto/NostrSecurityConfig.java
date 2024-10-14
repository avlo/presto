package com.prosilion.presto;

import com.prosilion.presto.nostr.controller.NostrAuthController;
import com.prosilion.presto.nostr.service.NostrAppUserDtoService;
import com.prosilion.presto.nostr.service.NostrAppUserDtoServiceImpl;
import com.prosilion.presto.nostr.service.NostrUserDetailsService;
import com.prosilion.presto.nostr.service.NostrUserDetailsServiceImpl;
import com.prosilion.presto.nostr.service.NostrUserService;
import com.prosilion.presto.nostr.service.NostrUserServiceImpl;
import com.prosilion.presto.security.repository.AppUserAuthUserRepository;
import com.prosilion.presto.security.service.AppUserService;
import com.prosilion.presto.security.service.CustomizableAppUserService;
import com.prosilion.presto.web.controller.AuthController;
import com.prosilion.presto.web.model.NostrAppUserDtoIF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

import javax.sql.DataSource;

@Slf4j
@AutoConfiguration
@EnableWebSecurity
//@AutoConfigureBefore({SecurityCoreConfig.class})
public class NostrSecurityConfig {

  @Bean(name = "authController")
  @Primary
  AuthController authController(NostrUserService nostrUserService) {
    return new NostrAuthController(nostrUserService);
  }

  @Bean(name = "userDetailsService")
  @Primary
  public NostrUserDetailsService userDetailsService(DataSource dataSource, PasswordEncoder passwordEncoder) {
    NostrUserDetailsServiceImpl nostrAuthUserDetailsService = new NostrUserDetailsServiceImpl(dataSource, passwordEncoder);
    return nostrAuthUserDetailsService;
  }

  @Bean
  @Primary
  public NostrUserService authUserService(
      CustomizableAppUserService customizableAppUserService,
      NostrUserDetailsService nostrUserDetailsService,
      AppUserService appUserService,
      AppUserAuthUserRepository appUserAuthUserRepository) {
    return new NostrUserServiceImpl(customizableAppUserService, nostrUserDetailsService, appUserService, appUserAuthUserRepository);
  }

  @Bean
  @Primary
  public NostrAppUserDtoService<NostrAppUserDtoIF> appUserDtoService(NostrUserService authUserService) {
    return new NostrAppUserDtoServiceImpl<>(authUserService);
  }
}
