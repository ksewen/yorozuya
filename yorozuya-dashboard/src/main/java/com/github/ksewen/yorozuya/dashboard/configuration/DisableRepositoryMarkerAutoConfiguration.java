package com.github.ksewen.yorozuya.dashboard.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 18.11.2023 12:26
 */
@Configuration(proxyBeanMethods = false)
public class DisableRepositoryMarkerAutoConfiguration {

  @Bean
  public DisableRepositoryMarkerAutoConfiguration.Marker disableRepositoryMarker() {
    return new Marker();
  }

  public static class Marker {}
}
