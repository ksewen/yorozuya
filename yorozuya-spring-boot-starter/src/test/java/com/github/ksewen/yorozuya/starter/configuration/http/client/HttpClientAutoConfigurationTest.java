package com.github.ksewen.yorozuya.starter.configuration.http.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * @author ksewen
 * @date 28.08.2023 17:37
 */
class HttpClientAutoConfigurationTest {

  @Test
  void httpClient() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(HttpClientAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(HttpClient.class);
              assertThat(context).getBean("httpClient").isSameAs(context.getBean(HttpClient.class));

              assertThat(context).hasSingleBean(HttpClientConnectionManager.class);
              assertThat(context)
                  .getBean("connectionManager")
                  .isSameAs(context.getBean(HttpClientConnectionManager.class));

              assertThat(context).hasSingleBean(HttpRequestRetryStrategy.class);
              assertThat(context)
                  .getBean("retryStrategy")
                  .isSameAs(context.getBean(HttpRequestRetryStrategy.class));
            });
  }

  @Test
  void nonHttpClient() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(HttpClientAutoConfiguration.class))
        .withPropertyValues("common.http.client.hc5.enabled=false")
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(HttpClient.class);
              assertThat(context).doesNotHaveBean(HttpClientConnectionManager.class);
              assertThat(context).doesNotHaveBean(HttpRequestRetryStrategy.class);
            });
  }

  @Test
  void nonHttpClient1() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(HttpClientAutoConfiguration.class))
        .withPropertyValues("common.http.client.hc5.enabled=true")
        .withClassLoader(new FilteredClassLoader(HttpClient.class))
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(HttpClient.class);
              assertThat(context).doesNotHaveBean(HttpClientConnectionManager.class);
              assertThat(context).doesNotHaveBean(HttpRequestRetryStrategy.class);
            });
  }
}
