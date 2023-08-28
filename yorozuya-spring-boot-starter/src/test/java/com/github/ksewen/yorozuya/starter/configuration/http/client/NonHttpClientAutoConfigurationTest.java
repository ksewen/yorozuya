package com.github.ksewen.yorozuya.starter.configuration.http.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.hc.client5.http.classic.HttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ksewen
 * @date 28.08.2023 17:34
 */
@SpringBootTest(
    classes = {HttpClientAutoConfiguration.class},
    properties = {"common.http.client.enable=false"})
class NonHttpClientAutoConfigurationTest {
  @Autowired(required = false)
  HttpClient httpClient;

  @Test
  void nullHttpClient() {
    assertThat(this.httpClient).isNull();
  }
}
