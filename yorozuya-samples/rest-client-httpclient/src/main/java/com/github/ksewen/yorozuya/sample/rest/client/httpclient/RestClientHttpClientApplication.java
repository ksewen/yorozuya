package com.github.ksewen.yorozuya.sample.rest.client.httpclient;

import com.github.ksewen.yorozuya.starter.configuration.http.client.OkHttp3ClientAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = OkHttp3ClientAutoConfiguration.class)
public class RestClientHttpClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(RestClientHttpClientApplication.class, args);
  }
}
