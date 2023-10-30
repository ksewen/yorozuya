package com.github.ksewen.yorozuya.starter.configuration.http.client;

import com.github.ksewen.yorozuya.starter.helper.http.client.RestClientHelpers;
import com.github.ksewen.yorozuya.starter.helper.http.client.impl.RestClientHelpersImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 27.10.2023 20:51
 */
@Configuration
public class RestClientHelpersAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(RestClientHelpers.class)
  public RestClientHelpers restClientHelpers() {
    return new RestClientHelpersImpl();
  }
}
