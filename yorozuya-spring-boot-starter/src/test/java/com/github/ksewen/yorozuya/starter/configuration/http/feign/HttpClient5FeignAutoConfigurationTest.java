package com.github.ksewen.yorozuya.starter.configuration.http.feign;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.starter.configuration.http.client.HttpClientAutoConfiguration;
import feign.hc5.ApacheHttp5Client;
import feign.okhttp.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * @author ksewen
 * @date 05.09.2023 16:46
 */
class HttpClient5FeignAutoConfigurationTest {

  @Test
  void feignClient() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class, HttpClient5FeignAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(ApacheHttp5Client.class);
              assertThat(context)
                  .getBean("feignClient")
                  .isSameAs(context.getBean(ApacheHttp5Client.class));

              assertThat(context).doesNotHaveBean(OkHttpClient.class);
            });
  }
}
