package com.cache.supercache.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.persistence.EntityManagerFactory;

import com.cache.supercache.entity.AddressEntity;
import com.cache.supercache.entity.CustomerEntity;
import com.cache.supercache.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

import org.hibernate.Cache;
import org.hibernate.SessionFactory;
import org.hibernate.stat.CacheRegionStatistics;
import org.hibernate.stat.Statistics;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class CacheTestController {
  private final CustomerRepository customerRepository;
  private final EntityManagerFactory entityManagerFactory;

  @GetMapping("/cache-stats")
  public ResponseEntity<Map<String, Object>> getCacheStats() {
    SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    Cache cache = sessionFactory.getCache();

    Map<String, Object> stats = new HashMap<>();
    stats.put("customerCache", cache.containsEntity(CustomerEntity.class, 1L));
    //stats.put("emailCache", cache.containsEntity(EmailEntity.class, 1L));
    stats.put("addressCache", cache.containsEntity(AddressEntity.class, 1L));
    //stats.put("addressTypeCache", cache.containsEntity(AddressTypeEntity.class, 1L));
    //stats.put("interestCache", cache.containsEntity(InterestEntity.class, 1L));

    return ResponseEntity.ok(stats);
  }

  @GetMapping("/performance/{id}")
  public ResponseEntity<Map<String, Object>> testPerformance(@PathVariable Long id) {
    Map<String, Object> result = new LinkedHashMap<>();

    // İlk çağrı (cache miss)
    long startTime1 = System.currentTimeMillis();
    customerRepository.findById(id);
    long endTime1 = System.currentTimeMillis();

    // İkinci çağrı (cache hit)
    long startTime2 = System.currentTimeMillis();
    customerRepository.findById(id);
    long endTime2 = System.currentTimeMillis();

    result.put("firstCallMs", endTime1 - startTime1);
    result.put("secondCallMs", endTime2 - startTime2);
    result.put("improvement", ((endTime1 - startTime1) - (endTime2 - startTime2)) + "ms");
    result.put("improvementPercentage",
        String.format("%.2f%%",
            (1 - (double) (endTime2 - startTime2) / (endTime1 - startTime1)) * 100));

    return ResponseEntity.ok(result);
  }

  @GetMapping("/cache-regions")
  public ResponseEntity<Map<String, Object>> getCacheRegions() {
    SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    Statistics statistics = sessionFactory.getStatistics();

    Map<String, Object> regions = new HashMap<>();

    // Cache region istatistiklerini al
    String[] regionNames = statistics.getSecondLevelCacheRegionNames();
    for (String regionName : regionNames) {
      CacheRegionStatistics regionStats = statistics.getDomainDataRegionStatistics(regionName);
      Map<String, Object> regionInfo = new HashMap<>();
      regionInfo.put("putCount", regionStats.getPutCount());
      regionInfo.put("hitCount", regionStats.getHitCount());
      regionInfo.put("missCount", regionStats.getMissCount());
      regionInfo.put("elementCountInMemory", regionStats.getElementCountInMemory());
      regions.put(regionName, regionInfo);
    }

    return ResponseEntity.ok(regions);
  }

  @GetMapping("/hibernate-stats")
  public ResponseEntity<Map<String, Object>> getHibernateStatistics() {
    SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    Statistics statistics = sessionFactory.getStatistics();

    Map<String, Object> stats = new HashMap<>();
    stats.put("secondLevelCacheHitCount", statistics.getSecondLevelCacheHitCount());
    stats.put("secondLevelCacheMissCount", statistics.getSecondLevelCacheMissCount());
    stats.put("secondLevelCachePutCount", statistics.getSecondLevelCachePutCount());
    stats.put("queryCacheHitCount", statistics.getQueryCacheHitCount());
    stats.put("queryCacheMissCount", statistics.getQueryCacheMissCount());
    stats.put("queryCachePutCount", statistics.getQueryCachePutCount());
    stats.put("entityFetchCount", statistics.getEntityFetchCount());
    stats.put("queryExecutionCount", statistics.getQueryExecutionCount());

    return ResponseEntity.ok(stats);
  }

  @GetMapping("/clear-and-test/{id}")
  public ResponseEntity<Map<String, Object>> clearAndTest(@PathVariable Long id) {
    Map<String, Object> result = new HashMap<>();

    // Önce cache'i temizle
    SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    sessionFactory.getCache().evictAll();

    // Test
    long startTime = System.currentTimeMillis();
    customerRepository.findById(id);
    long endTime = System.currentTimeMillis();

    result.put("afterClearMs", endTime - startTime);
    result.put("cacheCleared", true);

    return ResponseEntity.ok(result);
  }
}
