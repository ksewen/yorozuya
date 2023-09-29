package com.github.ksewen.yorozuya.sample.spring.data.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableFeignClients
@EnableRedisRepositories
public class SpringDataRedisApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringDataRedisApplication.class, args);
  }
}
