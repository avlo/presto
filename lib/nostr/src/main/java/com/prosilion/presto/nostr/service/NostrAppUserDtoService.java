package com.prosilion.presto.nostr.service;

import com.prosilion.presto.web.model.AppUserDtoIF;
import com.prosilion.presto.web.service.AppUserDtoService;

import java.util.List;

public interface NostrAppUserDtoService extends AppUserDtoService {
  List<AppUserDtoIF> getAllAppUsersAsDto();
}
