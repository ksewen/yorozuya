package com.github.ksewen.yorozuya.auth.server.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.github.ksewen.yorozuya.auth.server.key.KeyPairManager;
import com.github.ksewen.yorozuya.auth.server.key.PropertiesRASKeyPairManagerTest;
import com.github.ksewen.yorozuya.auth.server.token.TokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author ksewen
 * @date 29.11.2023 17:26
 */
class TokenAuthenticationAutoConfigurationTest {

  @Test
  void keyPairManager() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.class,
                MockTokenProviderAutoConfiguration.class,
                TokenAuthenticationAutoConfiguration.class))
        .withPropertyValues(
            "security.token.access-public-key="
                + PropertiesRASKeyPairManagerTest.PUBLIC_KEY_FOR_TEST,
            "security.token.access-private-key="
                + PropertiesRASKeyPairManagerTest.PRIVATE_KEY_FOR_TEST,
            "security.token.refresh-public-key="
                + PropertiesRASKeyPairManagerTest.PUBLIC_KEY_FOR_TEST,
            "security.token.refresh-private-key="
                + PropertiesRASKeyPairManagerTest.PRIVATE_KEY_FOR_TEST)
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(KeyPairManager.class);
              assertThat(context)
                  .getBean("keyPairManager")
                  .isSameAs(context.getBean(KeyPairManager.class));
            });
  }

  @Test
  void withMockKeyPairManager() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.class,
                MockTokenProviderAutoConfiguration.class,
                MockKeyPairManagerAutoConfiguration.class,
                MockJwtEncoderAutoConfiguration.class,
                MockJwtDecoderAutoConfiguration.class,
                TokenAuthenticationAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(KeyPairManager.class);
              assertThat(context)
                  .getBean("mockKeyPairManager")
                  .isSameAs(context.getBean(KeyPairManager.class));

              assertThat(context).doesNotHaveBean("keyPairManager");
            });
  }

  @Test
  void jwtTokenDecoder() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ResourceServerMarkerAutoConfiguration.class,
                MockTokenProviderAutoConfiguration.class,
                TokenAuthenticationAutoConfiguration.class))
        .withPropertyValues(
            "security.token.access-public-key="
                + PropertiesRASKeyPairManagerTest.PUBLIC_KEY_FOR_TEST,
            "security.token.access-private-key="
                + PropertiesRASKeyPairManagerTest.PRIVATE_KEY_FOR_TEST,
            "security.token.refresh-public-key="
                + PropertiesRASKeyPairManagerTest.PUBLIC_KEY_FOR_TEST,
            "security.token.refresh-private-key="
                + PropertiesRASKeyPairManagerTest.PRIVATE_KEY_FOR_TEST)
        .run(
            (context) -> {
              assertThat(context).hasBean("jwtAccessTokenDecoder");
              assertThat(context).hasBean("jwtRefreshTokenDecoder");
            });
  }

  @Test
  void withMockTokenDecoder() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ResourceServerMarkerAutoConfiguration.class,
                MockJwtEncoderAutoConfiguration.class,
                MockJwtDecoderAutoConfiguration.class,
                MockTokenProviderAutoConfiguration.class,
                TokenAuthenticationAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasBean("jwtAccessTokenEncoder");
              assertThat(context).hasBean("jwtRefreshTokenDecoder");
            });
  }

  @Test
  void jwtTokenEncoder() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.class,
                MockTokenProviderAutoConfiguration.class,
                TokenAuthenticationAutoConfiguration.class))
        .withPropertyValues(
            "security.token.access-public-key="
                + PropertiesRASKeyPairManagerTest.PUBLIC_KEY_FOR_TEST,
            "security.token.access-private-key="
                + PropertiesRASKeyPairManagerTest.PRIVATE_KEY_FOR_TEST,
            "security.token.refresh-public-key="
                + PropertiesRASKeyPairManagerTest.PUBLIC_KEY_FOR_TEST,
            "security.token.refresh-private-key="
                + PropertiesRASKeyPairManagerTest.PRIVATE_KEY_FOR_TEST)
        .run(
            (context) -> {
              assertThat(context).hasBean("jwtAccessTokenEncoder");
              assertThat(context).hasBean("jwtRefreshTokenEncoder");
            });
  }

  @Test
  void withMockTokenEncoder() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                AuthServerMarkerAutoConfiguration.class,
                MockJwtEncoderAutoConfiguration.class,
                MockJwtDecoderAutoConfiguration.class,
                MockTokenProviderAutoConfiguration.class,
                TokenAuthenticationAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasBean("jwtAccessTokenEncoder");
              assertThat(context).hasBean("jwtRefreshTokenEncoder");
            });
  }

  @Test
  void tokenProvider() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ResourceServerMarkerAutoConfiguration.class,
                MockKeyPairManagerAutoConfiguration.class,
                MockJwtEncoderAutoConfiguration.class,
                MockJwtDecoderAutoConfiguration.class,
                TokenAuthenticationAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(TokenProvider.class);
              assertThat(context)
                  .getBean("tokenProvider")
                  .isSameAs(context.getBean(TokenProvider.class));
            });
  }

  @Test
  void withMockTokenProvider() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ResourceServerMarkerAutoConfiguration.class,
                MockKeyPairManagerAutoConfiguration.class,
                MockJwtEncoderAutoConfiguration.class,
                MockJwtDecoderAutoConfiguration.class,
                MockTokenProviderAutoConfiguration.class,
                TokenAuthenticationAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(TokenProvider.class);
              assertThat(context)
                  .getBean("mockTokenProvider")
                  .isSameAs(context.getBean(TokenProvider.class));

              assertThat(context).doesNotHaveBean("tokenProvider");
            });
  }

  @Test
  void bearerTokenAuthenticationEntryPoint() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ResourceServerMarkerAutoConfiguration.class,
                MockJwtEncoderAutoConfiguration.class,
                MockJwtDecoderAutoConfiguration.class,
                MockTokenProviderAutoConfiguration.class,
                MockAccessDeniedHandlerAutoConfiguration.class,
                TokenAuthenticationAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(AuthenticationEntryPoint.class);
              assertThat(context)
                  .getBean("bearerTokenAuthenticationEntryPoint")
                  .isSameAs(context.getBean(AuthenticationEntryPoint.class));
            });
  }

  @Test
  void mockAuthenticationEntryPoint() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ResourceServerMarkerAutoConfiguration.class,
                MockJwtEncoderAutoConfiguration.class,
                MockJwtDecoderAutoConfiguration.class,
                MockTokenProviderAutoConfiguration.class,
                MockAccessDeniedHandlerAutoConfiguration.class,
                MockAuthenticationEntryPointAutoConfiguration.class,
                TokenAuthenticationAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(AuthenticationEntryPoint.class);
              assertThat(context)
                  .getBean("mockAuthenticationEntryPoint")
                  .isSameAs(context.getBean(AuthenticationEntryPoint.class));
            });
  }

  @Test
  void bearerTokenAccessDeniedHandler() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ResourceServerMarkerAutoConfiguration.class,
                MockJwtEncoderAutoConfiguration.class,
                MockJwtDecoderAutoConfiguration.class,
                MockTokenProviderAutoConfiguration.class,
                MockAuthenticationEntryPointAutoConfiguration.class,
                TokenAuthenticationAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(AccessDeniedHandler.class);
              assertThat(context)
                  .getBean("bearerTokenAccessDeniedHandler")
                  .isSameAs(context.getBean(AccessDeniedHandler.class));
            });
  }

  @Test
  void mockAccessDeniedHandler() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ResourceServerMarkerAutoConfiguration.class,
                MockJwtEncoderAutoConfiguration.class,
                MockJwtDecoderAutoConfiguration.class,
                MockTokenProviderAutoConfiguration.class,
                MockAuthenticationEntryPointAutoConfiguration.class,
                MockAccessDeniedHandlerAutoConfiguration.class,
                TokenAuthenticationAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(AccessDeniedHandler.class);
              assertThat(context)
                  .getBean("mockAccessDeniedHandler")
                  .isSameAs(context.getBean(AccessDeniedHandler.class));
            });
  }

  @Test
  void disableAuthServer() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                MockServiceAutoConfiguration.class, TokenAuthenticationAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(KeyPairManager.class);
              assertThat(context).doesNotHaveBean(TokenProvider.class);
              assertThat(context).doesNotHaveBean(JwtEncoder.class);
              assertThat(context).doesNotHaveBean(JwtDecoder.class);
              assertThat(context).doesNotHaveBean(AuthenticationEntryPoint.class);
              assertThat(context).doesNotHaveBean(AccessDeniedHandler.class);
            });
  }

  @Configuration
  public static class MockServiceAutoConfiguration {

    @Bean
    public UserDetailsService mockUserDetailsService() {
      return mock(UserDetailsService.class);
    }

    @Bean
    public TokenService mockTokenService() {
      return mock(TokenService.class);
    }
  }

  @Configuration
  @AutoConfigureBefore(TokenAuthenticationAutoConfiguration.class)
  public static class MockKeyPairManagerAutoConfiguration {

    @Bean
    public KeyPairManager mockKeyPairManager() {
      return mock(KeyPairManager.class);
    }
  }

  @Configuration
  @AutoConfigureBefore(TokenAuthenticationAutoConfiguration.class)
  public static class MockTokenProviderAutoConfiguration {

    @Bean
    public TokenProvider mockTokenProvider() {
      return mock(TokenProvider.class);
    }
  }

  @Configuration
  @AutoConfigureBefore(TokenAuthenticationAutoConfiguration.class)
  public static class MockJwtEncoderAutoConfiguration {

    @Bean("jwtAccessTokenEncoder")
    @Primary
    public JwtEncoder jwtAccessTokenEncoder() {
      return mock(JwtEncoder.class);
    }

    @Bean("jwtRefreshTokenEncoder")
    public JwtEncoder jwtRefreshTokenEncoder() {
      return mock(JwtEncoder.class);
    }
  }

  @Configuration
  @AutoConfigureBefore(TokenAuthenticationAutoConfiguration.class)
  public static class MockJwtDecoderAutoConfiguration {

    @Bean("jwtAccessTokenDecoder")
    @Primary
    public JwtDecoder jwtAccessTokenDecoder() {
      return mock(JwtDecoder.class);
    }

    @Bean("jwtRefreshTokenDecoder")
    public JwtDecoder jwtRefreshTokenDecoder() {
      return mock(JwtDecoder.class);
    }
  }

  @Configuration
  @AutoConfigureBefore(TokenAuthenticationAutoConfiguration.class)
  static class MockAuthenticationEntryPointAutoConfiguration {

    @Bean
    public AuthenticationEntryPoint mockAuthenticationEntryPoint() {
      return mock(AuthenticationEntryPoint.class);
    }
  }

  @Configuration
  @AutoConfigureBefore(TokenAuthenticationAutoConfiguration.class)
  static class MockAccessDeniedHandlerAutoConfiguration {

    @Bean
    public AccessDeniedHandler mockAccessDeniedHandler() {
      return mock(AccessDeniedHandler.class);
    }
  }
}
