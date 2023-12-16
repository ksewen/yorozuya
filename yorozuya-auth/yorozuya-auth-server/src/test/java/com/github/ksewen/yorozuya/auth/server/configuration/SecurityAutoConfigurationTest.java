package com.github.ksewen.yorozuya.auth.server.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author ksewen
 * @date 10.12.2023 22:19
 */
class SecurityAutoConfigurationTest {

  @Test
  void authSecurityFilterChain() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.class,
                AuthServerAutoConfigurationTest.MockJwtToUserConverterAutoConfiguration.class,
                MockWebEndpointPropertiesAutoConfiguration.class,
                SecurityAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(SecurityFilterChain.class);
              assertThat(context)
                  .getBean("authSecurityFilterChain")
                  .isSameAs(context.getBean(SecurityFilterChain.class));
            });
  }

  @Test
  void resourceSecurityFilterChain() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ResourceServerMarkerAutoConfiguration.class,
                AuthServerAutoConfigurationTest.MockJwtToUserConverterAutoConfiguration.class,
                TokenAuthenticationAutoConfigurationTest.MockJwtDecoderAutoConfiguration.class,
                TokenAuthenticationAutoConfigurationTest
                    .MockAuthenticationEntryPointAutoConfiguration.class,
                TokenAuthenticationAutoConfigurationTest.MockAccessDeniedHandlerAutoConfiguration
                    .class,
                MockWebEndpointPropertiesAutoConfiguration.class,
                SecurityAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(SecurityFilterChain.class);
              assertThat(context)
                  .getBean("resourceSecurityFilterChain")
                  .isSameAs(context.getBean(SecurityFilterChain.class));
            });
  }

  @Test
  void noSecurityFilterChain() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(SecurityAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(SecurityFilterChain.class);
            });
  }

  @Configuration
  @AutoConfigureBefore(SecurityAutoConfiguration.class)
  public static class MockWebEndpointPropertiesAutoConfiguration {

    @Bean
    public WebEndpointProperties mockWebEndpointProperties() {
      return new WebEndpointProperties();
    }
  }
}
