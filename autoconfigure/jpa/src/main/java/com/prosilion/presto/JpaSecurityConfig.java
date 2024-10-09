package com.prosilion.presto;

import com.prosilion.presto.jpa.controller.JpaAuthController;
import com.prosilion.presto.security.service.AuthUserDetailServiceImpl;
import com.prosilion.presto.security.service.AuthUserDetailsService;
import com.prosilion.presto.security.service.AuthUserService;
import com.prosilion.presto.web.controller.AuthController;
import com.prosilion.presto.web.model.AppUserDtoIF;
import com.prosilion.presto.web.service.AppUserDtoService;
import com.prosilion.presto.web.service.AppUserDtoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
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
public class JpaSecurityConfig {

  @Bean
  @ConditionalOnMissingBean
  AuthController authController(AuthUserService authUserService) {
    return new JpaAuthController(authUserService);
  }

  @Bean
  @ConditionalOnMissingBean
  public AuthenticationSuccessHandler defaultAuthenticationSuccessHandler() {
    return new DefaultLoginHandler();
  }

  @Bean
  @DependsOn("mvc")
  @ConditionalOnMissingFilterBean
  public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc, AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
    log.info("Loading JPA - Endpoint authorization configuration");
    http.authorizeHttpRequests(authorize -> authorize
        .requestMatchers(mvc.pattern("/css/**")).permitAll()
        .requestMatchers(mvc.pattern("/images/**")).permitAll()
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


  @Bean(name = "userDetailsService")
  @ConditionalOnMissingBean(name = "userDetailsService")
  public AuthUserDetailsService userDetailsService(DataSource dataSource, PasswordEncoder passwordEncoder) {
    return new AuthUserDetailServiceImpl(dataSource, passwordEncoder);
  }

  @Bean
  @ConditionalOnMissingBean
  public AppUserDtoService<AppUserDtoIF> appUserDtoService(AuthUserService authUserService) {
    return new AppUserDtoServiceImpl<>(authUserService);
  }
}
