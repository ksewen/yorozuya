package com.github.ksewen.yorozuya.starter.configuration.aspect;

import com.github.ksewen.yorozuya.common.environment.Environment;
import com.github.ksewen.yorozuya.starter.aspect.logger.AroundLogger;
import com.github.ksewen.yorozuya.starter.aspect.logger.impl.NotRecordByDefaultAroundLogger;
import com.github.ksewen.yorozuya.starter.aspect.logger.impl.RecordByDefaultAroundLogger;
import com.github.ksewen.yorozuya.starter.configuration.environment.EnvironmentAutoConfiguration;
import com.github.ksewen.yorozuya.starter.configuration.jackson.JacksonJsonHelpersAutoConfiguration;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 05.10.2023 18:19
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean({Environment.class, JsonHelpers.class})
@AutoConfigureAfter({EnvironmentAutoConfiguration.class, JacksonJsonHelpersAutoConfiguration.class})
public class AroundLoggerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(AroundLogger.class)
  @ConditionalOnProperty(
      value = "common.aspect.logger.by.default.enable",
      havingValue = "true",
      matchIfMissing = true)
  public RecordByDefaultAroundLogger recordByDefaultAroundAspectLogger(
      @Autowired Environment environment, @Autowired JsonHelpers jsonHelpers) {
    return new RecordByDefaultAroundLogger(environment, jsonHelpers);
  }

  @Bean
  @ConditionalOnMissingBean(AroundLogger.class)
  @ConditionalOnProperty(
      value = "common.aspect.logger.by.default.enable",
      havingValue = "false",
      matchIfMissing = true)
  public NotRecordByDefaultAroundLogger notRecordByDefaultAroundAspectLogger(
      @Autowired Environment environment, @Autowired JsonHelpers jsonHelpers) {
    return new NotRecordByDefaultAroundLogger(environment, jsonHelpers);
  }
}
