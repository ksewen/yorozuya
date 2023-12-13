package com.github.ksewen.yorozuya.auth.server.configuration;

import com.github.ksewen.yorozuya.auth.server.key.KeyPairManager;
import com.github.ksewen.yorozuya.auth.server.key.PropertiesRSAKeyPairManager;
import com.github.ksewen.yorozuya.auth.server.token.TokenProvider;
import com.github.ksewen.yorozuya.auth.server.token.impl.JwtTokenProvider;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import java.security.interfaces.RSAPublicKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author ksewen
 * @date 26.11.2023 21:48
 */
@Configuration(proxyBeanMethods = false)
@Conditional(AuthServerAutoConfiguration.AuthOderResourceServerEnabled.class)
@EnableConfigurationProperties(TokenAuthenticationProperties.class)
@RequiredArgsConstructor
public class TokenAuthenticationAutoConfiguration {

  private final TokenAuthenticationProperties properties;

  @Bean
  @Conditional(AuthServerAutoConfiguration.AuthOderResourceServerEnabled.class)
  @ConditionalOnMissingBean(KeyPairManager.class)
  public KeyPairManager keyPairManager() {
    return new PropertiesRSAKeyPairManager(this.properties);
  }

  @Bean
  @Primary
  @ConditionalOnMissingBean(name = "jwtAccessTokenDecoder")
  JwtDecoder jwtAccessTokenDecoder(@Autowired KeyPairManager keyPairManager) {
    return NimbusJwtDecoder.withPublicKey(
            (RSAPublicKey) keyPairManager.getAccessTokenKeyPair().getPublic())
        .build();
  }

  @Bean
  @Primary
  @ConditionalOnMissingBean(name = "jwtAccessTokenEncoder")
  JwtEncoder jwtAccessTokenEncoder(@Autowired KeyPairManager keyPairManager) {
    return new NimbusJwtEncoder(
        new ImmutableJWKSet<>(
            new JWKSet(
                new RSAKey.Builder(
                        (RSAPublicKey) keyPairManager.getAccessTokenKeyPair().getPublic())
                    .privateKey(keyPairManager.getAccessTokenKeyPair().getPrivate())
                    .build())));
  }

  @Bean
  @ConditionalOnMissingBean(name = "jwtRefreshTokenDecoder")
  JwtDecoder jwtRefreshTokenDecoder(@Autowired KeyPairManager keyPairManager) {
    return NimbusJwtDecoder.withPublicKey(
            (RSAPublicKey) keyPairManager.getRefreshTokenKeyPair().getPublic())
        .build();
  }

  @Bean
  @ConditionalOnMissingBean(name = "jwtRefreshTokenEncoder")
  JwtEncoder jwtRefreshTokenEncoder(@Autowired KeyPairManager keyPairManager) {
    return new NimbusJwtEncoder(
        new ImmutableJWKSet<>(
            new JWKSet(
                new RSAKey.Builder(
                        (RSAPublicKey) keyPairManager.getRefreshTokenKeyPair().getPublic())
                    .privateKey(keyPairManager.getRefreshTokenKeyPair().getPrivate())
                    .build())));
  }

  @Bean
  @ConditionalOnMissingBean(TokenProvider.class)
  public TokenProvider tokenProvider(
      @Autowired JwtEncoder jwtAccessTokenEncoder,
      @Autowired JwtDecoder jwtAccessTokenDecoder,
      @Autowired @Qualifier("jwtRefreshTokenEncoder") JwtEncoder jwtRefreshTokenEncoder) {
    return new JwtTokenProvider(
        jwtAccessTokenEncoder,
        jwtAccessTokenDecoder,
        jwtRefreshTokenEncoder,
        this.properties.getExpiration().toSeconds(),
        this.properties.getRefreshTokenExpiration().toSeconds(),
        this.properties.getIssuer(),
        this.properties.isWithAuthorities());
  }

  @Bean
  @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
  @ConditionalOnBean(ResourceServerMarkerAutoConfiguration.Marker.class)
  public AuthenticationEntryPoint bearerTokenAuthenticationEntryPoint() {
    return new BearerTokenAuthenticationEntryPoint();
  }

  @Bean
  @ConditionalOnMissingBean(AccessDeniedHandler.class)
  @ConditionalOnBean(ResourceServerMarkerAutoConfiguration.Marker.class)
  public AccessDeniedHandler bearerTokenAccessDeniedHandler() {
    return new BearerTokenAccessDeniedHandler();
  }
}
