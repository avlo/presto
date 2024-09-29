package com.prosilion.presto.web.service;

import com.prosilion.presto.security.entity.AppUserAuthUser;
import com.prosilion.presto.web.model.AppUserDto;
import com.prosilion.presto.web.model.AppUserDtoIF;

import java.util.List;

public interface AppUserDtoService {
  List<AppUserDtoIF> getAllAppUsersAsDto();

  default List<AppUserDtoIF> convertAppUserToDto(List<AppUserAuthUser> users) {
    return users.stream().map(this::mapToUserDto).toList();
  }
  default AppUserDtoIF mapToUserDto(AppUserAuthUser appUserAuthUser) {
    AppUserDtoIF userDto = new AppUserDto();
    userDto.setUsername(appUserAuthUser.getAuthUserName());
    userDto.setId(appUserAuthUser.getAppUserId());
    return userDto;
  }
}
