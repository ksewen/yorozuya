package com.github.ksewen.yorozuya.sample.spring.data.redis.repository;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @author ksewen
 * @date 22.09.2023 17:49
 */
public class RedisContainerTest {

  static GenericContainer<?> redis =
      new GenericContainer<>(DockerImageName.parse("redis:7.2.1")).withExposedPorts(6379);

  @DynamicPropertySource
  static void redisProperties(DynamicPropertyRegistry registry) {
    redis.start();
    registry.add("spring.data.redis.host", redis::getHost);
    registry.add("spring.data.redis.port", redis::getFirstMappedPort);
  }
}
