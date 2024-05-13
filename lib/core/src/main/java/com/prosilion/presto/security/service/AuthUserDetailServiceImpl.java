package com.prosilion.presto.security.service;

import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.security.entity.AuthUserDetails;
import com.prosilion.presto.security.entity.AuthUserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.text.MessageFormat;

public class AuthUserDetailServiceImpl extends JdbcUserDetailsManager implements AuthUserDetailsService {
  private static final Logger NON_CLASHING_LOGGER = LoggerFactory.getLogger(AuthUserDetailServiceImpl.class);
  private final PasswordEncoder passwordEncoder;
  private static final String JSP_ROLE = "USER";
  private static final String AZURE_ROLE = "ACTIVE";

  public AuthUserDetailServiceImpl(DataSource dataSource, PasswordEncoder passwordEncoder) {
    super(dataSource);
    NON_CLASHING_LOGGER.info("Loading AuthUserDetailServiceImpl");
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  @Override
  public AuthUserDetails createAuthUser(String username, String password) throws PreExistingUserException {
    createUser(new AuthUserDetailsImpl(createAuthUser(username, password, JSP_ROLE)));
    return loadUserByUsername(username);
  }

  public UserDetails createAuthUser(String username, String password, String role) throws PreExistingUserException {
    NON_CLASHING_LOGGER.info("JPA/LDAP - Attempting to create local-db user");
    if (userExists(username)) {
      throw new PreExistingUserException(MessageFormat.format("User [{0}] already exists", username));
    }

    NON_CLASHING_LOGGER.info("JPA/LDAP - Creating new local-db user [{}]", username);
    return buildUserDetails(username, password, role);
  }

  public UserDetails buildUserDetails(String username, String password, String role) {
    return User.withUsername(username).password(passwordEncoder.encode(password)).roles(role).build();
  }

  @Override
  public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return new AuthUserDetailsImpl(super.loadUserByUsername(username));
  }
}
