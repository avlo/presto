package com.prosilion.presto;

import com.prosilion.presto.nostr.service.NostrAppUserDtoService;
import com.prosilion.presto.nostr.service.NostrAppUserDtoServiceImpl;
import com.prosilion.presto.nostr.service.NostrUserDetailsService;
import com.prosilion.presto.nostr.service.NostrUserDetailsServiceImpl;
import com.prosilion.presto.nostr.service.NostrUserService;
import com.prosilion.presto.nostr.service.NostrUserServiceImpl;
import com.prosilion.presto.security.repository.AppUserAuthUserRepository;
import com.prosilion.presto.security.service.AppUserService;
import com.prosilion.presto.security.service.CustomizableAppUserService;
import com.prosilion.presto.web.model.NostrAppUserDtoIF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

import javax.sql.DataSource;

@Slf4j
@AutoConfiguration
public class NostrSecurityConfig {

//  @Bean
//  AuthController authController(NostrUserService nostrUserService) {
//    return new NostrAuthController(nostrUserService);
//  }

  @Bean
  @DependsOn("mvc")
  public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc, AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
    log.info("Loading JPA - Endpoint authorization configuration");
    http.authorizeHttpRequests(authorize -> authorize
        .requestMatchers(mvc.pattern("/css/**")).permitAll()
        .requestMatchers(mvc.pattern("/images/**")).permitAll()
        .requestMatchers(mvc.pattern("/register")).permitAll()
        .requestMatchers(mvc.pattern("/register-nostr")).permitAll()
        .requestMatchers(mvc.pattern("/users/**")).hasRole("USER")
        .anyRequest().authenticated() // anyRequest() defines a rule chain for any request which did not match the previous rules
    ).formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/loginuser")
//				.defaultSuccessUrl("/users")
            .successHandler(authenticationSuccessHandler)
            .permitAll()
    ).logout(logout -> logout
        .logoutRequestMatcher(mvc.pattern("/logout")).permitAll()
    );
    return http.build();
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
