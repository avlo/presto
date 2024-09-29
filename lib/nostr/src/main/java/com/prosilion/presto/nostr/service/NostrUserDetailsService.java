package com.prosilion.presto.nostr.service;

import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.security.entity.AuthUserDetails;
import com.prosilion.presto.security.service.AuthUserDetailsService;

public interface NostrUserDetailsService extends AuthUserDetailsService {
  AuthUserDetails createAuthUser(String username, String password, String pubKey) throws PreExistingUserException;
}
