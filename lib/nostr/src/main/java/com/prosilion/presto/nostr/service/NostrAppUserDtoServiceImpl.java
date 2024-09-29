package com.prosilion.presto.nostr.service;

import com.prosilion.presto.nostr.dto.model.NostrAppUserDto;
import com.prosilion.presto.nostr.dto.model.NostrAppUserDtoIF;
import com.prosilion.presto.security.entity.AppUserAuthUser;
import com.prosilion.presto.security.service.AuthUserService;
import com.prosilion.presto.web.model.AppUserDto;
import com.prosilion.presto.web.model.AppUserDtoIF;
import com.prosilion.presto.web.service.AppUserDtoService;
import lombok.NonNull;

import java.util.List;

public class NostrAppUserDtoServiceImpl implements AppUserDtoService {

  private final AuthUserService authUserService;

  public NostrAppUserDtoServiceImpl(@NonNull AuthUserService authUserService) {
    this.authUserService = authUserService;
  }

  @Override
  public List<AppUserDtoIF> getAllAppUsersAsDto() {
    return convertAppUserToDto(authUserService.getAllAppUsersMappedAuthUsers());
  }

  private List<AppUserDtoIF> convertAppUserToDto(List<AppUserAuthUser> users) {
    return users.stream().map(this::mapToUserDto).toList();
  }

  AppUserDtoIF mapToUserDto(AppUserAuthUser appUserAuthUser) {
    NostrAppUserDtoIF userDto = new NostrAppUserDto(new AppUserDto());
    userDto.setUsername(appUserAuthUser.getAuthUserName());
    userDto.setId(appUserAuthUser.getAppUserId());
    return userDto;
  }
}
