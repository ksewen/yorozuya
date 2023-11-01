package com.github.ksewen.yorozuya.dashboard.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 25.10.2023 17:34
 */
@Configuration(proxyBeanMethods = false)
public class DashboardMarkerAutoConfiguration {

  @Bean
  public Marker adminServerMarker() {
    return new Marker();
  }

  public static class Marker {}
}
