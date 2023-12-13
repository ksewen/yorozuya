package com.github.ksewen.yorozuya.auth.server.key;

import com.github.ksewen.yorozuya.auth.server.configuration.TokenAuthenticationProperties;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ksewen
 * @date 29.11.2023 16:34
 */
@RequiredArgsConstructor
@Slf4j
public class PropertiesRSAKeyPairManager implements KeyPairManager {

  private final TokenAuthenticationProperties properties;

  @Override
  public KeyPair getAccessTokenKeyPair() {
    try {
      return this.getKeyPair(
          this.properties.getAccessPrivateKey(), this.properties.getAccessPublicKey());
    } catch (NoSuchAlgorithmException e) {
      throw this.handleNoSuchAlgorithmException(e);
    } catch (InvalidKeySpecException e) {
      throw this.handleInvalidKeySpecException(e);
    }
  }

  @Override
  public KeyPair getRefreshTokenKeyPair() {
    try {
      return this.getKeyPair(
          this.properties.getRefreshPrivateKey(), this.properties.getRefreshPublicKey());
    } catch (NoSuchAlgorithmException e) {
      throw this.handleNoSuchAlgorithmException(e);
    } catch (InvalidKeySpecException e) {
      throw this.handleInvalidKeySpecException(e);
    }
  }

  private KeyPair getKeyPair(String privateKeyContent, String publicKeyContent)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeyFactory kf = KeyFactory.getInstance("RSA");

    PKCS8EncodedKeySpec keySpecPKCS8 =
        new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
    PrivateKey privateKey = kf.generatePrivate(keySpecPKCS8);

    X509EncodedKeySpec keySpecX509 =
        new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
    PublicKey publicKey = kf.generatePublic(keySpecX509);
    return new KeyPair(publicKey, privateKey);
  }

  private RuntimeException handleNoSuchAlgorithmException(Exception cause) {
    log.error("The given algorithm is requested but is not available in the environment.", cause);
    return new RuntimeException(cause);
  }

  private RuntimeException handleInvalidKeySpecException(Exception cause) {
    log.error("Invalid key specification for key.", cause);
    return new RuntimeException(cause);
  }
}
