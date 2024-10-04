package com.prosilion.presto.nostr.service;

import com.prosilion.presto.security.entity.NostrAuthUserDetails;
import com.prosilion.presto.security.entity.NostrAuthUserDetailsImpl;
import com.prosilion.presto.security.PreExistingUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.text.MessageFormat;

@Slf4j
public class NostrAuthUserDetailsServiceImpl extends JdbcUserDetailsManager implements NostrAuthUserDetailsService {
  private static final String NOSTR_DEFAULT_ROLE = "USER";
  private final PasswordEncoder passwordEncoder;

  public NostrAuthUserDetailsServiceImpl(DataSource dataSource, PasswordEncoder passwordEncoder) {
    super(dataSource);
    log.info("NOSTR - Loading NostrAuthUserDetailServiceImpl");
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  @Override
  public NostrAuthUserDetails createAuthUser(String username, String password, String pubKey) throws PreExistingUserException {
    UserDetails authUser = createAuthUser(username, password, pubKey, NOSTR_DEFAULT_ROLE);
    NostrAuthUserDetails nostrAuthUserDetails = createNostrAuthUserDetails(authUser, pubKey);
    return loadUserByUsernameAndPubKey(username, pubKey);
  }

  public UserDetails createAuthUser(String username, String password, String pubKey, String role) throws PreExistingUserException {
    log.info("NOSTR - Attempting to create local-db user");
    if (userExists(username)) {
      throw new PreExistingUserException(MessageFormat.format("User [{0}] already exists", username));
    }

    log.info("NOSTR - Creating new local-db user [{}]", username);
    return buildUserDetails(username, password, pubKey, role);
  }

  public UserDetails buildUserDetails(String username, String password, String pubKey, String role) {
    UserDetails userDetails = User.withUsername(username).password(passwordEncoder.encode(password)).roles(role).build();
    NostrAuthUserDetails nostrAuthUserDetails = createNostrAuthUserDetails(userDetails, pubKey);
    return nostrAuthUserDetails;
  }

  @Override
  public NostrAuthUserDetails loadUserByUsernameAndPubKey(String username, String pubKey) throws UsernameNotFoundException {
    UserDetails userDetails = super.loadUserByUsername(username);
    NostrAuthUserDetails nostrAuthUserDetails = createNostrAuthUserDetails(userDetails, pubKey);
    return nostrAuthUserDetails;
  }

  private NostrAuthUserDetails createNostrAuthUserDetails(UserDetails authUserDetails, String pubKey) {
    return new NostrAuthUserDetailsImpl(authUserDetails, pubKey);
  }

//  @Override
//  public NostrAuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//    UserDetails userDetails = super.loadUserByUsername(username);
//    return userDetails;
//  }
}
