package com.prosilion.presto.nostr.db;

import com.prosilion.presto.security.entity.NostrUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface NostrUserDetailsManager extends UserDetailsService {
  void createUser(NostrUserDetails user);

  void updateUser(NostrUserDetails user);

  void deleteUser(String username);

  void changePassword(String oldPassword, String newPassword);

  boolean userExists(String username);
}
