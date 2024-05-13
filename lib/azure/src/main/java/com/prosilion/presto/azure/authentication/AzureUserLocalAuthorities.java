package com.prosilion.presto.azure.authentication;

import com.prosilion.presto.security.AppUserLocalAuthorities;
import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorityAuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

public class AzureUserLocalAuthorities implements AuthorizationManager<RequestAuthorizationContext> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AzureUserLocalAuthorities.class);
	private final AppUserLocalAuthorities appUserLocalAuthorities;
	public AzureUserLocalAuthorities(AppUserLocalAuthorities appUserLocalAuthorities) {
		LOGGER.info("ADOAUTH2 - Using Local Authorities");
		this.appUserLocalAuthorities = appUserLocalAuthorities;
	}

	/**
	 * Hooks into spring-security mechanism to intercept post-user-authentication flow
	 * to apply our own local-db-user authorization
	 * @param authentication
	 * @param object
	 * @return
	 */
	@Override
	public AuthorizationDecision check(@NonNull Supplier<Authentication> authentication, RequestAuthorizationContext object) {
		Authentication suppliedAuthentication = authentication.get();
		if (!suppliedAuthentication.isAuthenticated())
			return new AuthorityAuthorizationDecision(false, getGrantedAuthorities(suppliedAuthentication));

		return new AuthorityAuthorizationDecision(isAuthorized(suppliedAuthentication), getGrantedAuthorities(suppliedAuthentication));
	}

	private boolean isAuthorized(Authentication authentication) {
		return authentication.getAuthorities().stream().anyMatch(grantedAuthority -> getGrantedAuthorities(authentication).contains(grantedAuthority));
	}

	private Collection<GrantedAuthority> getGrantedAuthorities(Authentication authentication) {
		String name = authentication.getPrincipal().toString();
		Set<String> authorities = AuthorityUtils.authorityListToSet(appUserLocalAuthorities.getGrantedAuthorities(name));
		return AuthorityUtils.createAuthorityList(authorities);
	}
}