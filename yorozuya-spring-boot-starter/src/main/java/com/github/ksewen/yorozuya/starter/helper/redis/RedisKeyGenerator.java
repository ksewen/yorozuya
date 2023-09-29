package com.github.ksewen.yorozuya.starter.helper.redis;
/**
 * @author ksewen
 * @date 24.09.2023 22:08
 */
public interface RedisKeyGenerator {

  /**
   * Generate a standardized key.
   *
   * @param key must not be null.
   * @return
   */
  default String generate(String key) {
    return key;
  }

  /**
   * Choose whether to generate a key.
   *
   * @return If it's necessary to generate a key, return {@literal true}
   */
  default boolean needGenerate() {
    return Boolean.FALSE;
  }
}
