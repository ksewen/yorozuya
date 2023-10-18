package com.github.ksewen.yorozuya.sample.micrometer.observation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, RedisAutoConfiguration.class})
@EnableFeignClients
@EnableAsync
public class MicrometerObservationApplication {

  public static void main(String[] args) {
    SpringApplication.run(MicrometerObservationApplication.class, args);
  }
}
