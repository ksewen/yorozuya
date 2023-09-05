package com.github.ksewen.yorozuya.sample.rest.client.httpclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RestClientHttpClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(RestClientHttpClientApplication.class, args);
  }
}
