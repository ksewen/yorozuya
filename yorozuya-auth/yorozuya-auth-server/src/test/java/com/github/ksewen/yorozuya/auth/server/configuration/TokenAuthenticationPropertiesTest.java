package com.github.ksewen.yorozuya.auth.server.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.github.ksewen.yorozuya.auth.server.annotation.EnableAuthServer;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ksewen
 * @date 29.11.2023 16:51
 */
@EnableAuthServer
@SpringBootTest(
    classes = {
      TokenAuthenticationAutoConfigurationTest.MockKeyPairManagerAutoConfiguration.class,
      TokenAuthenticationAutoConfigurationTest.MockJwtEncoderAutoConfiguration.class,
      TokenAuthenticationAutoConfigurationTest.MockJwtDecoderAutoConfiguration.class,
      TokenAuthenticationAutoConfiguration.class
    },
    properties = {
      "security.token.expiration=PT5H",
      "security.token.read-header=Authentication",
      "security.token.issuer=tester",
      "security.token.access-public-key=accessPublic",
      "security.token.access-private-key=accessPrivate",
      "security.token.refresh-public-key=refreshPublic",
      "security.token.refresh-private-key=refreshPrivate",
      "security.token.token-prefix=TestBearer ",
      "security.token.debug=true"
    })
public class TokenAuthenticationPropertiesTest {

  @Autowired private TokenAuthenticationProperties properties;

  @Test
  void getExpiration() {
    assertThat(this.properties.getExpiration()).isEqualTo(Duration.ofHours(5));
  }

  @Test
  void getRefreshTokenExpiration() {
    assertThat(this.properties.getRefreshTokenExpiration()).isEqualTo(Duration.ofDays(7));
  }

  @Test
  void getReadHeader() {
    assertThat(this.properties.getReadHeader()).isEqualTo("Authentication");
  }

  @Test
  void getTokenPrefix() {
    assertThat(this.properties.getTokenPrefix()).isEqualTo("TestBearer ");
  }

  @Test
  void getIssuer() {
    assertThat(this.properties.getIssuer()).isEqualTo("tester");
  }

  @Test
  void getAccessPublicKey() {
    assertThat(this.properties.getAccessPublicKey()).isEqualTo("accessPublic");
  }

  @Test
  void getAccessPrivateKey() {
    assertThat(this.properties.getAccessPrivateKey()).isEqualTo("accessPrivate");
  }

  @Test
  void getRefreshPublicKey() {
    assertThat(this.properties.getRefreshPublicKey()).isEqualTo("refreshPublic");
  }

  @Test
  void getRefreshPrivateKey() {
    assertThat(this.properties.getRefreshPrivateKey()).isEqualTo("refreshPrivate");
  }

  @Test
  void isWithAuthorities() {
    assertThat(this.properties.isWithAuthorities()).isFalse();
  }

  @Test
  void isDebug() {
    assertThat(this.properties.isDebug()).isTrue();
  }
}
