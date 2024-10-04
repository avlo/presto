package com.prosilion.presto.security.entity;

import lombok.NonNull;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * Authentication & authorization user, directly bound to Spring Security.
 * If you'd like a customizable user, use:
 * @see com.prosilion.presto.security.core.entity.AppUser
 *
 * Note: Spring Security using JPA maps this class to "USERS" DB table.
 */
@Scope("session")
public class NostrAuthUserDetailsImpl implements NostrAuthUserDetails, Serializable {

  private final UserDetails user;
  private String pubKey;

  public NostrAuthUserDetailsImpl(@NonNull UserDetails user, @NonNull String pubKey) {
    this.user = user;
    this.pubKey = pubKey;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getAuthorities();
  }

  @Override
  public UserDetails getUser() {
    return user;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
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
    return user.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return user.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return user.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return user.isEnabled();
  }
}
