package com.prosilion.presto;

import com.prosilion.presto.security.AppUserLocalAuthorities;
import com.prosilion.presto.security.repository.AppUserAuthUserRepository;
import com.prosilion.presto.security.service.AppUserService;
import com.prosilion.presto.security.service.AuthUserDetailServiceImpl;
import com.prosilion.presto.security.service.AuthUserDetailsService;
import com.prosilion.presto.security.service.AuthUserService;
import com.prosilion.presto.security.service.AuthUserServiceImpl;
import com.prosilion.presto.security.service.CustomizableAppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@AutoConfiguration
@EnableWebSecurity
@ConditionalOnBean(CustomizableAppUserService.class)
@EnableJpaRepositories(basePackages = "com.prosilion.presto.security.repository")
// TODO: below should not be required
@EntityScan(basePackages = "com.prosilion.presto.security.entity")
public class SecurityCoreConfig {
  private static final Logger LOGGER = LoggerFactory.getLogger(SecurityCoreConfig.class);

  @Bean
//	@ConditionalOnMissingBean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    LOGGER.info("DB - H2 Console active at /h2-console/");
    return web -> web.ignoring().requestMatchers(PathRequest.toH2Console());
  }

//  @Bean
//  @ConditionalOnMissingBean
//  public AuthUserDetailsService userDetailsService(DataSource dataSource, PasswordEncoder passwordEncoder) {
//    return new AuthUserDetailServiceImpl(dataSource, passwordEncoder);
//  }

  @Bean
  @ConditionalOnMissingBean
  public AuthUserService authUserService(
      CustomizableAppUserService customizableAppUserService,
      AuthUserDetailsService authUserDetailService,
      AppUserService appUserService,
      AppUserAuthUserRepository appUserAuthUserRepository) {
    return new AuthUserServiceImpl(customizableAppUserService, authUserDetailService, appUserService, appUserAuthUserRepository);
  }

  @Bean
  @ConditionalOnMissingBean
  public AppUserLocalAuthorities appUserLocalAuthorities(AuthUserService authUserService) {
    return new AppUserLocalAuthorities(authUserService);
  }
}
