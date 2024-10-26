package com.prosilion.presto;

import com.prosilion.presto.ldap.service.LdapUserLocalAuthorities;
import com.prosilion.presto.ldap.service.PostBindSuccessHandler;
import com.prosilion.presto.security.AppUserLocalAuthorities;
import com.prosilion.presto.security.service.AuthUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

@AutoConfiguration
@ConditionalOnClass(LdapTemplate.class)
public class LdapSecurityConfig {
  private static final Logger LOGGER = LoggerFactory.getLogger(LdapSecurityConfig.class);
  public static final String OBJECT_CLASS = "objectClass";
  public static final String USER_SEARCH_FILTER = "{0}";
  public static final String DIST_VARIABLE = "cn";
  public static final String USER = "user";

  @Value("${ldap.search.base}")
  private String ldapSearchBase;

  @Bean
  @DependsOn("mvc")
  public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
    LOGGER.info("Loading LDAP - Endpoint authorization configuration");
    http.authorizeHttpRequests(authorize -> authorize
        .requestMatchers(mvc.pattern("/css/**")).permitAll()
        .requestMatchers(mvc.pattern("/images/**")).permitAll()
        .requestMatchers(mvc.pattern("/users/**")).hasRole("USER")
        .anyRequest().authenticated() // anyRequest() defines a rule chain for any request which did not match the previous rules
    ).formLogin(form -> form
        .loginPage("/login")
        .loginProcessingUrl("/loginuser")
        .defaultSuccessUrl("/users")
        .permitAll()
    ).logout(logout -> logout
        .logoutRequestMatcher(mvc.pattern("/logout")).permitAll()
    );
    return http.build();
  }

  @Bean
  public LdapUserLocalAuthorities ldapUserLocalAuthorities(AppUserLocalAuthorities appUserLocalAuthorities) {
    return new LdapUserLocalAuthorities(appUserLocalAuthorities);
  }

  @Bean
  public PostBindSuccessHandler postBindSuccessHandler(LdapContextSource ldapContextSource, AuthUserService authUserService) {
    LOGGER.info("Loading LDAP - User Search (FilterBasedLdapUserSearch implements LdapUserSearch)");
    return new PostBindSuccessHandler(ldapSearchBase, getAndFilter(), ldapContextSource, authUserService);
  }

  @Bean
  public BindAuthenticator bindAuthenticator(LdapContextSource ldapContextSource, PostBindSuccessHandler postBindSuccessHandler) {
    LOGGER.info("Loading LDAP - Authenticator (BindAuthenticator extends AbstractLdapAuthenticator implements LdapAuthenticator)");
    BindAuthenticator authenticator = new BindAuthenticator(ldapContextSource);
    authenticator.setUserSearch(postBindSuccessHandler);
    authenticator.afterPropertiesSet();
    return authenticator;
  }

  @Bean
  public LdapAuthenticationProvider ldapAuthenticationProvider(BindAuthenticator bindAuthenticator, LdapUserLocalAuthorities ldapUserLocalAuthorities) {
    LOGGER.info("Loading LDAP - Authentication Provider (LdapAuthenticationProvider)");
    return new LdapAuthenticationProvider(bindAuthenticator, ldapUserLocalAuthorities);
  }

  private static String getAndFilter() {
    AndFilter filter = new AndFilter();
    filter.and(new EqualsFilter(OBJECT_CLASS, USER));
    filter.and(new EqualsFilter(DIST_VARIABLE, LdapSecurityConfig.USER_SEARCH_FILTER));
    return filter.toString();
  }
}
