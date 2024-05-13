package com.prosilion.presto.repository;

import com.prosilion.presto.model.entity.ExampleLdapUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleLdapUserRepository extends JpaRepository<ExampleLdapUser, Long> {
}
