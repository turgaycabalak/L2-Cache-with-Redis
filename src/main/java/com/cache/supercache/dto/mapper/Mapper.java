package com.cache.supercache.dto.mapper;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.cache.supercache.dto.AddressResponse;
import com.cache.supercache.dto.CustomerResponse;
import com.cache.supercache.dto.EmailResponse;
import com.cache.supercache.dto.LookupResponse;
import com.cache.supercache.entity.AddressEntity;
import com.cache.supercache.entity.AddressTypeEntity;
import com.cache.supercache.entity.CustomerEntity;
import com.cache.supercache.entity.EmailEntity;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Mapper {

  public List<CustomerResponse.CustomerResponseBuilder> toCustomerBuilders(Collection<CustomerEntity> entities) {
    return entities.stream()
        .map(Mapper::toCustomerBuilder)
        .toList();
  }

  public CustomerResponse.CustomerResponseBuilder toCustomerBuilder(CustomerEntity customerEntity) {
    return CustomerResponse.builder()
        .id(customerEntity.getId())
        .createdAt(customerEntity.getCreatedAt())
        .updatedAt(customerEntity.getUpdatedAt())
        .firstName(customerEntity.getFirstName())
        .lastName(customerEntity.getLastName());
  }

  public EmailResponse toEmailResponse(EmailEntity emailEntity) {
    return EmailResponse.builder()
        .id(emailEntity.getId())
        .createdAt(emailEntity.getCreatedAt())
        .updatedAt(emailEntity.getUpdatedAt())
        .emailAddress(emailEntity.getEmailAddress())
        .description(emailEntity.getDescription())
        .build();
  }


  public List<AddressResponse> toAddressResponses(Collection<AddressEntity> entities) {
    return entities.stream()
        .map(Mapper::toAddressResponse)
        .sorted(Comparator.comparingLong(AddressResponse::id))
        .toList();
  }

  public AddressResponse toAddressResponse(AddressEntity entity) {
    AddressTypeEntity at = entity.getAddressType();
    return AddressResponse.builder()
        .id(entity.getId())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .addressType(new LookupResponse(at.getId(), at.getName(), at.getCode()))
        .address(entity.getAddress())
        .build();
  }
}
