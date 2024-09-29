package com.prosilion.presto.nostr.dto.model;

import com.prosilion.presto.web.model.AppUserDtoIF;

public interface NostrAppUserDtoIF extends AppUserDtoIF {
  String getPubKey();
  void setPubKey(String pubKey);
}
