package com.github.ksewen.yorozuya.starter.configuration.http.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.web.client.RestTemplateCustomizer;

/**
 * @author ksewen
 * @date 31.08.2023 17:43
 */
class HttpClientRestTemplateCustomizerAutoConfigurationTest {

  @Test
  void context() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class,
                RestTemplateAutoConfiguration.HttpClientRestTemplateCustomizerAutoConfiguration
                    .class))
        .withPropertyValues("common.http.client.hc5.enabled=true")
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(RestTemplateCustomizer.class);
              assertThat(context)
                  .getBean("httpClientRestTemplateCustomizer")
                  .isSameAs(context.getBean(RestTemplateCustomizer.class));

              assertThat(context).doesNotHaveBean("okHttp3ClientRestTemplateCustomizer");
            });
  }
}
