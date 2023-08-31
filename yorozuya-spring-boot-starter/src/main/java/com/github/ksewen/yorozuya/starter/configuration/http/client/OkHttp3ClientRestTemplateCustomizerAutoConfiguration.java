package com.github.ksewen.yorozuya.starter.configuration.http.client;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

/**
 * @author ksewen
 * @date 31.08.2023 16:53
 */
@Configuration
@ConditionalOnClass(OkHttpClient.class)
@AutoConfigureAfter(OkHttp3ClientAutoConfiguration.class)
public class OkHttp3ClientRestTemplateCustomizerAutoConfiguration {

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
