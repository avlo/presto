package com.prosilion.presto.security.entity;

import org.springframework.security.core.userdetails.UserDetails;

public interface NostrUserDetails extends UserDetails {
  String getPubkey();
  void setPubkey(String pubkey);
}
