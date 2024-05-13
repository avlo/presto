package com.prosilion.presto.security.service;

import com.prosilion.presto.security.entity.AppUser;
import com.prosilion.presto.security.entity.CustomizableAppUser;

public class CustomizableAppUserService {
	private final AppUser appUser;
	public CustomizableAppUserService(AppUser appUser) {
		this.appUser = appUser;
	}
	public CustomizableAppUser<AppUser> createNewAppUser() {
		return appUser.createNewCustomAppUserInstance();
	}
}
