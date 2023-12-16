package com.github.ksewen.yorozuya.auth.server.token.impl;

import com.github.ksewen.yorozuya.auth.server.token.TokenProvider;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

/**
 * @author ksewen
 * @date 03.12.2023 22:57
 */
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenProvider {

  private final JwtEncoder accessTokenEncoder;

  private final JwtDecoder accessTokenDecoder;

  private final JwtEncoder refreshTokenEncoder;

  private final long EXPIRATION;

  private final long REFRESH_TOKEN_EXPIRATION;

  private final String ISSUER;

  private final boolean WITH_AUTHORITIES;

  public static final String AUTHORITIES_KEY = "authorities";

  @Override
  public String generateToken(Authentication authentication) {
    JwtClaimsSet.Builder builder = this.tokenBuilder(authentication, this.EXPIRATION);
    if (WITH_AUTHORITIES) {
      List<String> authorities =
          authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
      builder.claim(JwtTokenProvider.AUTHORITIES_KEY, authorities);
    }
    return accessTokenEncoder.encode(JwtEncoderParameters.from(builder.build())).getTokenValue();
  }

  @Override
  public String generateRefreshToken(Authentication authentication) {
    JwtClaimsSet.Builder builder = this.tokenBuilder(authentication, this.REFRESH_TOKEN_EXPIRATION);
    return refreshTokenEncoder.encode(JwtEncoderParameters.from(builder.build())).getTokenValue();
  }

  @Override
  public String extractUsername(String token) {
    return this.accessTokenDecoder.decode(token).getSubject();
  }

  @Override
  public Instant extractExpiration(String token) {
    return this.accessTokenDecoder.decode(token).getExpiresAt();
  }

  @Override
  public boolean validateToken(String token) {
    this.accessTokenDecoder.decode(token);
    return !isTokenExpired(token);
  }

  @Override
  public boolean isTokenExpired(String token) {
    return this.extractExpiration(token).isBefore(Instant.now());
  }

  private JwtClaimsSet.Builder tokenBuilder(Authentication authentication, long expiration) {
    User userPrincipal = (User) authentication.getPrincipal();
    Instant now = Instant.now();

    return JwtClaimsSet.builder()
        .subject(userPrincipal.getUsername())
        .issuer(this.ISSUER)
        .issuedAt(now)
        .expiresAt(now.plus(expiration, ChronoUnit.SECONDS));
  }
}
