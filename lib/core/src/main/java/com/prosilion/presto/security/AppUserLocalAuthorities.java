package com.prosilion.presto.security;

import com.prosilion.presto.security.service.AuthUserService;

import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

public class AppUserLocalAuthorities implements LocalAuthorities {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppUserLocalAuthorities.class);
	private final AuthUserService authUserService;

	public AppUserLocalAuthorities(AuthUserService authUserService) {
		this.authUserService = authUserService;
	}

	@Override
	public Collection<GrantedAuthority> getGrantedAuthorities(String username) {
		LOGGER.info("COMMON - Getting local user authorities from DB");
		return authUserService.getGrantedAuthorities(username);
	}
}