package com.github.ksewen.yorozuya.starter.configuration.http.feign;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.github.ksewen.yorozuya.starter.configuration.http.client.HttpClientAutoConfiguration;
import com.github.ksewen.yorozuya.starter.configuration.http.client.OkHttp3ClientAutoConfiguration;
import feign.hc5.ApacheHttp5Client;
import feign.okhttp.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * @author ksewen
 * @date 05.09.2023 16:46
 */
class OkHttp3FeignAutoConfigurationTest {

  @Test
  void feignClient() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class,
                OkHttp3ClientAutoConfiguration.class,
                HttpClient5FeignAutoConfiguration.class,
                OkHttp3FeignAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(OkHttpClient.class);
              assertThat(context)
                  .getBean("feignClient")
                  .isSameAs(context.getBean(OkHttpClient.class));

              assertThat(context).doesNotHaveBean(ApacheHttp5Client.class);
            });
  }
}
