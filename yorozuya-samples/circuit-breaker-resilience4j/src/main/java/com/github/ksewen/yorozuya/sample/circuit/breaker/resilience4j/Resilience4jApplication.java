package com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, RedisAutoConfiguration.class})
@EnableFeignClients
public class Resilience4jApplication {

  public static void main(String[] args) {
    SpringApplication.run(Resilience4jApplication.class, args);
  }
}
