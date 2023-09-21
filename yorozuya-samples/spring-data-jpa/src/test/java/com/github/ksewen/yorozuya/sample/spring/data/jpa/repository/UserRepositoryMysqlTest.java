package com.github.ksewen.yorozuya.sample.spring.data.jpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.sample.spring.data.jpa.SpringDataJpaApplication;
import com.github.ksewen.yorozuya.sample.spring.data.jpa.domain.User;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

/**
 * @author ksewen
 * @date 21.09.2023 10:56
 */
@SpringBootTest(classes = SpringDataJpaApplication.class)
@ActiveProfiles("mysql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryMysqlTest {

  static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.1.0");

  @Autowired private UserRepository userRepository;

  @DynamicPropertySource
  static void mysqlProperties(DynamicPropertyRegistry registry) {
    mysql.start();
    registry.add("spring.datasource.url", mysql::getJdbcUrl);
    registry.add("spring.datasource.username", mysql::getUsername);
    registry.add("spring.datasource.password", mysql::getPassword);
  }

  @Order(1)
  @Test
  public void testSave() {
    User user =
        this.userRepository.saveAndFlush(
            User.builder().firstName("Marko").lastName("Reus").build());
    assertThat(user).hasFieldOrProperty("id");
  }

  @Order(2)
  @Test
  public void testFindAll() {
    List<User> result = this.userRepository.findAll();
    assertThat(result).isNotNull().hasSize(1);
  }

  @Order(3)
  @Test
  public void testFindAllByFirstName() {
    List<User> result = this.userRepository.findAllByFirstName("Marko");
    assertThat(result).isNotNull().hasSize(1);
  }
}
