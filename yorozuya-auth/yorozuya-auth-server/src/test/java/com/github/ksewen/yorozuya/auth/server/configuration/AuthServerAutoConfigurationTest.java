package com.github.ksewen.yorozuya.auth.server.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.github.ksewen.yorozuya.auth.server.token.impl.JwtToUserConverter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

/**
 * @author ksewen
 * @date 04.12.2023 21:47
 */
class AuthServerAutoConfigurationTest {

  @Test
  void jwtToUserConverter() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.class,
                MockPasswordEncoderAutoConfiguration.class,
                MockDaoAuthenticationProviderAutoConfiguration.class,
                MockJwtAuthenticationProviderAutoConfiguration.class,
                AuthServerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(JwtToUserConverter.class);
              assertThat(context)
                  .getBean("jwtToUserConverter")
                  .isSameAs(context.getBean(JwtToUserConverter.class));
            });
  }

  @Test
  void withMockJwtToUserConverter() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.class,
                MockJwtToUserConverterAutoConfiguration.class,
                MockPasswordEncoderAutoConfiguration.class,
                MockDaoAuthenticationProviderAutoConfiguration.class,
                MockJwtAuthenticationProviderAutoConfiguration.class,
                AuthServerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(JwtToUserConverter.class);
              assertThat(context)
                  .getBean("mockJwtToUserConverter")
                  .isSameAs(context.getBean(JwtToUserConverter.class));
            });
  }

  @Test
  void passwordEncoder() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.class,
                MockJwtToUserConverterAutoConfiguration.class,
                MockDaoAuthenticationProviderAutoConfiguration.class,
                MockJwtAuthenticationProviderAutoConfiguration.class,
                AuthServerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(PasswordEncoder.class);
              assertThat(context)
                  .getBean("passwordEncoder")
                  .isSameAs(context.getBean(PasswordEncoder.class));
            });
  }

  @Test
  void withMockPasswordEncoder() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.class,
                MockJwtToUserConverterAutoConfiguration.class,
                MockPasswordEncoderAutoConfiguration.class,
                MockDaoAuthenticationProviderAutoConfiguration.class,
                MockJwtAuthenticationProviderAutoConfiguration.class,
                AuthServerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(PasswordEncoder.class);
              assertThat(context)
                  .getBean("mockPasswordEncoder")
                  .isSameAs(context.getBean(PasswordEncoder.class));
            });
  }

  @Test
  void daoAuthenticationProvider() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.class,
                MockJwtToUserConverterAutoConfiguration.class,
                MockPasswordEncoderAutoConfiguration.class,
                MockJwtAuthenticationProviderAutoConfiguration.class,
                TokenAuthenticationAutoConfigurationTest.MockServiceAutoConfiguration.class,
                AuthServerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(DaoAuthenticationProvider.class);
              assertThat(context)
                  .getBean("daoAuthenticationProvider")
                  .isSameAs(context.getBean(DaoAuthenticationProvider.class));
            });
  }

  @Test
  void withMockDaoAuthenticationProvider() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.class,
                MockJwtToUserConverterAutoConfiguration.class,
                MockPasswordEncoderAutoConfiguration.class,
                MockDaoAuthenticationProviderAutoConfiguration.class,
                MockJwtAuthenticationProviderAutoConfiguration.class,
                AuthServerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(DaoAuthenticationProvider.class);
              assertThat(context)
                  .getBean("mockDaoAuthenticationProvider")
                  .isSameAs(context.getBean(DaoAuthenticationProvider.class));
            });
  }

  @Test
  void refreshTokenAuthenticationProvider() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ResourceServerMarkerAutoConfiguration.Marker.class,
                MockJwtToUserConverterAutoConfiguration.class,
                MockPasswordEncoderAutoConfiguration.class,
                MockDaoAuthenticationProviderAutoConfiguration.class,
                TokenAuthenticationAutoConfigurationTest.MockJwtDecoderAutoConfiguration.class,
                AuthServerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(JwtAuthenticationProvider.class);
              assertThat(context)
                  .getBean("refreshTokenAuthenticationProvider")
                  .isSameAs(context.getBean(JwtAuthenticationProvider.class));
            });
  }

  @Test
  void withMockRefreshTokenAuthenticationProvider() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.class,
                MockJwtToUserConverterAutoConfiguration.class,
                MockPasswordEncoderAutoConfiguration.class,
                MockDaoAuthenticationProviderAutoConfiguration.class,
                MockJwtAuthenticationProviderAutoConfiguration.class,
                AuthServerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(JwtAuthenticationProvider.class);
              assertThat(context)
                  .getBean("refreshTokenAuthenticationProvider")
                  .isSameAs(context.getBean(JwtAuthenticationProvider.class));
            });
  }

  @Configuration
  @AutoConfigureBefore(AuthServerAutoConfiguration.class)
  public static class MockJwtToUserConverterAutoConfiguration {
    @Bean
    public JwtToUserConverter mockJwtToUserConverter() {
      return mock(JwtToUserConverter.class);
    }
  }

  @Configuration
  @AutoConfigureBefore(AuthServerAutoConfiguration.class)
  static class MockPasswordEncoderAutoConfiguration {

    @Bean
    public PasswordEncoder mockPasswordEncoder() {
      return mock(PasswordEncoder.class);
    }
  }

  @Configuration
  @AutoConfigureBefore(AuthServerAutoConfiguration.class)
  static class MockDaoAuthenticationProviderAutoConfiguration {
    @Bean
    public DaoAuthenticationProvider mockDaoAuthenticationProvider() {
      return mock(DaoAuthenticationProvider.class);
    }
  }

  @Configuration
  @AutoConfigureBefore(AuthServerAutoConfiguration.class)
  static class MockJwtAuthenticationProviderAutoConfiguration {
    @Bean("refreshTokenAuthenticationProvider")
    public JwtAuthenticationProvider refreshTokenAuthenticationProvider() {
      return mock(JwtAuthenticationProvider.class);
    }
  }
}
