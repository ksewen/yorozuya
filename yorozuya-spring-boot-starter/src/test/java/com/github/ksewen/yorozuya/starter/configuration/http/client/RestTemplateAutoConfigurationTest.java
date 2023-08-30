package com.github.ksewen.yorozuya.starter.configuration.http.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.web.client.RestTemplate;

/**
 * @author ksewen
 * @date 30.08.2023 21:59
 */
@SpringBootTest(
    classes = {OkHttp3ClientAutoConfiguration.class, RestTemplateAutoConfiguration.class})
class RestTemplateAutoConfigurationTest {

  @Autowired private RestTemplate restTemplate;

  @Test
  void restTemplate() {
    assertThat(this.restTemplate).isNotNull();
  }

  @Test
  void contextWithOkHttpClient() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                RestTemplateAutoConfiguration.class,
                HttpClientAutoConfiguration.class,
                OkHttp3ClientAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(RestTemplate.class);
              assertThat(context)
                  .getBean("restTemplate")
                  .isSameAs(context.getBean(RestTemplate.class));

              assertThat(context).hasSingleBean(RestTemplateCustomizer.class);
              assertThat(context)
                  .getBean("okHttp3ClientRestTemplateCustomizer")
                  .isSameAs(context.getBean(RestTemplateCustomizer.class));

              assertThat(context).doesNotHaveBean("httpClientRestTemplateCustomizer");
            });
  }

  @Test
  void contextWithHttpClient() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                RestTemplateAutoConfiguration.class, HttpClientAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(RestTemplate.class);
              assertThat(context)
                  .getBean("restTemplate")
                  .isSameAs(context.getBean(RestTemplate.class));

              assertThat(context).hasSingleBean(RestTemplateCustomizer.class);
              assertThat(context)
                  .getBean("httpClientRestTemplateCustomizer")
                  .isSameAs(context.getBean(RestTemplateCustomizer.class));

              assertThat(context).doesNotHaveBean("okHttp3ClientRestTemplateCustomizer");
            });
  }
}
