package com.prosilion.presto.security.service;

import com.prosilion.presto.security.entity.AppUserAuthUser;
import com.prosilion.presto.security.PreExistingUserException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface AuthUserService {

  boolean userExists(String userName);
  AppUserAuthUser getAppuserAuthuser(String username);

  AppUserAuthUser createUser(String username, String password) throws PreExistingUserException;

  List<AppUserAuthUser> getAllAppUsersMappedAuthUsers();

  Collection<GrantedAuthority> getGrantedAuthorities(String username);
}
