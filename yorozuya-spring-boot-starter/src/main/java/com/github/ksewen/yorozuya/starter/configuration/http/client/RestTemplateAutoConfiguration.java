package com.github.ksewen.yorozuya.starter.configuration.http.client;

import okhttp3.OkHttpClient;
import org.apache.hc.client5.http.classic.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
  public RestTemplate restTemplate(@Autowired RestTemplateBuilder restTemplateBuilder) {
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
        @Autowired OkHttpClient okHttpClient) {
      return restTemplate -> {
        restTemplate.setRequestFactory(new OkHttp3ClientHttpRequestFactory(okHttpClient));
        // TODO: config Interceptors for logging, tracing and context passing.
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
        @Autowired HttpClient httpClient) {
      return restTemplate -> {
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        // TODO: config Interceptors for logging, tracing and context passing.
      };
    }
  }
}
