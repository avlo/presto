package com.prosilion.presto.nostr.service;

import com.prosilion.presto.nostr.dto.model.NostrAppUserDto;
import com.prosilion.presto.web.model.NostrAppUserDtoIF;
import com.prosilion.presto.security.entity.AppUserAuthUser;
import lombok.NonNull;

import java.util.List;

public class NostrAppUserDtoServiceImpl<T extends NostrAppUserDtoIF> implements NostrAppUserDtoService<T> {
  private final NostrAuthUserService nostrAuthUserService;

  public NostrAppUserDtoServiceImpl(@NonNull NostrAuthUserService nostrAuthUserService) {
    this.nostrAuthUserService = nostrAuthUserService;
  }

  @Override
  public List<T> getAllAppUsersAsDto() {
    return convertAppUserToDto(nostrAuthUserService.getAllAppUsersMappedAuthUsers());
  }

  private List<T> convertAppUserToDto(List<AppUserAuthUser> users) {
    return users.stream().map(this::mapToUserDto).toList();
  }

  T mapToUserDto(AppUserAuthUser appUserAuthUser) {
    T userDto = (T) new NostrAppUserDto();
    userDto.setUsername(appUserAuthUser.getAuthUserName());
    userDto.setId(appUserAuthUser.getAppUserId());
    return userDto;
  }
}
