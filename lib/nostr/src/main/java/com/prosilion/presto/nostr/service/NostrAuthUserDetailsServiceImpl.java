package com.prosilion.presto.nostr.service;

import com.prosilion.presto.nostr.entity.NostrAuthUserDetails;
import com.prosilion.presto.nostr.entity.NostrAuthUserDetailsImpl;
import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.security.entity.AuthUserDetails;
import com.prosilion.presto.security.service.AuthUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class NostrAuthUserDetailsServiceImpl implements NostrAuthUserDetailsService {
  private static final String NOSTR_DEFAULT_ROLE = "USER";
  private final AuthUserDetailsService authUserDetailsService;

  public NostrAuthUserDetailsServiceImpl(AuthUserDetailsService authUserDetailsService) {
    log.info("NOSTR - Loading NostrAuthUserDetailServiceImpl");
    this.authUserDetailsService = authUserDetailsService;
  }

  @Override
  public boolean userExists(String userName) {
    return authUserDetailsService.userExists(userName);
  }

  @Transactional
  @Override
  public NostrAuthUserDetails createAuthUser(String username, String password, String pubKey) throws PreExistingUserException {
    authUserDetailsService.updateUser(
        createNostrAuthUserDetails(
            authUserDetailsService.createAuthUser(username, password),
            pubKey));
    return loadUserByUsername(username, pubKey);
  }

//  public UserDetails createAuthUser(String username, String password, String pubKey, String role) throws PreExistingUserException {
//    log.info("NOSTR - Attempting to create local-db user");
//    if (userExists(username)) {
//      throw new PreExistingUserException(MessageFormat.format("User [{0}] already exists", username));
//    }
//
//    log.info("NOSTR - Creating new local-db user [{}]", username);
//    return buildUserDetails(username, password, pubKey, role);
//  }

//  public UserDetails buildUserDetails(String username, String password, String pubKey, String role) {
//    return createNostrAuthUserDetails(
//        authUserDetailsService.,
//        pubKey);
//  }

  @Override
  public NostrAuthUserDetails loadUserByUsername(String username, String pubKey) throws UsernameNotFoundException {
    return createNostrAuthUserDetails(
        authUserDetailsService.loadUserByUsername(username),
        pubKey);
  }

  private NostrAuthUserDetails createNostrAuthUserDetails(AuthUserDetails authUserDetails, String pubKey) {
    return new NostrAuthUserDetailsImpl(authUserDetails, pubKey);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return authUserDetailsService.loadUserByUsername(username);
  }
}
