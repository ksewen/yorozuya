package com.github.ksewen.yorozuya.starter.configuration.context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.github.ksewen.yorozuya.common.context.Context;
import com.github.ksewen.yorozuya.common.context.impl.ServiceContext;
import com.github.ksewen.yorozuya.starter.configuration.context.interceptor.ContextClientHttpRequestInterceptor;
import com.github.ksewen.yorozuya.starter.configuration.context.interceptor.ContextRequestInterceptor;
import com.github.ksewen.yorozuya.starter.filter.context.extension.ContextFilterExtension;
import com.github.ksewen.yorozuya.starter.helper.context.ContextHelpers;
import com.github.ksewen.yorozuya.starter.helper.context.impl.ServiceContextHelpers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 15.10.2023 10:56
 */
@Slf4j
class ServiceContextAutoConfigurationTest {

  @Test
  void serviceContext() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(ServiceContextAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(Context.class);
              assertThat(context)
                  .getBean("serviceContext")
                  .isSameAs(context.getBean(ServiceContext.class));
            });
  }

  @Test
  void serviceContextHelpers() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(ServiceContextAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(ContextHelpers.class);
              assertThat(context)
                  .getBean("serviceContextHelpers")
                  .isSameAs(context.getBean(ServiceContextHelpers.class));
            });
  }

  @Test
  void mockContextHelpers() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ServiceContextAutoConfiguration.class, MockContextAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(ContextHelpers.class);
              assertThat(context)
                  .getBean("mockContextHelpers")
                  .isSameAs(context.getBean(ContextHelpers.class));

              assertThat(context).doesNotHaveBean(ServiceContextHelpers.class);
            });
  }

  @Test
  void filterRegistrationBean() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(ServiceContextAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasBean("filterRegistrationBean");
            });
  }

  @Test
  void filterRegistrationBeanWithExtension() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ServiceContextAutoConfiguration.class, MockContextAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasBean("filterRegistrationBean");
              assertThat(context).hasBean("mockContextFilterExtension1");
              assertThat(context).hasBean("mockContextFilterExtension2");
            });
  }

  @Test
  void contextRequestInterceptor() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ServiceContextAutoConfiguration.class, MockContextAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(ContextRequestInterceptor.class);
              assertThat(context)
                  .getBean("contextRequestInterceptor")
                  .isSameAs(context.getBean(ContextRequestInterceptor.class));
            });
  }

  @Test
  void contextClientHttpRequestInterceptor() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ServiceContextAutoConfiguration.class, MockContextAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(ContextClientHttpRequestInterceptor.class);
              assertThat(context)
                  .getBean("contextClientHttpRequestInterceptor")
                  .isSameAs(context.getBean(ContextClientHttpRequestInterceptor.class));
            });
  }

  @Configuration
  @AutoConfigureBefore(ServiceContextAutoConfiguration.class)
  static class MockContextAutoConfiguration {

    @Bean
    public ContextHelpers mockContextHelpers() {
      return new ContextHelpers() {
        @Override
        public boolean hasKey(String key) {
          return false;
        }

        @Override
        public String get(String key) {
          return null;
        }

        @Override
        public void put(String key, String value) {}

        @Override
        public void remove(String key) {}
      };
    }

    @Bean
    public ContextFilterExtension mockContextFilterExtension1() {
      return servletRequest -> log.info("do something...");
    }

    @Bean
    public ContextFilterExtension mockContextFilterExtension2() {
      return servletRequest -> log.info("do something else...");
    }
  }
}
