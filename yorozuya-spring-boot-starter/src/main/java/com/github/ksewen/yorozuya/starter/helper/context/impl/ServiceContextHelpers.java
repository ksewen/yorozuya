package com.github.ksewen.yorozuya.starter.helper.context.impl;

import com.github.ksewen.yorozuya.common.context.Context;
import com.github.ksewen.yorozuya.starter.helper.context.ContextHelpers;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 15.10.2023 17:56
 */
@RequiredArgsConstructor
public class ServiceContextHelpers implements ContextHelpers {

  private final Context context;

  private final String prefix;

  @Override
  public boolean hasKey(String key) {
    return this.context.contains(this.generateKey(key));
  }

  @Override
  public String get(String key) {
    return this.context.get(this.generateKey(key));
  }

  @Override
  public void put(String key, String value) {
    this.context.put(this.generateKey(key), value);
  }

  @Override
  public void remove(String key) {
    this.context.remove(this.generateKey(key));
  }

  private String generateKey(String key) {
    return prefix + key;
  }
}
