package com.prosilion.presto.web.model;

public interface NostrAppUserDtoIF extends AppUserDtoIF {
  String getPubkey();
  void setPubkey(String pubkey);
}
