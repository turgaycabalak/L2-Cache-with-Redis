package com.cache.supercache.controller;

import java.util.List;

import com.cache.supercache.dto.AddressResponse;
import com.cache.supercache.dto.CustomerResponse;
import com.cache.supercache.dto.EmailResponse;
import com.cache.supercache.dto.LookupResponse;
import com.cache.supercache.entity.AddressEntity;
import com.cache.supercache.entity.AddressTypeEntity;
import com.cache.supercache.entity.CustomerEntity;
import com.cache.supercache.entity.EmailEntity;
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
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
  private final CustomerRepository customerRepository;
  //private final EmailRepository emailRepository;
  private final AddressRepository addressRepository;


  @PutMapping("/{id}")
  public boolean updateCustomerFirstName(@PathVariable Long id,
                                         @RequestParam("firstName") String firstName) {
    CustomerEntity customerEntity = customerRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Customer not found with id " + id));

    customerEntity.setFirstName(firstName);
    customerRepository.save(customerEntity);
    return true;
  }

  @PutMapping("/address/{id}")
  public boolean updateAddress(@PathVariable Long id,
                               @RequestParam("address") String address) {
    AddressEntity addressEntity = addressRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Address not found with id " + id));

    addressEntity.setAddress(address);
    addressRepository.save(addressEntity);
    return true;
  }

  @GetMapping("/{id}")
  public CustomerResponse getCustomer(@PathVariable Long id,
                                      @RequestParam("addr") boolean addr,
                                      @RequestParam("email") boolean email,
                                      @RequestParam("intrst") boolean intrst) {
    CustomerEntity customerEntity = customerRepository.findById(id)
//    CustomerEntity customerEntity = customerRepository.findByIdWithDetails(id)
        .orElseThrow(() -> new IllegalArgumentException("Customer not found with id " + id));

    CustomerResponse.CustomerResponseBuilder customerBuilder = toCustomerResponse(customerEntity);

    if (addr) {
      List<AddressResponse> addressResponses = customerEntity.getAddresses().stream()
          .map(this::toAddressResponse)
          .toList();
      customerBuilder.addresses(addressResponses);
    }

    if (email) {
      EmailEntity entityEmail = customerEntity.getEmail();
      EmailResponse emailResponse = toEmailResponse(entityEmail);
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

//  @GetMapping("/email/{id}")
//  public EmailResponse getEmail(@PathVariable Long id) {
//    EmailEntity emailEntity = emailRepository.findById(id)
//        .orElseThrow(() -> new IllegalArgumentException("Email not found with id " + id));
//
//    return EmailResponse.builder()
//        .id(emailEntity.getId())
//        .createdAt(emailEntity.getCreatedAt())
//        .updatedAt(emailEntity.getUpdatedAt())
//        .emailAddress(emailEntity.getEmailAddress())
//        .description(emailEntity.getDescription())
//        .build();
//  }

//  @GetMapping("/addresses/{customerId}")
//  public List<AddressResponse> getAddressesByCustomer(@PathVariable Long customerId) {
//    //List<AddressEntity> addressEntities = addressRepository.findByCustomer_Id(customerId);
//    List<AddressEntity> addressEntities = addressRepository.findByCustomerIdWithAddressType(customerId);
//
//    return addressEntities.stream()
//        .map(this::toAddressResponse)
//        .toList();
//  }

  /// ///
  //private CustomerResponse toCustomerResponse(CustomerEntity customerEntity) {
  private CustomerResponse.CustomerResponseBuilder toCustomerResponse(CustomerEntity customerEntity) {
    //EmailEntity emailEntity = customerEntity.getEmail();
    //EmailResponse email = toEmailResponse(emailEntity);

    return CustomerResponse.builder()
        .id(customerEntity.getId())
        .createdAt(customerEntity.getCreatedAt())
        .updatedAt(customerEntity.getUpdatedAt())
        .firstName(customerEntity.getFirstName())
        .lastName(customerEntity.getLastName());


    //return CustomerResponse.builder()
    //    .id(customerEntity.getId())
    //    .createdAt(customerEntity.getCreatedAt())
    //    .updatedAt(customerEntity.getUpdatedAt())
    //    .firstName(customerEntity.getFirstName())
    //    .lastName(customerEntity.getLastName())
    //    //.email(email)
    //    .build();
  }

  private CustomerResponse toCustomerWithAddressesResponse(CustomerEntity customerEntity) {
    //EmailEntity emailEntity = customerEntity.getEmail();
    //EmailResponse email = toEmailResponse(emailEntity);
    List<AddressResponse> addressResponses = customerEntity.getAddresses().stream()
        .map(this::toAddressResponse)
        .toList();

    return CustomerResponse.builder()
        .id(customerEntity.getId())
        .createdAt(customerEntity.getCreatedAt())
        .updatedAt(customerEntity.getUpdatedAt())
        .firstName(customerEntity.getFirstName())
        .lastName(customerEntity.getLastName())
        //.email(email)
        .addresses(addressResponses)
        .build();
  }

  private EmailResponse toEmailResponse(EmailEntity emailEntity) {
    return EmailResponse.builder()
        .id(emailEntity.getId())
        .createdAt(emailEntity.getCreatedAt())
        .updatedAt(emailEntity.getUpdatedAt())
        .emailAddress(emailEntity.getEmailAddress())
        .description(emailEntity.getDescription())
        .build();
  }

  private AddressResponse toAddressResponse(AddressEntity address) {
    AddressTypeEntity at = address.getAddressType();
    return AddressResponse.builder()
        .id(address.getId())
        .createdAt(address.getCreatedAt())
        .updatedAt(address.getUpdatedAt())
        .addressType(new LookupResponse(at.getId(), at.getName(), at.getCode()))
        .address(address.getAddress())
        .build();
  }
}
