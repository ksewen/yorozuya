package com.github.ksewen.yorozuya.auth.server.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.github.ksewen.yorozuya.auth.server.security.AuthenticationManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 13.12.2023 17:11
 */
class ResourceServerAutoConfigurationTest {

  @Test
  void authenticationHelper() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ResourceServerMarkerAutoConfiguration.class,
                TokenAuthenticationAutoConfigurationTest.MockServiceAutoConfiguration.class,
                ResourceServerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(AuthenticationManager.class);
              assertThat(context)
                  .getBean("authenticationManager")
                  .isSameAs(context.getBean(AuthenticationManager.class));
            });
  }

  @Test
  void mockAuthenticationHelper() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ResourceServerMarkerAutoConfiguration.class,
                TokenAuthenticationAutoConfigurationTest.MockServiceAutoConfiguration.class,
                MockAuthenticationManagerAutoConfiguration.class,
                ResourceServerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(AuthenticationManager.class);
              assertThat(context)
                  .getBean("mockAuthenticationManager")
                  .isSameAs(context.getBean(AuthenticationManager.class));
            });
  }

  @Test
  void disableResourceServer() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(ResourceServerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(AuthenticationManager.class);
            });
  }

  @Configuration
  @AutoConfigureBefore(ResourceServerAutoConfiguration.class)
  static class MockAuthenticationManagerAutoConfiguration {

    @Bean
    public AuthenticationManager mockAuthenticationManager() {
      return mock(AuthenticationManager.class);
    }
  }
}
