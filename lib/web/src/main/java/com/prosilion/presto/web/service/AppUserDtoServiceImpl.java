package com.prosilion.presto.web.service;

import com.prosilion.presto.security.entity.AppUserAuthUser;
import com.prosilion.presto.security.service.AuthUserService;
import com.prosilion.presto.web.model.AppUserDto;
import com.prosilion.presto.web.model.AppUserDtoIF;
import lombok.NonNull;

import java.util.List;

public class AppUserDtoServiceImpl implements AppUserDtoService {

  private final AuthUserService authUserService;

  public AppUserDtoServiceImpl(@NonNull AuthUserService authUserService) {
    this.authUserService = authUserService;
  }

  @Override
  public List<AppUserDtoIF> getAllAppUsersAsDto() {
    return convertAppUserToDto(authUserService.getAllAppUsersMappedAuthUsers());
  }

  private List<AppUserDtoIF> convertAppUserToDto(List<AppUserAuthUser> users) {
    return users.stream().map(this::mapToUserDto).toList();
  }

  private AppUserDtoIF mapToUserDto(AppUserAuthUser appUserAuthUser) {
    AppUserDtoIF userDto = new AppUserDto();
    userDto.setUsername(appUserAuthUser.getAuthUserName());
    userDto.setId(appUserAuthUser.getAppUserId());
    return userDto;
  }
}
