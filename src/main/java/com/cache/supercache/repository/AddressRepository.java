package com.cache.supercache.repository;

import java.util.List;

import jakarta.persistence.QueryHint;

import com.cache.supercache.entity.AddressEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
  @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
  List<AddressEntity> findByCustomer_Id(Long customerId);
}
