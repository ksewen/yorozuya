package com.github.ksewen.yorozuya.starter.configuration.redis.template;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.starter.configuration.redis.GzipStringRedisTemplateAutoConfiguration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @author ksewen
 * @date 24.09.2023 14:15
 */
@SpringBootTest(
    classes = {GzipStringRedisTemplateAutoConfiguration.class, RedisAutoConfiguration.class},
    properties = {"spring.data.redis.template.gzip.enable=true"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GzipStringRedisTemplateTest {
  static GenericContainer<?> redis =
      new GenericContainer<>(DockerImageName.parse("redis:7.2.1")).withExposedPorts(6379);

  @DynamicPropertySource
  static void redisProperties(DynamicPropertyRegistry registry) {
    redis.start();
    registry.add("spring.data.redis.host", redis::getHost);
    registry.add("spring.data.redis.port", redis::getFirstMappedPort);
  }

  @Autowired private GzipStringRedisTemplate stringRedisTemplate;

  private final String KEY = "test";

  @Order(1)
  @Test
  void testSetAndGet() {
    final String value = "bmw";
    this.stringRedisTemplate.opsForValue().set(this.KEY, value);
    assertThat(this.stringRedisTemplate.hasKey(this.KEY)).isTrue();

    StringRedisTemplate redisTemplate = new StringRedisTemplate();
    redisTemplate.setConnectionFactory(this.stringRedisTemplate.getConnectionFactory());
    redisTemplate.afterPropertiesSet();
    String temp = redisTemplate.opsForValue().get(this.KEY);
    assertThat(temp).matches(s -> !value.equals(s));
  }

  @Order(2)
  @Test
  void testDelete() {
    this.stringRedisTemplate.delete(this.KEY);
    Boolean result = this.stringRedisTemplate.hasKey(this.KEY);
    assertThat(result).isFalse();
  }
}
