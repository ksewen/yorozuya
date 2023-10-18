package com.github.ksewen.yorozuya.common.context;

import java.util.Map;
import javax.annotation.Nullable;

/**
 * @author ksewen
 * @date 13.10.2023 11:03
 */
public interface Context {

  /**
   * Put the {@code value} with the {@code key} in the context. If the context contained a mapping
   * for the {@code key}, the old value is replaced by the {@code value}.
   *
   * @param key must not be null.
   * @param value must not be null.
   * @return
   */
  void put(String key, String value);

  /**
   * Set multiple key-values with {@code map} in the context. If the context contained a mapping of
   * the {@code key}, the old value is replaced by the {@code value}.
   *
   * @param map must not be null.
   * @return
   */
  void putAll(Map<String, String> map);

  /**
   * Get the value of key from the context.
   *
   * @param key must not be null.
   * @return null when the mapping doesn't exist in the context.
   */
  @Nullable
  String get(String key);

  /**
   * Remove the key-value from the context.
   *
   * @param key
   * @return
   */
  void remove(String key);

  /**
   * Get all mappings in the context as a Map
   *
   * @param
   * @return
   */
  Map<String, String> getContext();

  /**
   * Determine if given {@code key} exists.
   *
   * @param key
   * @return
   */
  boolean contains(String key);

  /**
   * Clean up the current context.
   *
   * @param
   * @return
   */
  void shutdown();
}
