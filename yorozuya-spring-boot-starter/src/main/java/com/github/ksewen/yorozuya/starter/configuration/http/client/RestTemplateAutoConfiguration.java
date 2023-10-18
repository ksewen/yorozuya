package com.github.ksewen.yorozuya.starter.configuration.http.client;

import okhttp3.OkHttpClient;
import org.apache.hc.client5.http.classic.HttpClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author ksewen
 * @date 30.08.2023 21:36
 */
@Configuration
public class RestTemplateAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(name = "restTemplate")
  @ConditionalOnProperty(
      value = "common.rest.template.default.enabled",
      havingValue = "true",
      matchIfMissing = true)
  @Primary
  public RestTemplate restTemplate(@Autowired RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.build();
  }

  @Bean
  @LoadBalanced
  @Conditional(LoadBalancerRestTemplateEnabled.class)
  @ConditionalOnMissingBean(name = "loadBalancerRestTemplate")
  public RestTemplate loadBalancerRestTemplate(@Autowired RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.build();
  }

  @Configuration
  @ConditionalOnClass(OkHttpClient.class)
  @AutoConfigureAfter(OkHttp3ClientAutoConfiguration.class)
  public static class OkHttp3ClientRestTemplateCustomizerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RestTemplateCustomizer.class)
    @ConditionalOnBean(OkHttpClient.class)
    public RestTemplateCustomizer okHttp3ClientRestTemplateCustomizer(
        @Autowired OkHttpClient okHttpClient,
        @Autowired ObjectProvider<ClientHttpRequestInterceptor> interceptors) {
      return restTemplate -> {
        restTemplate.setRequestFactory(new OkHttp3ClientHttpRequestFactory(okHttpClient));
        restTemplate.setInterceptors(interceptors.orderedStream().toList());
      };
    }
  }

  @Configuration
  @ConditionalOnClass(HttpClient.class)
  @AutoConfigureAfter(HttpClientAutoConfiguration.class)
  public static class HttpClientRestTemplateCustomizerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = {RestTemplateCustomizer.class, OkHttpClient.class})
    @ConditionalOnBean(HttpClient.class)
    public RestTemplateCustomizer httpClientRestTemplateCustomizer(
        @Autowired HttpClient httpClient,
        @Autowired ObjectProvider<ClientHttpRequestInterceptor> interceptors) {
      return restTemplate -> {
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        restTemplate.setInterceptors(interceptors.orderedStream().toList());
      };
    }
  }

  // FIXME: exclude in jacoco report
  static final class LoadBalancerRestTemplateEnabled extends AllNestedConditions {

    public LoadBalancerRestTemplateEnabled() {
      super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnClass(value = {LoadBalancerClient.class, LoadBalancerClientFactory.class})
    static class LoadBalancer {}

    @ConditionalOnProperty(
        value = "spring.cloud.loadbalancer.enabled",
        havingValue = "true",
        matchIfMissing = true)
    static class LoadBalancerEnable {}

    @ConditionalOnProperty(
        value = "common.rest.template.loadbalancer.enabled",
        havingValue = "true",
        matchIfMissing = true)
    static class Enable {}
    ;
  }
}
