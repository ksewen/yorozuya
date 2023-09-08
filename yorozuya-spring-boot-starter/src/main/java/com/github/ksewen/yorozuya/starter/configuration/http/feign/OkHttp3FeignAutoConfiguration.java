package com.github.ksewen.yorozuya.starter.configuration.http.feign;

import com.github.ksewen.yorozuya.starter.configuration.http.client.OkHttp3ClientAutoConfiguration;
import feign.Client;
import feign.okhttp.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 05.09.2023 16:44
 */
@Configuration
@ConditionalOnClass(value = {Client.class, OkHttpClient.class, okhttp3.OkHttpClient.class})
@ConditionalOnProperty("spring.cloud.openfeign.okhttp.enabled")
@Conditional(OnNoLoadBalancerOrDisable.class)
@AutoConfigureAfter(OkHttp3ClientAutoConfiguration.class)
public class OkHttp3FeignAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(Client.class)
  @ConditionalOnBean(okhttp3.OkHttpClient.class)
  public Client feignClient(@Autowired okhttp3.OkHttpClient client) {
    return new OkHttpClient(client);
  }
}
