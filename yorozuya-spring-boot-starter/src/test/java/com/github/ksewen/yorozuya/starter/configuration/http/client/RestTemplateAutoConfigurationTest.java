package com.github.ksewen.yorozuya.starter.configuration.http.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;

/**
 * @author ksewen
 * @date 08.09.2023 14:32
 */
class RestTemplateAutoConfigurationTest {

  @Test
  void restTemplate() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class,
                RestTemplateAutoConfiguration.class,
                RestTemplateAutoConfiguration.HttpClientRestTemplateCustomizerAutoConfiguration
                    .class,
                MockRestTemplateBuilderAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasBean("restTemplate");
            });
  }

  @Test
  void restTemplateWithInterceptors() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class,
                RestTemplateAutoConfiguration.class,
                RestTemplateAutoConfiguration.HttpClientRestTemplateCustomizerAutoConfiguration
                    .class,
                MockRestTemplateBuilderAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasBean("restTemplate");
              assertThat(context).getBeans(ClientHttpRequestInterceptor.class).hasSize(2);
            });
  }

  @Test
  void nonRestTemplate() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class,
                RestTemplateAutoConfiguration.class,
                RestTemplateAutoConfiguration.HttpClientRestTemplateCustomizerAutoConfiguration
                    .class,
                MockRestTemplateBuilderAutoConfiguration.class))
        .withPropertyValues("common.rest.template.default.enabled=false")
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean("restTemplate");
            });
  }

  @Test
  void loadBalancerRestTemplate() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class,
                RestTemplateAutoConfiguration.class,
                RestTemplateAutoConfiguration.HttpClientRestTemplateCustomizerAutoConfiguration
                    .class,
                MockRestTemplateBuilderAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasBean("loadBalancerRestTemplate");
            });
  }

  @Test
  void nonLoadBalancerRestTemplate() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class,
                RestTemplateAutoConfiguration.class,
                RestTemplateAutoConfiguration.HttpClientRestTemplateCustomizerAutoConfiguration
                    .class,
                MockRestTemplateBuilderAutoConfiguration.class))
        .withClassLoader(
            new FilteredClassLoader(LoadBalancerClient.class, LoadBalancerClientFactory.class))
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean("loadBalancerRestTemplate");
            });
  }

  @Test
  void nonLoadBalancerRestTemplate1() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class,
                RestTemplateAutoConfiguration.class,
                RestTemplateAutoConfiguration.HttpClientRestTemplateCustomizerAutoConfiguration
                    .class,
                MockRestTemplateBuilderAutoConfiguration.class))
        .withPropertyValues("spring.cloud.loadbalancer.enabled=false")
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean("loadBalancerRestTemplate");
            });
  }

  @Test
  void nonLoadBalancerRestTemplate2() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class,
                RestTemplateAutoConfiguration.class,
                RestTemplateAutoConfiguration.HttpClientRestTemplateCustomizerAutoConfiguration
                    .class,
                MockRestTemplateBuilderAutoConfiguration.class))
        .withPropertyValues("common.rest.template.loadbalancer.enabled=false")
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean("loadBalancerRestTemplate");
            });
  }

  @Configuration(proxyBeanMethods = false)
  @Slf4j
  static class MockRestTemplateBuilderAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RestTemplateBuilder.class)
    public RestTemplateBuilder restTemplateBuilder() {
      return new RestTemplateBuilder();
    }

    @Bean
    public ClientHttpRequestInterceptor clientHttpRequestInterceptor1() {
      return mock(ClientHttpRequestInterceptor.class);
    }

    @Bean
    public ClientHttpRequestInterceptor clientHttpRequestInterceptor2() {
      return mock(ClientHttpRequestInterceptor.class);
    }
  }
}
