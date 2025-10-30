package com.cache.supercache.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class RedissonConfig {

  @Bean(destroyMethod = "shutdown")
  public RedissonClient redissonClient() throws Exception {
    Config config = new Config();
    //Config config = Config.fromYAML(getClass().getClassLoader().getResource("redisson.yml"));

    // Custom ObjectMapper to handle Java 8 time types (LocalDateTime etc.)
    setTimeModule(config);

    config.useSingleServer()
        .setAddress("redis://127.0.0.1:6379")
        .setPassword("123456")
        .setDatabase(0)
        .setConnectionMinimumIdleSize(10)
        .setConnectionPoolSize(64)
        .setIdleConnectionTimeout(10000)
        .setConnectTimeout(10000)
        .setTimeout(3000)
        .setRetryAttempts(3)
        //.setRetryInterval(1500)
        .setPingConnectionInterval(60000);

    config.setThreads(4);
    config.setNettyThreads(8);
    config.setLockWatchdogTimeout(30000);
    config.setUseScriptCache(true);

    return Redisson.create(config);
  }

  private void setTimeModule(Config config) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());

    Codec codec = new JsonJacksonCodec(mapper);
    config.setCodec(codec);
  }

}
