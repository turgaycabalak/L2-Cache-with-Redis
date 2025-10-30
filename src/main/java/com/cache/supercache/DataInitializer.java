//package com.cache.supercache;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Random;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//import com.cache.supercache.entity.AddressEntity;
//import com.cache.supercache.entity.AddressTypeEntity;
//import com.cache.supercache.entity.CustomerEntity;
//import com.cache.supercache.entity.EmailEntity;
//import com.cache.supercache.entity.InterestEntity;
//import com.cache.supercache.repository.AddressTypeRepository;
//import com.cache.supercache.repository.CustomerRepository;
//import com.cache.supercache.repository.InterestRepository;
//import com.github.javafaker.Faker;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class DataInitializer implements CommandLineRunner {
//
//  private final CustomerRepository customerRepository;
//  private final AddressTypeRepository addressTypeRepository;
//  private final InterestRepository interestRepository;
//
//  private final Faker faker = new Faker();
//  private final Random random = new Random();
//
//  @Override
//  @Transactional
//  public void run(String... args) {
//    initializeLookupTables();
//
//    if (customerRepository.count() > 0) {
//      log.warn("Customer tablosu dolu, veri oluşturulmadı.");
//      return;
//    }
//
//    List<AddressTypeEntity> addressTypes = addressTypeRepository.findAll();
//    List<InterestEntity> interests = interestRepository.findAll();
//    int customerCount = 100;
//
//    List<CustomerEntity> customers = IntStream.range(0, customerCount)
//        .mapToObj(i -> createCustomer(addressTypes, interests))
//        .toList();
//
//    customerRepository.saveAll(customers);
//
//    log.info("{} fake customer oluşturuldu!", customerCount);
//  }
//
//  private void initializeLookupTables() {
//    if (addressTypeRepository.count() == 0) {
//      List<AddressTypeEntity> types = List.of(
//          AddressTypeEntity.builder().code("HOME").name("Ev").build(),
//          AddressTypeEntity.builder().code("WORK").name("İş").build()
//      );
//      addressTypeRepository.saveAll(types);
//    }
//
//    if (interestRepository.count() == 0) {
//      List<InterestEntity> interests = List.of(
//          InterestEntity.builder().code("TECH").name("Teknoloji").build(),
//          InterestEntity.builder().code("SPORTS").name("Spor").build(),
//          InterestEntity.builder().code("MUSIC").name("Müzik").build()
//      );
//      interestRepository.saveAll(interests);
//    }
//  }
//
//  private CustomerEntity createCustomer(List<AddressTypeEntity> addressTypes, List<InterestEntity> interests) {
//    LocalDateTime createdAt = faker.date().past(1000, TimeUnit.DAYS)
//        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//    LocalDateTime updatedAt = faker.date().past(100, TimeUnit.DAYS)
//        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//
//    CustomerEntity customer = CustomerEntity.builder()
//        .firstName(faker.name().firstName())
//        .lastName(faker.name().lastName())
//        .createdAt(createdAt)
//        .updatedAt(updatedAt)
//        .build();
//
//    // Email
//    EmailEntity email = EmailEntity.builder()
//        .emailAddress(faker.internet().emailAddress())
//        .description(faker.lorem().sentence(3))
//        .createdAt(createdAt)
//        .updatedAt(updatedAt)
//        .customer(customer)
//        .build();
//    customer.setEmail(email);
//
//    // Addresses
//    Set<AddressEntity> customerAddresses = IntStream.range(0, random.nextInt(2) + 1)
//        .mapToObj(j -> AddressEntity.builder()
//            .customer(customer)
//            .address(faker.address().fullAddress())
//            .addressType(addressTypes.get(random.nextInt(addressTypes.size())))
//            .createdAt(createdAt)
//            .updatedAt(updatedAt)
//            .build())
//        .collect(Collectors.toSet());
//    customer.setAddresses(customerAddresses);
//
//    // Interests
//    HashSet<InterestEntity> customerInterests = new HashSet<>();
//    int interestCount = random.nextInt(interests.size()) + 1;
//    while (customerInterests.size() < interestCount) {
//      customerInterests.add(interests.get(random.nextInt(interests.size())));
//    }
//    customer.setInterests(customerInterests);
//
//    return customer;
//  }
//}