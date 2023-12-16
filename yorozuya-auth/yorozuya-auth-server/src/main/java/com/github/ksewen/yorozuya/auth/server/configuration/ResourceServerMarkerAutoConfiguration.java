package com.github.ksewen.yorozuya.auth.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 26.11.2023 21:10
 */
@Configuration(proxyBeanMethods = false)
public class ResourceServerMarkerAutoConfiguration {

  @Bean
  public Marker resourceServerMarker() {
    return new Marker();
  }

  public static class Marker {}
}
