package com.prosilion.presto.web.service;

import com.prosilion.presto.security.entity.AppUserAuthUser;
import com.prosilion.presto.web.model.AppUserDto;

import java.util.List;

public interface AppUserDtoService {
	List<AppUserDto> getAllAppUsersAsDto();

	default List<AppUserDto> convertAppUserToDto(List<AppUserAuthUser> users) {
		return users.stream().map(this::mapToUserDto).toList();
	}
	default AppUserDto mapToUserDto(AppUserAuthUser appUserAuthUser) {
		AppUserDto userDto = new AppUserDto();
		userDto.setUsername(appUserAuthUser.getAuthUserName());
		userDto.setId(appUserAuthUser.getAppUserId());
		return userDto;
	}
}
