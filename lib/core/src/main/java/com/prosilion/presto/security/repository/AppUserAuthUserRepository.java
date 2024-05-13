package com.prosilion.presto.security.repository;

import com.prosilion.presto.security.entity.AppUserAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserAuthUserRepository extends JpaRepository<AppUserAuthUser, Long> {
  AppUserAuthUser getAppUserAuthUserByAuthUserName(String username);
}
