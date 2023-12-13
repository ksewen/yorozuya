package com.github.ksewen.yorozuya.auth.server.token.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

/**
 * @author ksewen
 * @date 23.11.2023 10:04
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JwtTokenProviderTest {

  private final JwtTokenProvider PROVIDER;

  private final String USERNAME = "username";

  private final long EXPIRATION = 5;

  {
    try {
      KeyPair accessKeyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
      KeyPair refreshKeyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();

      NimbusJwtEncoder accessTokenEncoder =
          new NimbusJwtEncoder(
              new ImmutableJWKSet<>(
                  new JWKSet(
                      new RSAKey.Builder((RSAPublicKey) accessKeyPair.getPublic())
                          .privateKey((RSAPrivateCrtKey) accessKeyPair.getPrivate())
                          .build())));
      NimbusJwtDecoder accessTokenDecoder =
          NimbusJwtDecoder.withPublicKey((RSAPublicKey) accessKeyPair.getPublic()).build();

      NimbusJwtEncoder refreshTokenEncoder =
          new NimbusJwtEncoder(
              new ImmutableJWKSet<>(
                  new JWKSet(
                      new RSAKey.Builder((RSAPublicKey) refreshKeyPair.getPublic())
                          .privateKey((RSAPrivateCrtKey) refreshKeyPair.getPrivate())
                          .build())));
      long refreshTokenEexpiration = 3600;
      this.PROVIDER =
          new JwtTokenProvider(
              accessTokenEncoder,
              accessTokenDecoder,
              refreshTokenEncoder,
              this.EXPIRATION,
              refreshTokenEexpiration,
              "me",
              true);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  @Order(2)
  void generateToken() {
    final Authentication mockAuthentication = mock(Authentication.class);
    final Collection mockCollection = mock(Collection.class);
    when(mockAuthentication.getPrincipal())
        .thenReturn(new User(this.USERNAME, this.USERNAME, new ArrayList<>()));
    when(mockAuthentication.getAuthorities()).thenReturn(mockCollection);

    String result = this.PROVIDER.generateToken(mockAuthentication);

    assertThat(result).isNotEmpty();
    verify(mockCollection, times(1)).stream();

    String username = this.PROVIDER.extractUsername(result);
    assertThat(username).isEqualTo(this.USERNAME);

    Instant instant = this.PROVIDER.extractExpiration(result);
    assertThat(instant).isBefore(Instant.now().plus(this.EXPIRATION, ChronoUnit.SECONDS));

    boolean valid = this.PROVIDER.validateToken(result);
    assertThat(valid).isTrue();

    boolean notExpired = this.PROVIDER.isTokenExpired(result);
    assertThat(notExpired).isFalse();
  }

  @Test
  @Order(1)
  void generateRefreshToken() {
    final Authentication mockAuthentication = mock(Authentication.class);
    final Collection mockCollection = mock(Collection.class);
    when(mockAuthentication.getPrincipal())
        .thenReturn(new User(this.USERNAME, this.USERNAME, new ArrayList<>()));
    when(mockAuthentication.getAuthorities()).thenReturn(mockCollection);

    String result = this.PROVIDER.generateRefreshToken(mockAuthentication);
    assertThat(result).isNotEmpty();
    verify(mockCollection, never()).stream();

    BadJwtException exception =
        assertThrows(BadJwtException.class, () -> this.PROVIDER.extractUsername(result));
    assertThat(exception).isNotNull().matches(e -> e.getMessage().contains("Invalid signature"));

    BadJwtException exception1 =
        assertThrows(BadJwtException.class, () -> this.PROVIDER.extractExpiration(result));
    assertThat(exception1).isNotNull().matches(e -> e.getMessage().contains("Invalid signature"));
  }
}
