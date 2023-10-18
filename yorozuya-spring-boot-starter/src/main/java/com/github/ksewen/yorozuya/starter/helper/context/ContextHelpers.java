package com.github.ksewen.yorozuya.starter.helper.context;
/**
 * @author ksewen
 * @date 15.10.2023 17:56
 */
public interface ContextHelpers {

  /**
   * Determine if given {@code key} exists.
   *
   * @param key must be not {@literal null}
   * @return
   */
  boolean hasKey(String key);

  /**
   * Get the value of key.
   *
   * @param key must be not {@literal null}
   * @return
   */
  String get(String key);

  /**
   * Set {@code value} for {@code key}.
   *
   * @param key must be not {@literal null}
   * @param value must be not {@literal null}
   */
  void put(String key, String value);

  /**
   * Remove key-value by given {@code key}.
   *
   * @param key must be not {@literal null}
   */
  void remove(String key);
}
