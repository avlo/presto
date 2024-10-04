package com.prosilion.presto.security.entity;

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
public class NostrAuthUserDetailsImpl implements NostrAuthUserDetails {
  private final UserDetails authUserDetails;
  private String pubKey;

  public NostrAuthUserDetailsImpl(@NonNull UserDetails authUserDetails, @NonNull String pubKey) {
    this.authUserDetails = authUserDetails;
    this.pubKey = pubKey;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authUserDetails.getAuthorities();
  }

//  @Override
//  public UserDetails getUser() {
//    return authUserDetails.getUser();
//  }

  @Override
  public String getPassword() {
    return authUserDetails.getPassword();
  }

  @Override
  public String getUsername() {
    return authUserDetails.getUsername();
  }

  @Override
  public String getPubKey() {
    return pubKey;
  }

  @Override
  public void setPubKey(String pubKey) {
    this.pubKey = pubKey;
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
