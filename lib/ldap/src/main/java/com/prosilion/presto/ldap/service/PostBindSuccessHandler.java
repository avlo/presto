package com.prosilion.presto.ldap.service;

import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.security.service.AuthUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;

/**
 * Subclass for extending LDAP user post-authentication functionality
 */
public class PostBindSuccessHandler extends FilterBasedLdapUserSearch {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostBindSuccessHandler.class);
	private static final String REFACTOR_LOCAL_LDAP_PASSWORD = "NO-OP";
	private final AuthUserService authUserService;

	public PostBindSuccessHandler(String searchBase, String searchFilter, BaseLdapPathContextSource contextSource, AuthUserService authUserService) {
		super(searchBase, searchFilter, contextSource);
		this.authUserService = authUserService;
	}

	/**
	 * Responsible for creating/retrieving local-db user after successful LDAP authentication
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	//	@Secured({ "ROLE_ANONYMOUS", "ANONYMOUS","ROLE_USER", "USER"})
	public DirContextOperations searchForUser(String username) throws UsernameNotFoundException {
		try {
			// TODO: need to find better mechanism than this (continued below)...
			super.searchForUser(username);
		} catch (UsernameNotFoundException e) {
			LOGGER.info("LDAP - User not found.");
			throw e;
		}
		LOGGER.info("LDAP - authentication successful.");
		LOGGER.info("LDAP - Creating local user & user authorities (does NOT use LDAP authorities)");
		try {
			authUserService.createUser(username, REFACTOR_LOCAL_LDAP_PASSWORD);
		} catch (PreExistingUserException e) {
			LOGGER.info(e.getMessage());
		}
		// TODO-cont... same method called twice == hack!
		return super.searchForUser(username);
	}
}