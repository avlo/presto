package com.prosilion.presto.web.service;

import com.prosilion.presto.security.entity.AppUserAuthUser;
import com.prosilion.presto.web.model.AppUserDto;
import com.prosilion.presto.web.model.AppUserDtoIF;

import java.util.List;

public interface AppUserDtoService {
  List<AppUserDtoIF> getAllAppUsersAsDto();
}
