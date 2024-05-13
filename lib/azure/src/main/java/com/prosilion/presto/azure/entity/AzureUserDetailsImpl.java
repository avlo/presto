package com.prosilion.presto.azure.entity;

import com.prosilion.presto.security.entity.AuthUserDetails;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Authentication & authorization user, directly bound to Spring Security.
 * If you'd like a customizable user, use:
 * @see com.prosilion.presto.security.core.entity.AppUser
 *
 * Note: Spring Security using JPA maps this class to "USERS" DB table.
 */
@Scope("session")
public class AzureUserDetailsImpl implements GrantedAuthoritiesMapper, AuthUserDetails {
  private final AuthUserDetails authUserDetails;
  private final transient GrantedAuthoritiesMapper grantedAuthoritiesMapper;

  public AzureUserDetailsImpl(@NotNull AuthUserDetails authUserDetails) {
    this.authUserDetails = authUserDetails;
    this.grantedAuthoritiesMapper = new SimpleAuthorityMapper();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authUserDetails.getAuthorities();
  }

  @Override
  public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
    return grantedAuthoritiesMapper.mapAuthorities(authorities);
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
