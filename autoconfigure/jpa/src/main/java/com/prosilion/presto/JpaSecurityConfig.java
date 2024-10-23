package com.prosilion.presto;

import com.prosilion.presto.security.service.AuthUserService;
import com.prosilion.presto.web.model.AppUserDtoIF;
import com.prosilion.presto.web.service.AppUserDtoService;
import com.prosilion.presto.web.service.AppUserDtoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

@Slf4j
@AutoConfiguration
@EnableWebSecurity
public class JpaSecurityConfig {

  @Bean
  @DependsOn("mvc")
  public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc, AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
    log.info("Loading JPA - Endpoint authorization configuration");
//        TODO: below should redirect http requests to https, but does not.  revisit
    http.requiresChannel(channel -> channel
        .anyRequest().requiresSecure()
    ).authorizeHttpRequests(authorize -> authorize
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

  @Bean
  public AppUserDtoService<AppUserDtoIF> appUserDtoService(AuthUserService authUserService) {
    return new AppUserDtoServiceImpl<>(authUserService);
  }
}
