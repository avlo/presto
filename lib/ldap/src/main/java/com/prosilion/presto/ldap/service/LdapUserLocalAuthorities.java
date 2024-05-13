package com.prosilion.presto.ldap.service;

import com.prosilion.presto.security.AppUserLocalAuthorities;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

/**
 * Application authorization/authorities bean serving in place of Ldap authorization/authorities (roles).
 */
public class LdapUserLocalAuthorities implements LdapAuthoritiesPopulator {
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapUserLocalAuthorities.class);
	private final AppUserLocalAuthorities appUserLocalAuthorities;

	public LdapUserLocalAuthorities(AppUserLocalAuthorities appUserLocalAuthorities) {
		LOGGER.info("LDAP - Using Local Authorities");
		this.appUserLocalAuthorities = appUserLocalAuthorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
		LOGGER.info("LDAP - Getting Granted Authorities");
		return appUserLocalAuthorities.getGrantedAuthorities(username);
	}
}
