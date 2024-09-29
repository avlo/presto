package com.prosilion.presto.web.model;

public interface AppUserDtoIF {
  Long getId();
  void setId(Long id);

  String getUsername();
  void setUsername(String username);

  String getPassword();
  void setPassword(String password);
}