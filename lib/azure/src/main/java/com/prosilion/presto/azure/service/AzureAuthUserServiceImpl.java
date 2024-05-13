package com.prosilion.presto.azure.service;

import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.security.entity.AppUser;
import com.prosilion.presto.security.entity.AppUserAuthUser;
import com.prosilion.presto.security.entity.AuthUserDetails;
import com.prosilion.presto.security.entity.CustomizableAppUser;
import com.prosilion.presto.security.repository.AppUserAuthUserRepository;
import com.prosilion.presto.security.service.AppUserService;
import com.prosilion.presto.security.service.AuthUserDetailsService;
import com.prosilion.presto.security.service.AuthUserService;
import com.prosilion.presto.security.service.CustomizableAppUserService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

@Service
public class AzureAuthUserServiceImpl extends OidcUserService implements AuthUserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AzureAuthUserServiceImpl.class);

  private final CustomizableAppUserService customizableAppUserService;
  private final AppUserAuthUserRepository appUserAuthUserRepository;
  private final AuthUserDetailsService authUserDetailsService;
  private final AppUserService appUserService;

  @Autowired
  public AzureAuthUserServiceImpl(
      CustomizableAppUserService customizableAppUserService,
      AuthUserDetailsService authUserDetailsService,
      AppUserService appUserService,
      AppUserAuthUserRepository appUserAuthUserRepository) {
    LOGGER.info("ADOAUTH2 - Loading AzureAuthUserServiceImpl");
    this.customizableAppUserService = customizableAppUserService;
    this.authUserDetailsService = authUserDetailsService;
    this.appUserService = appUserService;
    this.appUserAuthUserRepository = appUserAuthUserRepository;
  }

  @Override
  public boolean userExists(String userName) {
    return authUserDetailsService.userExists(userName);
  }

  @Override
  public AppUserAuthUser getAppuserAuthuser(String username) {
    return appUserAuthUserRepository.getAppUserAuthUserByAuthUserName(username);
  }

  @Transactional
  @Override
  public AppUserAuthUser createUser(@NonNull String username, @NonNull String password) throws PreExistingUserException {
    if (userExists(username)) {
      LOGGER.info("Pre-existing user [{}]", username);
      throw new PreExistingUserException(MessageFormat.format("Pre-existing user [{0}]", username));
    }

    AuthUserDetails savedAuthUserDetails = authUserDetailsService.createAuthUser(username, password);
    CustomizableAppUser<AppUser> appUser = appUserService.save(customizableAppUserService.createNewAppUser());

    AppUserAuthUser appUserAuthUser = new AppUserAuthUser(appUser.getId(), savedAuthUserDetails.getUsername());
    return appUserAuthUserRepository.saveAndFlush(appUserAuthUser);
  }

  @Override
  public List<AppUserAuthUser> getAllAppUsersMappedAuthUsers() {
    return appUserAuthUserRepository.findAll();
  }

  @Override
  public Collection<GrantedAuthority> getGrantedAuthorities(@NonNull String username) {
    return List.copyOf(authUserDetailsService.loadUserByUsername(username).getAuthorities());
  }

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUser oidcUser = super.loadUser(userRequest);
    try {
      createUser(oidcUser.getPreferredUsername(), "NO-OP");
    } catch (PreExistingUserException e) {
      LOGGER.info(e.getMessage());
    }
    return oidcUser;
  }
}
