package com.cache.supercache.repository;

import java.util.Optional;

import jakarta.persistence.QueryHint;

import com.cache.supercache.entity.CustomerEntity;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

  //@EntityGraph(attributePaths = {"email", "addresses", "interests"})
  //@Query("SELECT c FROM CustomerEntity c WHERE c.id = :id")
  //Optional<CustomerEntity> findByIdWithDetails(@Param("id") Long id);

  //@QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
  //@Override
  //Optional<CustomerEntity> findById(Long id);
}
