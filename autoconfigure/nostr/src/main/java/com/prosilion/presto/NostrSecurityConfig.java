package com.prosilion.presto;

import com.prosilion.presto.nostr.controller.NostrAuthController;
import com.prosilion.presto.nostr.service.NostrAppUserDtoServiceImpl;
import com.prosilion.presto.nostr.service.NostrAuthUserDetailsService;
import com.prosilion.presto.nostr.service.NostrAuthUserDetailsServiceImpl;
import com.prosilion.presto.nostr.service.NostrAuthUserService;
import com.prosilion.presto.security.service.AuthUserDetailsService;
import com.prosilion.presto.web.controller.AuthController;
import com.prosilion.presto.web.service.AppUserDtoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

@Slf4j
@AutoConfiguration
@AutoConfigureBefore({JpaSecurityConfig.class})
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

  @Bean
  public NostrAuthUserDetailsService nostrAuthUserDetailsService(AuthUserDetailsService authUserDetailService) {
//    AuthUserDetailServiceImpl authUserDetailService = new AuthUserDetailServiceImpl(dataSource, passwordEncoder);
    return new NostrAuthUserDetailsServiceImpl(authUserDetailService);
  }

  @Bean
  public AppUserDtoService appUserDtoService(NostrAuthUserService authUserService) {
    return new NostrAppUserDtoServiceImpl(authUserService);
  }
}
