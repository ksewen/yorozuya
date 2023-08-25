package com.github.ksewen.yorozuya.starter.configuration;

import com.github.ksewen.yorozuya.common.environment.Environment;
import com.github.ksewen.yorozuya.common.environment.impl.BasicEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author ksewen
 * @date 25.08.2023 22:21
 */
@AutoConfiguration
public class EnvironmentAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(Environment.class)
  public Environment systemInformation(
      @Value("${spring.profiles.active:UNSET}") String environment,
      @Value("${spring.application.name:UNSET}") String applicationName) {
    BasicEnvironment basicEnvironment = BasicEnvironment.getInstance();
    basicEnvironment.initEnvironment(environment);
    basicEnvironment.initApplicationName(applicationName);
    return basicEnvironment;
  }
}
