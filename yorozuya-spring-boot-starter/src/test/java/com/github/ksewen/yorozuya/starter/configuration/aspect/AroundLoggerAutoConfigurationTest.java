package com.github.ksewen.yorozuya.starter.configuration.aspect;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.common.constant.SystemConstants;
import com.github.ksewen.yorozuya.starter.aspect.logger.AroundLogger;
import com.github.ksewen.yorozuya.starter.configuration.environment.EnvironmentAutoConfiguration;
import com.github.ksewen.yorozuya.starter.configuration.jackson.JacksonJsonHelpersAutoConfiguration;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 05.10.2023 18:24
 */
class AroundLoggerAutoConfigurationTest {

  @Test
  void recordByDefaultAroundAspectLogger() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AroundLoggerAutoConfiguration.class,
                EnvironmentAutoConfiguration.class,
                JacksonJsonHelpersAutoConfiguration.class,
                JacksonAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(AroundLogger.class);
              assertThat(context)
                  .getBean("recordByDefaultAroundAspectLogger")
                  .isSameAs(context.getBean(AroundLogger.class));
            });
  }

  @Test
  void noRecordByDefaultAroundAspectLogger() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(AroundLoggerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(AroundLogger.class);
            });
  }

  @Test
  void notRecordByDefaultAroundAspectLogger() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AroundLoggerAutoConfiguration.class,
                EnvironmentAutoConfiguration.class,
                JacksonJsonHelpersAutoConfiguration.class,
                JacksonAutoConfiguration.class))
        .withPropertyValues("common.aspect.logger.by.default.enable=false")
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(AroundLogger.class);
              assertThat(context)
                  .getBean("notRecordByDefaultAroundAspectLogger")
                  .isSameAs(context.getBean(AroundLogger.class));

              assertThat(context).doesNotHaveBean("recordByDefaultAroundAspectLogger");
            });
  }

  @Test
  void mockAroundAspect() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                MockAroundAspectAutoConfiguration.class,
                AroundLoggerAutoConfiguration.class,
                EnvironmentAutoConfiguration.class,
                JacksonJsonHelpersAutoConfiguration.class,
                JacksonAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(AroundLogger.class);
              assertThat(context)
                  .getBean("mockAroundAspectLogger")
                  .isSameAs(context.getBean(AroundLogger.class));

              assertThat(context).doesNotHaveBean("recordByDefaultAroundAspectLogger");
              assertThat(context).doesNotHaveBean("notRecordByDefaultAroundAspectLogger");
            });
  }

  @Configuration
  @AutoConfigureBefore(AroundLoggerAutoConfiguration.class)
  static class MockAroundAspectAutoConfiguration {

    @Bean
    public AroundLogger mockAroundAspectLogger() {
      return new MockAroundAspect();
    }
  }

  static class MockAroundAspect implements AroundLogger {
    @Override
    public String getApplicationName() {
      return SystemConstants.PROPERTIES_NOT_SET_VALUE;
    }

    @Override
    public String getOperationDescription(JoinPoint joinPoint) {
      return SystemConstants.PROPERTIES_NOT_SET_VALUE;
    }

    @Override
    public String parseArgumentsOrResult(Object object) {
      return SystemConstants.PROPERTIES_NOT_SET_VALUE;
    }
  }
}
