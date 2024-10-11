package com.prosilion.presto.nostr.service;

import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.security.entity.AppUserAuthUser;
import com.prosilion.presto.security.service.AuthUserService;

public interface NostrUserService extends AuthUserService {
  AppUserAuthUser createUser(String username, String password, String pubKey) throws PreExistingUserException;
  AppUserAuthUser findUserByPubkey(String pubkey);
}
