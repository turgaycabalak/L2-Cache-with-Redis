package com.cache.supercache.repository;

import java.util.List;

import jakarta.persistence.QueryHint;

import com.cache.supercache.entity.AddressEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
  List<AddressEntity> findByCustomer_Id(Long customerId);

  //@Query("SELECT a FROM AddressEntity a JOIN FETCH a.addressType WHERE a.customer.id = :customerId")
  //@QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
  //List<AddressEntity> findByCustomerIdWithAddressType(@Param("customerId") Long customerId);
}
