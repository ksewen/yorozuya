package com.github.ksewen.yorozuya.sample.rest.client.okhttp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, RedisAutoConfiguration.class})
@EnableFeignClients
public class RestClientOkhttpApplication {

  public static void main(String[] args) {
    SpringApplication.run(RestClientOkhttpApplication.class, args);
  }
}
