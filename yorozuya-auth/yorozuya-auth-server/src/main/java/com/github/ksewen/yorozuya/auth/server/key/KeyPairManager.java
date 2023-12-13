package com.github.ksewen.yorozuya.auth.server.key;

import java.security.KeyPair;

/**
 * @author ksewen
 * @date 29.11.2023 16:29
 */
public interface KeyPairManager {

  /**
   * Generate a key-pair.
   *
   * @return
   */
  KeyPair getAccessTokenKeyPair();

  KeyPair getRefreshTokenKeyPair();
}
