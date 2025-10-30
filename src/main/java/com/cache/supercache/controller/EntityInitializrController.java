package com.cache.supercache.controller;

import static com.cache.supercache.enums.CustomerInitializerTypeEnum.FULL;
import static com.cache.supercache.enums.CustomerInitializerTypeEnum.WITH_ADDRESS_TYPE;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.cache.supercache.entity.AddressEntity;
import com.cache.supercache.entity.AddressTypeEntity;
import com.cache.supercache.entity.CustomerEntity;
import com.cache.supercache.entity.EmailEntity;
import com.cache.supercache.entity.InterestEntity;
import com.cache.supercache.enums.CustomerInitializerTypeEnum;
import com.cache.supercache.repository.AddressTypeRepository;
import com.cache.supercache.repository.CustomerRepository;
import com.cache.supercache.repository.InterestRepository;
import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/initializr")
@RequiredArgsConstructor
public class EntityInitializrController {
  private final CustomerRepository customerRepository;
  private final AddressTypeRepository addressTypeRepository;
  private final InterestRepository interestRepository;

  private final Faker faker = new Faker();
  private final Random random = new Random();

  /**
   * @param mode createCustomer davranışını belirler:
   *             - basic: sadece customer + address
   *             - withAddressType: address + addressType
   *             - full: addressType + interest + email
   */
  @Transactional
  @GetMapping
  public void initialize(@RequestParam(defaultValue = "FULL") CustomerInitializerTypeEnum mode) {
    initializeLookupTables();

    List<AddressTypeEntity> addressTypes = addressTypeRepository.findAll();
    List<InterestEntity> interests = interestRepository.findAll();
    int customerCount = 100;

    List<CustomerEntity> customers = IntStream.range(0, customerCount)
        .mapToObj(i -> createCustomer(mode, addressTypes, interests))
        .toList();

    customerRepository.saveAll(customers);
    log.info("{} fake customer oluşturuldu! (mode = {})", customerCount, mode);
  }

  private void initializeLookupTables() {
    if (addressTypeRepository.count() == 0) {
      List<AddressTypeEntity> types = List.of(
          AddressTypeEntity.builder().code("HOME").name("Ev").build(),
          AddressTypeEntity.builder().code("WORK").name("İş").build()
      );
      addressTypeRepository.saveAll(types);
    }

    if (interestRepository.count() == 0) {
      List<InterestEntity> interests = List.of(
          InterestEntity.builder().code("TECH").name("Teknoloji").build(),
          InterestEntity.builder().code("SPORTS").name("Spor").build(),
          InterestEntity.builder().code("MUSIC").name("Müzik").build()
      );
      interestRepository.saveAll(interests);
    }
  }

  private CustomerEntity createCustomer(CustomerInitializerTypeEnum mode,
                                        List<AddressTypeEntity> addressTypes,
                                        List<InterestEntity> interests) {

    LocalDateTime createdAt = faker.date().past(1000, TimeUnit.DAYS)
        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    LocalDateTime updatedAt = faker.date().past(100, TimeUnit.DAYS)
        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

    // Customer temel bilgileri
    CustomerEntity customer = CustomerEntity.builder()
        .firstName(faker.name().firstName())
        .lastName(faker.name().lastName())
        .createdAt(createdAt)
        .updatedAt(updatedAt)
        .build();

    // Email sadece "full" modda oluşturulur
    if (FULL.equals(mode)) {
      EmailEntity email = EmailEntity.builder()
          .emailAddress(faker.internet().emailAddress())
          .description(faker.lorem().sentence(3))
          .createdAt(createdAt)
          .updatedAt(updatedAt)
          .customer(customer)
          .build();
      customer.setEmail(email);
    }

    // Address oluşturma (her modda var)
    Set<AddressEntity> addresses = IntStream.range(0, random.nextInt(5) + 1)
        .mapToObj(j -> {
          AddressEntity.AddressEntityBuilder builder = AddressEntity.builder()
              .customer(customer)
              .address(faker.address().fullAddress())
              .createdAt(createdAt)
              .updatedAt(updatedAt);

          // "withAddressType" veya "full" modlarında addressType ekle
          if (WITH_ADDRESS_TYPE.equals(mode) || FULL.equals(mode)) {
            builder.addressType(addressTypes.get(random.nextInt(addressTypes.size())));
          }

          return builder.build();
        })
        .collect(Collectors.toSet());
    customer.setAddresses(addresses);

    // Interests sadece "full" modda eklenir
    if (FULL.equals(mode)) {
      Set<InterestEntity> customerInterests = new HashSet<>();
      int interestCount = random.nextInt(interests.size()) + 1;
      while (customerInterests.size() < interestCount) {
        customerInterests.add(interests.get(random.nextInt(interests.size())));
      }
      customer.setInterests(customerInterests);
    }

    return customer;
  }
}
