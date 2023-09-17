package com.github.ksewen.yorozuya.starter.configuration.http.client;

import static org.assertj.core.api.Assertions.assertThat;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
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
        .withPropertyValues("common.http.client.hc5.enabled=true")
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

              assertThat(context).doesNotHaveBean(OkHttpClient.class);
              assertThat(context).doesNotHaveBean(ConnectionPool.class);
              assertThat(context).doesNotHaveBean(Dispatcher.class);
            });
  }

  @Test
  void httpClient1() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class, OkHttp3ClientAutoConfiguration.class))
        .withPropertyValues(
            "common.ok.http.client.enabled=false", "common.http.client.hc5.enabled=true")
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(OkHttpClient.class);
              assertThat(context).doesNotHaveBean(ConnectionPool.class);
              assertThat(context).doesNotHaveBean(Dispatcher.class);

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
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class, OkHttp3ClientAutoConfiguration.class))
        .withPropertyValues("common.http.client.hc5.enabled=true")
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(OkHttpClient.class);
              assertThat(context)
                  .getBean("okHttpClient")
                  .isSameAs(context.getBean(OkHttpClient.class));

              assertThat(context).hasSingleBean(ConnectionPool.class);
              assertThat(context)
                  .getBean("okHttp3ConnectionPool")
                  .isSameAs(context.getBean(ConnectionPool.class));

              assertThat(context).hasSingleBean(Dispatcher.class);
              assertThat(context).getBean("dispatcher").isSameAs(context.getBean(Dispatcher.class));

              assertThat(context).doesNotHaveBean(HttpClient.class);
              assertThat(context).doesNotHaveBean(HttpClientConnectionManager.class);
              assertThat(context).doesNotHaveBean(HttpRequestRetryStrategy.class);
            });
  }

  @Test
  void nonHttpClient1() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(HttpClientAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(HttpClient.class);
              assertThat(context).doesNotHaveBean(HttpClientConnectionManager.class);
              assertThat(context).doesNotHaveBean(HttpRequestRetryStrategy.class);
            });
  }

  @Test
  void nonHttpClient2() {
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
