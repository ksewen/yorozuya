package com.github.ksewen.yorozuya.starter.configuration.http.feign;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.starter.configuration.http.client.HttpClientAutoConfiguration;
import feign.hc5.ApacheHttp5Client;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;

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
        .withPropertyValues(
            "common.http.client.hc5.enabled=true",
            "spring.cloud.openfeign.httpclient.hc5.enabled=true")
        .withClassLoader(
            new FilteredClassLoader(LoadBalancerClient.class, LoadBalancerClientFactory.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(ApacheHttp5Client.class);
              assertThat(context)
                  .getBean("feignClient")
                  .isSameAs(context.getBean(ApacheHttp5Client.class));
            });
  }

  @Test
  void feignClient1() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class, HttpClient5FeignAutoConfiguration.class))
        .withPropertyValues(
            "spring.cloud.openfeign.httpclient.hc5.enabled=true",
            "common.http.client.hc5.enabled=true",
            "spring.cloud.loadbalancer.enabled=false")
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(ApacheHttp5Client.class);
              assertThat(context)
                  .getBean("feignClient")
                  .isSameAs(context.getBean(ApacheHttp5Client.class));
            });
  }

  @Test
  void nonFeignClient() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class, HttpClient5FeignAutoConfiguration.class))
        .withClassLoader(
            new FilteredClassLoader(LoadBalancerClient.class, LoadBalancerClientFactory.class))
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(ApacheHttp5Client.class);
            });
  }

  @Test
  void nonFeignClient1() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                HttpClientAutoConfiguration.class, HttpClient5FeignAutoConfiguration.class))
        .withPropertyValues("spring.cloud.openfeign.okhttp.enabled=true")
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(ApacheHttp5Client.class);
            });
  }
}
