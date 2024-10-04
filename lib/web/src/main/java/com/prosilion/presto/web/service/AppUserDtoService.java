package com.prosilion.presto.web.service;

import com.prosilion.presto.web.model.AppUserDtoIF;

import java.util.List;

public interface AppUserDtoService<T extends AppUserDtoIF> {
  List<T> getAllAppUsersAsDto();
}
