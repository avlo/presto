package com.prosilion.presto.security.entity;

public interface CustomizableAppUser <T extends AppUser> {
  Long getId();
  T getInstantiatedCustomAppUserType();
  T createNewCustomAppUserInstance();
}
