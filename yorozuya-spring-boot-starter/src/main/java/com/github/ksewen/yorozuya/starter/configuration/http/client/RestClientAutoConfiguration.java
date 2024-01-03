package com.github.ksewen.yorozuya.starter.configuration.http.client;

import com.github.ksewen.yorozuya.starter.configuration.http.client.interceptor.CustomClientHttpRequestInterceptor;
import lombok.Generated;
import org.apache.hc.client5.http.classic.HttpClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * @author ksewen
 * @date 02.01.2024 21:49
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(RestClientAutoConfiguration.RestClientBuilderAutoConfiguration.class)
public class RestClientAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(name = "restClient")
  @ConditionalOnProperty(value = "common.rest.client.default.enabled", havingValue = "true")
  @Primary
  public RestClient restClient(@Autowired RestClient.Builder restClientBuilder) {
    return restClientBuilder.build();
  }

  @Bean
  @Conditional(LoadBalancerRestClientEnabled.class)
  @ConditionalOnMissingBean(name = "loadBalancerRestClient")
  public RestClient loadBalancerRestClient(
      @Autowired @Qualifier("loadBalancerRestClientBuilder") RestClient.Builder restClientBuilder) {
    return restClientBuilder.build();
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnClass(HttpClient.class)
  @AutoConfigureAfter(HttpClientAutoConfiguration.class)
  static class RestClientBuilderAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "restClientBuilder")
    @ConditionalOnProperty(value = "common.rest.client.default.enabled", havingValue = "true")
    @ConditionalOnBean(HttpClient.class)
    @Primary
    public RestClient.Builder restClientBuilder(
        @Autowired HttpClient httpClient,
        @Autowired ObjectProvider<CustomClientHttpRequestInterceptor> interceptors) {
      return RestClient.builder()
          .requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient))
          .requestInterceptors(
              i ->
                  i.addAll(
                      interceptors
                          .orderedStream()
                          .map(x -> (ClientHttpRequestInterceptor) x)
                          .toList()));
    }

    @Bean
    @LoadBalanced
    @Conditional(LoadBalancerRestClientEnabled.class)
    @ConditionalOnMissingBean(name = "loadBalancerRestClientBuilder")
    @ConditionalOnBean(HttpClient.class)
    public RestClient.Builder loadBalancerRestClientBuilder(
        @Autowired HttpClient httpClient,
        @Autowired ObjectProvider<CustomClientHttpRequestInterceptor> interceptors) {
      return RestClient.builder()
          .requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient))
          .requestInterceptors(
              i ->
                  i.addAll(
                      interceptors
                          .orderedStream()
                          .map(x -> (ClientHttpRequestInterceptor) x)
                          .toList()));
    }
  }

  @Generated
  static final class LoadBalancerRestClientEnabled extends AllNestedConditions {

    public LoadBalancerRestClientEnabled() {
      super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnClass(value = {LoadBalancerClient.class, LoadBalancerClientFactory.class})
    @SuppressWarnings("unused")
    @Generated
    static class LoadBalancer {}

    @ConditionalOnProperty(
        value = "spring.cloud.loadbalancer.enabled",
        havingValue = "true",
        matchIfMissing = true)
    @SuppressWarnings("unused")
    @Generated
    static class LoadBalancerEnable {}

    @ConditionalOnProperty(value = "common.rest.client.loadbalancer.enabled", havingValue = "true")
    @SuppressWarnings("unused")
    @Generated
    static class Enable {}
  }
}
