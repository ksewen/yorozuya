package com.github.ksewen.yorozuya.starter.configuration.http.feign;

import com.github.ksewen.yorozuya.starter.configuration.http.client.HttpClientAutoConfiguration;
import feign.Client;
import feign.hc5.ApacheHttp5Client;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 05.09.2023 16:44
 */
@Configuration
@ConditionalOnClass(value = {Client.class, ApacheHttp5Client.class, HttpClient.class})
@ConditionalOnProperty("spring.cloud.openfeign.httpclient.enabled")
@ConditionalOnMissingClass(
    value = {
      "org.springframework.cloud.client.loadbalancer.LoadBalancerClient",
      "org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory"
    })
@AutoConfigureAfter(HttpClientAutoConfiguration.class)
public class HttpClient5FeignAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(Client.class)
  @ConditionalOnBean(HttpClient.class)
  public Client feignClient(@Autowired CloseableHttpClient httpClient) {
    return new ApacheHttp5Client(httpClient);
  }
}
