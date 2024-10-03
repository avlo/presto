package com.prosilion.presto.security.service;

import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.security.entity.AuthUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthUserDetailsService extends UserDetailsService {
	boolean userExists(String userName);
	AuthUserDetails createAuthUser(String username, String password) throws PreExistingUserException;
	void updateUser(final UserDetails user);
	AuthUserDetails loadUserByUsername(String username);
}
