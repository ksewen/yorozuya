package com.github.ksewen.yorozuya.auth.server.token;

import java.time.Instant;
import org.springframework.security.core.Authentication;

/**
 * @author ksewen
 * @date 23.11.2023 08:52
 */
public interface TokenProvider {

  /**
   * Generate token string.
   *
   * @param authentication
   * @return
   */
  String generateToken(Authentication authentication);

  /**
   * Generate refresh token string.
   *
   * @param authentication
   * @return
   */
  String generateRefreshToken(Authentication authentication);

  /**
   * Extract username from token string.
   *
   * @param token
   * @return
   */
  String extractUsername(String token);

  /**
   * Extract expiration from token string.
   *
   * @param token
   * @return
   */
  Instant extractExpiration(String token);

  /**
   * Validate token value.
   *
   * @param token
   * @return true if token is valid
   */
  boolean validateToken(String token);

  /**
   * Verify whether the token has expired
   *
   * @param token
   * @return false if the token is expired
   */
  boolean isTokenExpired(String token);
}
