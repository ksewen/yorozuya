package com.github.ksewen.yorozuya.starter.configuration.http.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ksewen
 * @date 30.08.2023 18:04
 */
@SpringBootTest(
    classes = HttpClientAutoConfiguration.class,
    properties = {
      "common.http.client.hc5.enabled=true",
      "common.http.client.hc5.connect-timeout=PT10S",
      "common.http.client.hc5.socket-timeout=50S",
      "common.http.client.hc5.max-idle-time=PT100S",
      "common.http.client.hc5.connection-max-total=10",
      "common.http.client.hc5.connection-default-max-per-route=5",
      "common.http.client.hc5.validate-after-inactivity=PT20S",
      "common.http.client.hc5.time-to-live=PT1H",
      "common.http.client.hc5.tcp-no-delay=false",
      "common.http.client.hc5.max-retries=5",
      "common.http.client.hc5.default-retry-interval=PT5S"
    })
class HttpClientPropertiesTest {

  @Autowired private HttpClientProperties httpClientProperties;

  @Test
  void getConnectTimeout() {
    assertThat(this.httpClientProperties.getConnectTimeout()).isEqualTo(Duration.ofSeconds(10));
  }

  @Test
  void getSocketTimeout() {
    assertThat(this.httpClientProperties.getSocketTimeout()).isEqualTo(Duration.ofSeconds(50));
  }

  @Test
  void getMaxIdleTime() {
    assertThat(this.httpClientProperties.getMaxIdleTime()).isEqualTo(Duration.ofSeconds(100));
  }

  @Test
  void getConnectionMaxTotal() {
    assertThat(this.httpClientProperties.getConnectionMaxTotal()).isEqualTo(10);
  }

  @Test
  void getConnectionDefaultMaxPerRoute() {
    assertThat(this.httpClientProperties.getConnectionDefaultMaxPerRoute()).isEqualTo(5);
  }

  @Test
  void getValidateAfterInactivity() {
    assertThat(this.httpClientProperties.getValidateAfterInactivity())
        .isEqualTo(Duration.ofSeconds(20));
  }

  @Test
  void getTimeToLive() {
    assertThat(this.httpClientProperties.getTimeToLive()).isEqualTo(Duration.ofHours(1));
  }

  @Test
  void isTcpNoDelay() {
    assertThat(this.httpClientProperties.isTcpNoDelay()).isEqualTo(Boolean.FALSE);
  }

  @Test
  void getMaxRetries() {
    assertThat(this.httpClientProperties.getMaxRetries()).isEqualTo(5);
  }

  @Test
  void getDefaultRetryInterval() {
    assertThat(this.httpClientProperties.getDefaultRetryInterval())
        .isEqualTo(Duration.ofSeconds(5));
  }
}
