package com.github.ksewen.yorozuya.starter.configuration.http.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.util.MockUtil.isMock;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * @author ksewen
 * @date 03.01.2024 12:32
 */
class RestClientAutoConfigurationTest {

  @Test
  void restClient() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                RestClientAutoConfiguration.class, MockRestClientBuilderAutoConfiguration.class))
        .withPropertyValues("common.rest.client.default.enabled=true")
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(RestClient.class);
              assertThat(context).getBean("restClient").isSameAs(context.getBean(RestClient.class));
              assertThat(isMock(context.getBean(RestClient.class))).isFalse();
            });
  }

  @Test
  void mockRestClient() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                RestClientAutoConfiguration.class,
                MockRestClientBuilderAutoConfiguration.class,
                MockRestClientAutoConfiguration.class))
        .withPropertyValues("common.rest.client.default.enabled=true")
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(RestClient.class);
              assertThat(context).getBean("restClient").isSameAs(context.getBean(RestClient.class));
              assertThat(isMock(context.getBean(RestClient.class))).isTrue();
            });
  }

  @Test
  void loadBalancerRestClient() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                RestClientAutoConfiguration.class, MockRestClientBuilderAutoConfiguration.class))
        .withPropertyValues("common.rest.client.loadbalancer.enabled=true")
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(RestClient.class);
              assertThat(context)
                  .getBean("loadBalancerRestClient")
                  .isSameAs(context.getBean(RestClient.class));
              assertThat(isMock(context.getBean(RestClient.class))).isFalse();
            });
  }

  @Test
  void mockLoadBalancerRestClient() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                RestClientAutoConfiguration.class,
                MockRestClientBuilderAutoConfiguration.class,
                MockLoadBalancerRestClientAutoConfiguration.class))
        .withPropertyValues("common.rest.client.loadbalancer.enabled=true")
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(RestClient.class);
              assertThat(context)
                  .getBean("loadBalancerRestClient")
                  .isSameAs(context.getBean(RestClient.class));
              assertThat(isMock(context.getBean(RestClient.class))).isTrue();
            });
  }

  @Test
  void context() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                RestClientAutoConfiguration.class, MockRestClientBuilderAutoConfiguration.class))
        .withPropertyValues(
            "common.rest.client.default.enabled=true",
            "common.rest.client.loadbalancer.enabled=true")
        .run(
            (context) -> {
              assertThat(context).hasBean("restClient");
              assertThat(context).hasBean("loadBalancerRestClient");
              assertThat(isMock(context.getBean("restClient"))).isFalse();
              assertThat(isMock(context.getBean("loadBalancerRestClient"))).isFalse();
            });
  }

  @Test
  void mockContext() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                RestClientAutoConfiguration.class,
                MockRestClientBuilderAutoConfiguration.class,
                MockRestClientAutoConfiguration.class,
                MockLoadBalancerRestClientAutoConfiguration.class))
        .withPropertyValues(
            "common.rest.client.default.enabled=true",
            "common.rest.client.loadbalancer.enabled=true")
        .run(
            (context) -> {
              assertThat(context).hasBean("restClient");
              assertThat(context).hasBean("loadBalancerRestClient");
              assertThat(isMock(context.getBean("restClient"))).isTrue();
              assertThat(isMock(context.getBean("loadBalancerRestClient"))).isTrue();
            });
  }

  @Configuration
  @AutoConfigureBefore(RestClientAutoConfiguration.class)
  static class MockRestClientAutoConfiguration {

    @Bean
    public RestClient restClient() {
      return mock(RestClient.class);
    }
  }

  @Configuration
  @AutoConfigureBefore(RestClientAutoConfiguration.class)
  static class MockLoadBalancerRestClientAutoConfiguration {

    @Bean
    public RestClient loadBalancerRestClient() {
      return mock(RestClient.class);
    }
  }

  //  @Configuration
  @AutoConfigureBefore(RestClientAutoConfiguration.class)
  static class MockRestClientBuilderAutoConfiguration {

    @Bean
    public RestClient.Builder restClientBuilder() {
      return RestClient.builder();
    }

    @Bean
    public RestClient.Builder loadBalancerRestClientBuilder() {
      return RestClient.builder();
    }
  }
}
