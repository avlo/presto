package com.prosilion.presto.nostr.entity;

import com.prosilion.presto.security.entity.AuthUserDetails;

public interface NostrAuthUserDetails extends AuthUserDetails {
  String getPubKey();
  void setPubKey(String pubkey);
}
