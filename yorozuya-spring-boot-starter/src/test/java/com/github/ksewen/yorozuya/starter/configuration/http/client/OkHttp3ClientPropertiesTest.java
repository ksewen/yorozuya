package com.github.ksewen.yorozuya.starter.configuration.http.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ksewen
 * @date 30.08.2023 18:33
 */
@SpringBootTest(
    classes = OkHttp3ClientAutoConfiguration.class,
    properties = {
      "common.ok.http.client.connect-timeout=10S",
      "common.ok.http.client.read-timeout=PT60S",
      "common.ok.http.client.write-timeout=PT90S",
      "common.ok.http.client.ping-interval=PT1M",
      "common.ok.http.client.connection-pool.max-idle-connections=50",
      "common.ok.http.client.connection-pool.keep-alive-duration=PT1H",
      "common.ok.http.client.dispatcher.core-pool-size=10",
      "common.ok.http.client.dispatcher.maximum-pool-size=20",
      "common.ok.http.client.dispatcher.max-requests=50",
      "common.ok.http.client.dispatcher.max-requests-per-host=5",
      "common.ok.http.client.dispatcher.keep-alive-time=90S",
      "common.ok.http.client.dispatcher.work-queue-size=500",
      "common.ok.http.client.dispatcher.thread-factory-name-format=test-thread-%d"
    })
class OkHttp3ClientPropertiesTest {

  @Autowired private OkHttp3ClientProperties okHttp3ClientProperties;

  @Test
  void getConnectTimeout() {
    assertThat(this.okHttp3ClientProperties.getConnectTimeout()).isEqualTo(Duration.ofSeconds(10));
  }

  @Test
  void getReadTimeout() {
    assertThat(this.okHttp3ClientProperties.getReadTimeout()).isEqualTo(Duration.ofSeconds(60));
  }

  @Test
  void getWriteTimeout() {
    assertThat(this.okHttp3ClientProperties.getWriteTimeout()).isEqualTo(Duration.ofSeconds(90));
  }

  @Test
  void getPingInterval() {
    assertThat(this.okHttp3ClientProperties.getPingInterval()).isEqualTo(Duration.ofMinutes(1));
  }

  @Test
  void getConnectionPool() {
    assertThat(this.okHttp3ClientProperties.getConnectionPool()).isNotNull();
    assertThat(this.okHttp3ClientProperties.getConnectionPool().getMaxIdleConnections())
        .isEqualTo(50);
    assertThat(this.okHttp3ClientProperties.getConnectionPool().getKeepAliveDuration())
        .isEqualTo(Duration.ofHours(1));
  }

  @Test
  void getDispatcher() {
    assertThat(this.okHttp3ClientProperties.getDispatcher()).isNotNull();
    assertThat(this.okHttp3ClientProperties.getDispatcher().getMaxRequests()).isEqualTo(50);
    assertThat(this.okHttp3ClientProperties.getDispatcher().getMaxRequestsPerHost()).isEqualTo(5);
    assertThat(this.okHttp3ClientProperties.getDispatcher().getCorePoolSize()).isEqualTo(10);
    assertThat(this.okHttp3ClientProperties.getDispatcher().getMaximumPoolSize()).isEqualTo(20);
    assertThat(this.okHttp3ClientProperties.getDispatcher().getKeepAliveTime())
        .isEqualTo(Duration.ofSeconds(90));
    assertThat(this.okHttp3ClientProperties.getDispatcher().getWorkQueueSize()).isEqualTo(500);
    assertThat(this.okHttp3ClientProperties.getDispatcher().getThreadFactoryNameFormat())
        .isEqualTo("test-thread-%d");
  }
}
