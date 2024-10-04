package com.prosilion.presto.nostr.service;

import com.prosilion.presto.security.entity.NostrAuthUserDetails;
import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.security.entity.AppUser;
import com.prosilion.presto.security.entity.AppUserAuthUser;
import com.prosilion.presto.security.entity.CustomizableAppUser;
import com.prosilion.presto.security.repository.AppUserAuthUserRepository;
import com.prosilion.presto.security.service.AppUserService;
import com.prosilion.presto.security.service.CustomizableAppUserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

@Slf4j
public class NostrAuthUserServiceImpl implements NostrAuthUserService {
  private final CustomizableAppUserService customizableAppUserService;
  private final AppUserAuthUserRepository appUserAuthUserRepository;
  private final NostrAuthUserDetailsService nostrUserDetailService;
  private final AppUserService appUserService;

  public NostrAuthUserServiceImpl(
      CustomizableAppUserService customizableAppUserService,
      NostrAuthUserDetailsService nostrUserDetailService,
      AppUserService appUserService,
      AppUserAuthUserRepository appUserAuthUserRepository) {
    log.info("NOSTR - Loading NostrAuthUserServiceImpl");
    this.customizableAppUserService = customizableAppUserService;
    this.appUserAuthUserRepository = appUserAuthUserRepository;
    this.nostrUserDetailService = nostrUserDetailService;
    this.appUserService = appUserService;
  }

  @Override
  public boolean userExists(String userName) {
    return nostrUserDetailService.userExists(userName);
  }

  @Override
  public AppUserAuthUser createUser(String username, String password) throws PreExistingUserException {
    return null;
  }

  @Transactional
  @Override
  public AppUserAuthUser createUser(
      @NonNull String username,
      @NonNull String password,
      @NonNull String pubKey) throws PreExistingUserException {
    if (userExists(username)) {
      log.info("Pre-existing user [{}]", username);
      throw new PreExistingUserException(MessageFormat.format("Pre-existing user [{0}]", username));
    }

    NostrAuthUserDetails savedAuthUserDetails = nostrUserDetailService.createAuthUser(username, password, pubKey);
    CustomizableAppUser<AppUser> newAppUser = customizableAppUserService.createNewAppUser();
    CustomizableAppUser<AppUser> appUser = appUserService.save(newAppUser);

    AppUserAuthUser appUserAuthUser = new AppUserAuthUser(appUser.getId(), savedAuthUserDetails.getUsername());
    AppUserAuthUser appUserAuthUser1 = appUserAuthUserRepository.saveAndFlush(appUserAuthUser);
    return appUserAuthUser1;
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
    return List.copyOf(nostrUserDetailService.loadUserByUsername(username).getAuthorities());
  }
}