package com.github.ksewen.yorozuya.starter.configuration.http.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.hc.client5.http.classic.HttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ksewen
 * @date 28.08.2023 17:37
 */
@SpringBootTest(
    classes = {HttpClientAutoConfiguration.class},
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    properties = {"common.http.client.enable=true", "common.http.client.connectTimeout=10"})
class HttpClientAutoConfigurationTest {

  @Autowired private HttpClient httpClient;

  @Test
  void httpClient() {
    assertThat(this.httpClient).isNotNull();
  }
}
