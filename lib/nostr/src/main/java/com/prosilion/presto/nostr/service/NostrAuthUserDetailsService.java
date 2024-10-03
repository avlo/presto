package com.prosilion.presto.nostr.service;

import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.nostr.entity.NostrAuthUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface NostrAuthUserDetailsService extends UserDetailsService {
  boolean userExists(String userName);
  NostrAuthUserDetails createAuthUser(String username, String password, String pubKey) throws PreExistingUserException;
  NostrAuthUserDetails loadUserByUsername(String username, String pubKey);
}
