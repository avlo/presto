package com.prosilion.presto;

import com.prosilion.presto.nostr.controller.NostrAuthController;
import com.prosilion.presto.nostr.service.NostrAppUserDtoService;
import com.prosilion.presto.nostr.service.NostrAppUserDtoServiceImpl;
import com.prosilion.presto.nostr.service.NostrAuthUserDetailsService;
import com.prosilion.presto.nostr.service.NostrAuthUserDetailsServiceImpl;
import com.prosilion.presto.nostr.service.NostrAuthUserService;
import com.prosilion.presto.nostr.service.NostrAuthUserServiceImpl;
import com.prosilion.presto.security.repository.AppUserAuthUserRepository;
import com.prosilion.presto.security.service.AppUserService;
import com.prosilion.presto.security.service.CustomizableAppUserService;
import com.prosilion.presto.web.controller.AuthController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
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
//@EnableWebSecurity
@AutoConfigureBefore({JpaSecurityConfig.class, SecurityCoreConfig.class})
public class NostrSecurityConfig {

  @Bean
  AuthController authController(NostrAuthUserService nostrAuthUserService) {
    return new NostrAuthController(nostrAuthUserService);
  }

  @Bean
  @DependsOn("mvc")
  public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc, AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
    log.info("Loading Nostr - Endpoint authorization configuration");
    http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers(mvc.pattern("/css/**")).permitAll()
            .requestMatchers(mvc.pattern("/images/**")).permitAll()
            .requestMatchers(mvc.pattern("/register-nostr")).permitAll()
            .requestMatchers(mvc.pattern("/register")).permitAll()
//        .requestMatchers(mvc.pattern("/users/**")).hasRole("USER")
            .requestMatchers(mvc.pattern("/users/**")).permitAll()
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

  @Bean
  @Primary
  public NostrAuthUserDetailsService authUserDetailsService(DataSource dataSource, PasswordEncoder passwordEncoder) {
    return new NostrAuthUserDetailsServiceImpl(dataSource, passwordEncoder);
  }

  @Bean
  @Primary
  public NostrAuthUserService authUserService(
      CustomizableAppUserService customizableAppUserService,
      NostrAuthUserDetailsService nostrAuthUserDetailsService,
      AppUserService appUserService,
      AppUserAuthUserRepository appUserAuthUserRepository) {
    return new NostrAuthUserServiceImpl(customizableAppUserService, nostrAuthUserDetailsService, appUserService, appUserAuthUserRepository);
  }

  @Bean
  @Primary
  public NostrAppUserDtoService appUserDtoService(NostrAuthUserService authUserService) {
    return new NostrAppUserDtoServiceImpl(authUserService);
  }
}
