package com.prosilion.presto.security.entity;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthUserDetails extends UserDetails {
  UserDetails getUser();
}
