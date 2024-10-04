package com.prosilion.presto.security.entity;

import org.springframework.security.core.userdetails.UserDetails;

public interface NostrAuthUserDetails extends UserDetails {
  //  UserDetails getUser();
  String getPubKey();
  void setPubKey(String pubkey);
}
