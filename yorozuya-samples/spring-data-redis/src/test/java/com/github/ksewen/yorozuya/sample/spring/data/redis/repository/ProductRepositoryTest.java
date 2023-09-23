package com.github.ksewen.yorozuya.sample.spring.data.redis.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.github.ksewen.yorozuya.sample.spring.data.redis.SpringDataRedisApplication;
import com.github.ksewen.yorozuya.sample.spring.data.redis.model.Product;
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
 * @date 22.09.2023 12:12
 */
@SpringBootTest(classes = SpringDataRedisApplication.class)
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductRepositoryTest extends RedisContainerTest {

  @Autowired private ProductRepository productRepository;

  @Order(1)
  @Test
  void testSet() {
    Product result =
        this.productRepository.save(Product.builder().id(1L).name("bmw").price(70000.00).build());
    assertThat(result).isNotNull().matches(r -> r.getId() == 1L);
  }

  @Order(2)
  @Test
  void testGet() {
    Product result = this.productRepository.findById(1L).orElse(null);
    assertThat(result).isNotNull();
  }

  @Order(2)
  @Test
  void testCount() {
    long count = this.productRepository.count();
    assertThat(count).isEqualTo(1L);
  }

  @Order(3)
  @Test
  void testDelete() {
    this.productRepository.deleteById(1L);
    long count = this.productRepository.count();
    assertThat(count).isEqualTo(0L);
  }
}
