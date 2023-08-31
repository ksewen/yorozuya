package com.github.ksewen.yorozuya.starter.configuration.http.client;

import okhttp3.OkHttpClient;
import org.apache.hc.client5.http.classic.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * @author ksewen
 * @date 31.08.2023 16:54
 */
@Configuration
@ConditionalOnClass(HttpClient.class)
@ConditionalOnMissingBean(OkHttpClient.class)
@AutoConfigureAfter(HttpClientAutoConfiguration.class)
public class HttpClientRestTemplateCustomizerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(RestTemplateCustomizer.class)
  @ConditionalOnBean(HttpClient.class)
  public RestTemplateCustomizer httpClientRestTemplateCustomizer(@Autowired HttpClient httpClient) {
    return restTemplate -> {
      restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
      // TODO: config Interceptors for logging, tracing and context passing.
    };
  }
}
