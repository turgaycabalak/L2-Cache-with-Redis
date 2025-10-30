//package com.cache.supercache.controller;
//
//import jakarta.persistence.EntityManagerFactory;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import org.hibernate.SessionFactory;
//import org.redisson.api.RKeys;
//import org.redisson.api.RedissonClient;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequestMapping("/cache")
//@RequiredArgsConstructor
//public class CacheController {
//  private final EntityManagerFactory entityManagerFactory;
//  private final RedissonClient redissonClient;
//
//
//  @PostMapping("/clear-all")
//  public ResponseEntity<String> clearAllCaches() {
//    // 1️⃣ Hibernate 2nd Level Cache'i temizle
//    SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
//    sessionFactory.getCache().evictAll();
//
//    // 2️⃣ Redis tarafındaki tüm Redisson Hibernate hash keylerini sil
//    RKeys rKeys = redissonClient.getKeys();
//    // deleteByPattern modern ve non-deprecated yöntem
//    long deletedCount = rKeys.deleteByPattern("redisson_hibernate:*");
//
//    return ResponseEntity.ok("Tüm cache'ler temizlendi! (" + deletedCount + " key silindi)");
//  }
//}
