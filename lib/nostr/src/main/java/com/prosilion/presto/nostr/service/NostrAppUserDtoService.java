package com.prosilion.presto.nostr.service;

import com.prosilion.presto.web.model.NostrAppUserDtoIF;
import com.prosilion.presto.web.service.AppUserDtoService;

import java.util.List;

public interface NostrAppUserDtoService<T extends NostrAppUserDtoIF> extends AppUserDtoService<T> {
  List<T> getAllAppUsersAsDto();
}
