package com.prosilion.presto.nostr.service;

import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.security.entity.NostrUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface NostrUserDetailsService extends UserDetailsService {
  boolean userExists(String userName);
  NostrUserDetails createAuthUser(String username, String password, String pubKey) throws PreExistingUserException;
  NostrUserDetails loadUserByPubKey(String pubKey);
}
