package com.github.ksewen.yorozuya.auth.server.configuration;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ksewen
 * @date 24.11.2023 17:20
 */
@Data
@ConfigurationProperties(prefix = "security.token")
public class TokenAuthenticationProperties {

  private Duration expiration = Duration.ofHours(12);

  private Duration refreshTokenExpiration = Duration.ofDays(7);

  private String readHeader = "Authentication";

  private String tokenPrefix = "Bearer ";

  private String issuer = "me";

  private String accessPublicKey;

  private String accessPrivateKey;

  private String refreshPublicKey;

  private String refreshPrivateKey;

  private boolean withAuthorities = Boolean.FALSE;

  private boolean debug = Boolean.FALSE;
}
