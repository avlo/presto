package com.prosilion.presto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher.Builder;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Slf4j
@AutoConfiguration
public class WebCommonConfig {

  @Bean("mvc")
  public Builder mvc() {
    log.info("WebCommonConfig mvc() called, MvcRequestMatcher.Builder created...");
    return new MvcRequestMatcher.Builder(new HandlerMappingIntrospector());
  }

  @Bean
  @ConditionalOnMissingBean
  public AuthenticationSuccessHandler defaultAuthenticationSuccessHandler() {
    log.info("Loading DefaultAuthenticationSuccessHandler");
    return new DefaultLoginHandler();
  }

}
