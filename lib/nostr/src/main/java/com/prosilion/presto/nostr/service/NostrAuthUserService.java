package com.prosilion.presto.nostr.service;

import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.security.entity.AppUserAuthUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface NostrAuthUserService {

  boolean userExists(String userName);

  AppUserAuthUser getAppuserAuthuser(String username);

  AppUserAuthUser createUser(String username, String password, String pubKey) throws PreExistingUserException;

  List<AppUserAuthUser> getAllAppUsersMappedAuthUsers();

  Collection<GrantedAuthority> getGrantedAuthorities(String username);
}
