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
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * @author ksewen
 * @date 30.08.2023 16:43
 */
public class NonHttpClientAutoConfigurationTest {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withConfiguration(
              AutoConfigurations.of(
                  HttpClientAutoConfiguration.class, OkHttp3ClientAutoConfiguration.class));

  @Test
  void httpClient() {
    this.contextRunner
        .withUserConfiguration(
            HttpClientAutoConfiguration.class, OkHttp3ClientAutoConfiguration.class)
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
}
