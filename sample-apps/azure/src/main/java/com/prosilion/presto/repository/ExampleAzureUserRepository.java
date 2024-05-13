package com.prosilion.presto.repository;

import com.prosilion.presto.model.entity.ExampleAzureUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleAzureUserRepository extends JpaRepository<ExampleAzureUser, Long> {

//	@Query("SELECT u FROM ExampleAzureUser u where u.id = :id")
//	public Optional<ExampleAzureUser> findById(@Param("id") Long id);
}
