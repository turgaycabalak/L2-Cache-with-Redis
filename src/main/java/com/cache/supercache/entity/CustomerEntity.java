package com.cache.supercache.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "customers")
@Cacheable
@Cache(region = "customers", usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustomerEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private String firstName;
  private String lastName;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "email", referencedColumnName = "emailAddress"/*, nullable = false*/, unique = true)
  private EmailEntity email;

  @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @Cache(region = "addresses", usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<AddressEntity> addresses;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "customer_interests_mapping",
      joinColumns = @JoinColumn(name = "customer_id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "interest_code", referencedColumnName = "code", nullable = false)
  )
  @Cache(region = "interests", usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<InterestEntity> interests;
}
