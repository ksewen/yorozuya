package com.github.ksewen.yorozuya.starter.configuration.http.feign;

import com.github.ksewen.yorozuya.starter.configuration.http.client.HttpClientAutoConfiguration;
import feign.Client;
import feign.hc5.ApacheHttp5Client;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 05.09.2023 16:44
 */
@Configuration
@ConditionalOnClass(value = {Client.class, ApacheHttp5Client.class, HttpClient.class})
@AutoConfigureAfter(HttpClientAutoConfiguration.class)
public class HttpClient5FeignAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(Client.class)
  @ConditionalOnBean(HttpClient.class)
  public Client feignClient(@Autowired CloseableHttpClient httpClient) {
    return new ApacheHttp5Client(httpClient);
  }
}
