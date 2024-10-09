package com.prosilion.presto.nostr.service;

import com.prosilion.presto.nostr.db.NostrJdbcUserDetailsManager;
import com.prosilion.presto.nostr.entity.NostrUser;
import com.prosilion.presto.nostr.entity.NostrUser.NostrUserBuilder;
import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.security.entity.NostrUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.text.MessageFormat;

@Slf4j
public class NostrUserDetailsServiceImpl extends NostrJdbcUserDetailsManager implements NostrUserDetailsService {
  private static final String NOSTR_DEFAULT_ROLE = "USER";
  private final PasswordEncoder passwordEncoder;

  public NostrUserDetailsServiceImpl(DataSource dataSource, PasswordEncoder passwordEncoder) {
    super(dataSource);
    log.info("NOSTR - Loading NostrAuthUserDetailServiceImpl");
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  @Override
  public NostrUserDetails createAuthUser(String username, String password, String pubKey) throws PreExistingUserException {
    NostrUserDetails authUser = createAuthUser(username, password, pubKey, NOSTR_DEFAULT_ROLE);
    createUser(authUser);
    NostrUserDetails nostrUserDetails = loadUserByUsernameAndPubKey(username, pubKey);
    return nostrUserDetails;
  }

  public NostrUserDetails createAuthUser(String username, String password, String pubKey, String role) throws PreExistingUserException {
    log.info("NOSTR - Attempting to create local-db user");
    if (userExists(username)) {
      throw new PreExistingUserException(MessageFormat.format("User [{0}] already exists", username));
    }

    log.info("NOSTR - Creating new local-db user [{}]", username);
    NostrUserDetails nostrUserDetails = buildUserDetails(username, password, pubKey, role);
    return nostrUserDetails;
  }

  public NostrUserDetails buildUserDetails(String username, String password, String pubKey, String role) {
    NostrUserBuilder builder = NostrUser.withNostrUsername(username).password(passwordEncoder.encode(password)).pubkey(pubKey);
    NostrUserDetails userDetails = builder.roles(role).build();
    return userDetails;
  }

  @Override
  public NostrUserDetails loadUserByUsernameAndPubKey(String username, String pubKey) throws UsernameNotFoundException {
    NostrUserDetails nostrUserDetails = super.loadUserByUsername(username);
    return nostrUserDetails;
  }
}
