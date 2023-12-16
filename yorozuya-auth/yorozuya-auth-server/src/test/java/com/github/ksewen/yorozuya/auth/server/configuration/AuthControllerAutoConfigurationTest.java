package com.github.ksewen.yorozuya.auth.server.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.github.ksewen.yorozuya.auth.server.controller.AuthController;
import com.github.ksewen.yorozuya.auth.server.service.UserDetailsExtraService;
import com.github.ksewen.yorozuya.auth.server.service.impl.UserDetailsExtraServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author ksewen
 * @date 13.12.2023 17:32
 */
class AuthControllerAutoConfigurationTest {

  @Test
  void userDetailsExtraService() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.Marker.class,
                MockAuthControllerAutoConfiguration.class,
                AuthControllerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(UserDetailsExtraService.class);
              assertThat(context)
                  .getBean("userDetailsExtraService")
                  .isSameAs(context.getBean(UserDetailsExtraService.class));
            });
  }

  @Test
  void mockUserDetailsExtraService() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.Marker.class,
                MockUserDetailsExtraServiceAutoConfiguration.class,
                MockAuthControllerAutoConfiguration.class,
                AuthControllerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(UserDetailsExtraService.class);
              assertThat(context)
                  .getBean("userDetailsExtraService")
                  .isSameAs(context.getBean(UserDetailsExtraService.class));
            });
  }

  @Test
  void authController() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.Marker.class,
                AuthServerAutoConfigurationTest.MockDaoAuthenticationProviderAutoConfiguration
                    .class,
                TokenAuthenticationAutoConfigurationTest.MockTokenProviderAutoConfiguration.class,
                TokenAuthenticationAutoConfigurationTest.MockServiceAutoConfiguration.class,
                MockUserDetailsExtraServiceAutoConfiguration.class,
                AuthControllerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(AuthController.class);
              assertThat(context)
                  .getBean("authController")
                  .isSameAs(context.getBean(AuthController.class));
            });
  }

  @Test
  void mockAuthController() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.Marker.class,
                AuthServerAutoConfigurationTest.MockDaoAuthenticationProviderAutoConfiguration
                    .class,
                TokenAuthenticationAutoConfigurationTest.MockTokenProviderAutoConfiguration.class,
                TokenAuthenticationAutoConfigurationTest.MockServiceAutoConfiguration.class,
                MockAuthControllerAutoConfiguration.class,
                MockUserDetailsExtraServiceAutoConfiguration.class,
                AuthControllerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(AuthController.class);
              assertThat(context)
                  .getBean("authController")
                  .isSameAs(context.getBean(AuthController.class));
            });
  }

  @Test
  void closeLoginEndpoint() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.Marker.class,
                AuthControllerAutoConfiguration.class))
        .withPropertyValues("security.login.endpoint.enable=false")
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(UserDetailsExtraServiceImpl.class);
              assertThat(context).doesNotHaveBean(AuthController.class);
            });
  }

  @Configuration
  @AutoConfigureBefore(AuthControllerAutoConfiguration.class)
  static class MockUserDetailsExtraServiceAutoConfiguration {

    @Bean
    public UserDetailsExtraService<Long, UserDetails> userDetailsExtraService() {
      return mock(UserDetailsExtraServiceImpl.class);
    }
  }

  @Configuration
  @AutoConfigureBefore(AuthControllerAutoConfiguration.class)
  static class MockAuthControllerAutoConfiguration {

    @Bean
    public AuthController authController() {
      return mock(AuthController.class);
    }
  }
}
