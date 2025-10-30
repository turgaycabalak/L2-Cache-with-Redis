package com.cache.supercache.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.cache.supercache.dto.AddressResponse;
import com.cache.supercache.dto.mapper.Mapper;
import com.cache.supercache.entity.AddressEntity;
import com.cache.supercache.entity.CustomerEntity;
import com.cache.supercache.repository.AddressRepository;
import com.cache.supercache.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {
  private final AddressRepository addressRepository;
  private final CustomerRepository customerRepository;


  @GetMapping("/by-customer/{customerId}/query-cache")
  public List<AddressResponse> getAddressesByCustomerQuery(@PathVariable Long customerId) {
    List<AddressEntity> addressEntities = addressRepository.findByCustomer_Id(customerId);

    return addressEntities.stream()
        .map(Mapper::toAddressResponse)
        .toList();
  }

  @GetMapping("/by-customer/{customerId}/collection-cache")
  public List<AddressResponse> getAddressesByCustomerCollection(@PathVariable Long customerId) {
    CustomerEntity customerEntity = customerRepository.findById(customerId)
        .orElseThrow(() -> new IllegalArgumentException("Customer not found with id " + customerId));

    Set<AddressEntity> addressEntities = Optional.ofNullable(customerEntity.getAddresses())
        .orElse(Set.of());

    return addressEntities.stream()
        .map(Mapper::toAddressResponse)
        .toList();
  }

  @GetMapping("/{id}")
  public AddressResponse getAddress(@PathVariable Long id) {
    AddressEntity addressEntity = getAddressEntity(id);

    return Mapper.toAddressResponse(addressEntity);
  }

  @PutMapping("/{id}")
  public boolean updateAddress(@PathVariable Long id, @RequestParam("address") String address) {
    AddressEntity addressEntity = getAddressEntity(id);

    addressEntity.setAddress(address);
    addressRepository.save(addressEntity);
    return true;
  }


  private AddressEntity getAddressEntity(Long id) {
    return addressRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Address not found with id " + id));
  }
}
