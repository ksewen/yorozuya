package com.github.ksewen.yorozuya.starter.configuration.observation;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 04.10.2023 12:06
 */
@Configuration(proxyBeanMethods = false)
public class ObservedAspectConfiguration {

  @Bean
  public ObservedAspect observedAspect(@Autowired ObservationRegistry observationRegistry) {
    return new ObservedAspect(observationRegistry);
  }
}
