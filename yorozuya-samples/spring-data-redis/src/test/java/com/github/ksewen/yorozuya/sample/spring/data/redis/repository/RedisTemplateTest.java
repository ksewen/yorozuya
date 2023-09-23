package com.github.ksewen.yorozuya.sample.spring.data.redis.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ksewen.yorozuya.sample.spring.data.redis.SpringDataRedisApplication;
import com.github.ksewen.yorozuya.sample.spring.data.redis.model.Product;
import com.github.ksewen.yorozuya.starter.configuration.redis.template.GzipStringRedisTemplate;
import com.github.ksewen.yorozuya.starter.configuration.redis.template.Jackson2JsonRedisTemplate;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ksewen
 * @date 22.09.2023 17:48
 */
@SpringBootTest(
    classes = SpringDataRedisApplication.class,
    properties = {"spring.data.redis.template.gzip.enable=true"})
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RedisTemplateTest extends RedisContainerTest {

  @Autowired private Jackson2JsonRedisTemplate redisTemplate;

  @Autowired private GzipStringRedisTemplate stringRedisTemplate;

  @Autowired private ObjectMapper objectMapper;

  private final String KEY = "test";

  @Order(1)
  @Test
  void testRedisTemplate() {
    final String name = "bmw";
    final double price = 90000.00;
    this.redisTemplate
        .opsForValue()
        .set(this.KEY, Product.builder().id(1L).name(name).price(price).build());
    assertThat(this.redisTemplate.hasKey(this.KEY)).isTrue();
    Product result =
        this.objectMapper.convertValue(
            this.redisTemplate.opsForValue().get(this.KEY), Product.class);
    assertThat(result)
        .isNotNull()
        .matches(p -> p.getId() == 1L)
        .matches(p -> name.equals(p.getName()))
        .matches(p -> p.getPrice() == price);
  }

  @Order(1)
  @Test
  void testStringRedisTemplate() {
    final String value = "bmw";
    this.stringRedisTemplate.opsForValue().set(this.KEY, value);
    String result = this.stringRedisTemplate.opsForValue().get(this.KEY);
    assertThat(result).isNotNull().isEqualTo(value);
  }
}
