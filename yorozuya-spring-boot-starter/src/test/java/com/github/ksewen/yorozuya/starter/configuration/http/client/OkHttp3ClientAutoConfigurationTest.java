package com.github.ksewen.yorozuya.starter.configuration.http.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
 * @date 30.08.2023 16:57
 */
class OkHttp3ClientAutoConfigurationTest {

  @Test
  void okHttpClient() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(OkHttp3ClientAutoConfiguration.class))
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
            });
  }

  @Test
  void okHttpClient1() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class, OkHttp3ClientAutoConfiguration.class))
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
  void okHttpClient2() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class, OkHttp3ClientAutoConfiguration.class))
        .withPropertyValues("common.http.client.enabled=true")
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
  void nonOkHttpClient() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(OkHttp3ClientAutoConfiguration.class))
        .withPropertyValues("common.ok.http.client.enabled=false")
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(OkHttpClient.class);
              assertThat(context).doesNotHaveBean(ConnectionPool.class);
              assertThat(context).doesNotHaveBean(Dispatcher.class);
            });
  }

  @Test
  void nonOkHttpClient1() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(OkHttp3ClientAutoConfiguration.class))
        .withClassLoader(new FilteredClassLoader(OkHttpClient.class))
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(OkHttpClient.class);
              assertThat(context).doesNotHaveBean(ConnectionPool.class);
              assertThat(context).doesNotHaveBean(Dispatcher.class);
            });
  }
}
