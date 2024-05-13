package com.prosilion.presto.web.service;

import com.prosilion.presto.security.service.AuthUserService;
import com.prosilion.presto.web.model.AppUserDto;
import java.util.List;
import lombok.NonNull;

public class AppUserDtoServiceImpl implements AppUserDtoService {

	private final AuthUserService authUserService;

	public AppUserDtoServiceImpl(@NonNull AuthUserService authUserService) {
		this.authUserService = authUserService;
	}

	@Override
	public List<AppUserDto> getAllAppUsersAsDto() {
		return convertAppUserToDto(authUserService.getAllAppUsersMappedAuthUsers());
	}
}
