package com.prosilion.presto.nostr.entity;

import com.prosilion.presto.security.entity.AuthUserDetails;
import lombok.NonNull;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Authentication & authorization user, directly bound to Spring Security.
 * If you'd like a customizable user, use:
 * @see com.prosilion.presto.security.core.entity.AppUser
 *
 * Note: Spring Security using JPA maps this class to "USERS" DB table.
 */
@Scope("session")
public class NostrUserDetailsImpl implements AuthUserDetails {
  private final AuthUserDetails authUserDetails;

  public NostrUserDetailsImpl(@NonNull AuthUserDetails authUserDetails) {
    this.authUserDetails = authUserDetails;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authUserDetails.getAuthorities();
  }

  @Override
  public UserDetails getUser() {
    return authUserDetails.getUser();
  }

  @Override
  public String getPassword() {
    return authUserDetails.getPassword();
  }

  @Override
  public String getUsername() {
    return authUserDetails.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return authUserDetails.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return authUserDetails.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return authUserDetails.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return authUserDetails.isEnabled();
  }
}
