package com.github.ksewen.yorozuya.starter.configuration.http.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.client.RestClient;

/**
 * @author ksewen
 * @date 03.01.2024 12:26
 */
public class RestClientBuilderAutoConfigurationTests {

  @Test
  void defaultBuilder() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class,
                RestClientAutoConfiguration.RestClientBuilderAutoConfiguration.class))
        .withPropertyValues(
            "common.http.client.hc5.enabled=true", "common.rest.client.default.enabled=true")
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(RestClient.Builder.class);
              assertThat(context)
                  .getBean("restClientBuilder")
                  .isSameAs(context.getBean(RestClient.Builder.class));
            });
  }

  @Test
  void loadBalancerBuilder() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class,
                RestClientAutoConfiguration.RestClientBuilderAutoConfiguration.class))
        .withPropertyValues(
            "common.http.client.hc5.enabled=true", "common.rest.client.loadbalancer.enabled=true")
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(RestClient.Builder.class);
              assertThat(context)
                  .getBean("loadBalancerRestClientBuilder")
                  .isSameAs(context.getBean(RestClient.Builder.class));
            });
  }

  @Test
  void builder() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class,
                RestClientAutoConfiguration.RestClientBuilderAutoConfiguration.class))
        .withPropertyValues(
            "common.http.client.hc5.enabled=true",
            "common.rest.client.default.enabled=true",
            "common.rest.client.loadbalancer.enabled=true")
        .run(
            (context) -> {
              assertThat(context).hasBean("restClientBuilder");
              assertThat(context).hasBean("loadBalancerRestClientBuilder");
            });
  }
}
