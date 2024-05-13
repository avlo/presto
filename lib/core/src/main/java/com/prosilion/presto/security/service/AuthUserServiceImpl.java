package com.prosilion.presto.security.service;

import com.prosilion.presto.security.entity.AppUser;
import com.prosilion.presto.security.entity.AppUserAuthUser;
import com.prosilion.presto.security.repository.AppUserAuthUserRepository;
import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.security.entity.AuthUserDetails;
import com.prosilion.presto.security.entity.CustomizableAppUser;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

public class AuthUserServiceImpl implements AuthUserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthUserServiceImpl.class);

  private final CustomizableAppUserService customizableAppUserService;
  private final AppUserAuthUserRepository appUserAuthUserRepository;
  private final AuthUserDetailsService authUserDetailsService;
  private final AppUserService appUserService;

  public AuthUserServiceImpl(CustomizableAppUserService customizableAppUserService, AuthUserDetailsService authUserDetailsService, AppUserService appUserService, AppUserAuthUserRepository appUserAuthUserRepository) {
    LOGGER.info("AMBIGUOUS - Loading AuthUserServiceImpl");
    this.customizableAppUserService = customizableAppUserService;
    this.appUserAuthUserRepository = appUserAuthUserRepository;
    this.authUserDetailsService = authUserDetailsService;
    this.appUserService = appUserService;
  }

  @Override
  public boolean userExists(String userName) {
    return authUserDetailsService.userExists(userName);
  }

  @Transactional
  @Override
  public AppUserAuthUser createUser(@NonNull String username, @NonNull String password) throws PreExistingUserException {
    if (userExists(username)) {
      LOGGER.info("Pre-existing user [{}]", username);
      throw new PreExistingUserException(MessageFormat.format("Pre-existing user [{0}]", username));
    }

    AuthUserDetails savedAuthUserDetails = authUserDetailsService.createAuthUser(username, password);
    CustomizableAppUser<AppUser>  appUser = appUserService.save(customizableAppUserService.createNewAppUser());

    AppUserAuthUser appUserAuthUser = new AppUserAuthUser(appUser.getId(), savedAuthUserDetails.getUsername());
    return appUserAuthUserRepository.saveAndFlush(appUserAuthUser);
  }

  @Override
  public AppUserAuthUser getAppuserAuthuser(String username) {
    return appUserAuthUserRepository.getAppUserAuthUserByAuthUserName(username);
  }

  @Override
  public List<AppUserAuthUser> getAllAppUsersMappedAuthUsers() {
    return appUserAuthUserRepository.findAll();
  }

  @Override
  public Collection<GrantedAuthority> getGrantedAuthorities(@NonNull String username) {
    return List.copyOf(authUserDetailsService.loadUserByUsername(username).getAuthorities());
  }
}
