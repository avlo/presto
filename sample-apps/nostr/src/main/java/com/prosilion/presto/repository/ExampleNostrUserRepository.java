package com.prosilion.presto.repository;

import com.prosilion.presto.model.entity.ExampleNostrUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleNostrUserRepository extends JpaRepository<ExampleNostrUser, Long> {
}
