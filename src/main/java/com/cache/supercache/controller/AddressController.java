package com.cache.supercache.controller;

import java.util.List;

import com.cache.supercache.dto.AddressResponse;
import com.cache.supercache.dto.mapper.Mapper;
import com.cache.supercache.entity.AddressEntity;
import com.cache.supercache.repository.AddressRepository;

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


  @GetMapping("/by-customer/{customerId}")
  public List<AddressResponse> getAddressesByCustomer(@PathVariable Long customerId) {
    List<AddressEntity> addressEntities = addressRepository.findByCustomer_Id(customerId);

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
