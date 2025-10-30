package com.cache.supercache.controller;

import java.util.List;

import com.cache.supercache.dto.AddressResponse;
import com.cache.supercache.dto.CustomerResponse;
import com.cache.supercache.dto.EmailResponse;
import com.cache.supercache.dto.LookupResponse;
import com.cache.supercache.dto.mapper.Mapper;
import com.cache.supercache.entity.CustomerEntity;
import com.cache.supercache.entity.EmailEntity;
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
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
  private final CustomerRepository customerRepository;


  @GetMapping("/{id}")
  public CustomerResponse getCustomer(@PathVariable Long id,
                                      @RequestParam("addr") boolean addr,
                                      @RequestParam("email") boolean email,
                                      @RequestParam("intrst") boolean intrst) {
    CustomerEntity customerEntity = customerRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Customer not found with id " + id));

    CustomerResponse.CustomerResponseBuilder customerBuilder = Mapper.toCustomerBuilder(customerEntity);

    if (addr) {
      List<AddressResponse> addressResponses = customerEntity.getAddresses().stream()
          .map(Mapper::toAddressResponse)
          .toList();
      customerBuilder.addresses(addressResponses);
    }

    if (email) {
      EmailEntity entityEmail = customerEntity.getEmail();
      EmailResponse emailResponse = Mapper.toEmailResponse(entityEmail);
      customerBuilder.email(emailResponse);
    }

    if (intrst) {
      List<LookupResponse> interestResponses = customerEntity.getInterests().stream()
          .map(at -> (new LookupResponse(at.getId(), at.getName(), at.getCode())))
          .toList();
      customerBuilder.interests(interestResponses);
    }

    return customerBuilder.build();
  }

  @PutMapping("/{id}")
  public boolean updateCustomerFirstName(@PathVariable Long id,
                                         @RequestParam("firstName") String firstName) {
    CustomerEntity customerEntity = customerRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Customer not found with id " + id));

    customerEntity.setFirstName(firstName);
    customerRepository.save(customerEntity);
    return true;
  }
}
