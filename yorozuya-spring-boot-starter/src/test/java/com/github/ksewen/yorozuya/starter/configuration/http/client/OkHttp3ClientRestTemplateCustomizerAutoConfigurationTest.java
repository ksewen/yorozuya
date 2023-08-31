package com.github.ksewen.yorozuya.starter.configuration.http.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.web.client.RestTemplateCustomizer;

/**
 * @author ksewen
 * @date 31.08.2023 17:44
 */
class OkHttp3ClientRestTemplateCustomizerAutoConfigurationTest {

  @Test
  void context() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class,
                OkHttp3ClientAutoConfiguration.class,
                HttpClientRestTemplateCustomizerAutoConfiguration.class,
                OkHttp3ClientRestTemplateCustomizerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(RestTemplateCustomizer.class);
              assertThat(context)
                  .getBean("okHttp3ClientRestTemplateCustomizer")
                  .isSameAs(context.getBean(RestTemplateCustomizer.class));

              assertThat(context).doesNotHaveBean("httpClientRestTemplateCustomizer");
            });
  }
}
