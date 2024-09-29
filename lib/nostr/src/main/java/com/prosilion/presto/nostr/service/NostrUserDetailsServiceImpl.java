package com.prosilion.presto.nostr.service;

import com.prosilion.presto.nostr.entity.NostrUserDetailsImpl;
import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.security.entity.AuthUserDetails;
import com.prosilion.presto.security.entity.AuthUserDetailsImpl;
import com.prosilion.presto.security.service.AuthUserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class NostrUserDetailsServiceImpl implements NostrUserDetailsService {
  private final AuthUserDetailServiceImpl authUserDetailsService;

  private static final String NOSTR_DEFAULT_ROLE = "ANONYMOUS";

  public NostrUserDetailsServiceImpl(AuthUserDetailServiceImpl authUserDetailsService) {
    log.info("ADOAUTH2 - Loading AzureUserDetailServiceImpl");
    this.authUserDetailsService = authUserDetailsService;
  }

  @Override
  public boolean userExists(String userName) {
    return authUserDetailsService.userExists(userName);
  }

  @Override
  public AuthUserDetails createAuthUser(String username, String password) throws PreExistingUserException {
    return null;
  }

  @Override
  public AuthUserDetails createAuthUser(String username, String password, String pubKey) throws PreExistingUserException {
    authUserDetailsService.createUser(new NostrUserDetailsImpl(new AuthUserDetailsImpl(authUserDetailsService.createAuthUser(username, password, NOSTR_DEFAULT_ROLE))));
    return loadUserByUsername(username);
  }

  @Override
  public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return new NostrUserDetailsImpl(authUserDetailsService.loadUserByUsername(username));
  }
}
