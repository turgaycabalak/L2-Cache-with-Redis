package com.cache.supercache.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "addresses")
@Cacheable
@Cache(region = "addresses", usage = CacheConcurrencyStrategy.READ_WRITE)
public class AddressEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private CustomerEntity customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_type_code", referencedColumnName = "code", nullable = false)
  @Cache(region = "address_types", usage = CacheConcurrencyStrategy.READ_WRITE)
  private AddressTypeEntity addressType;

  private String address;
}
