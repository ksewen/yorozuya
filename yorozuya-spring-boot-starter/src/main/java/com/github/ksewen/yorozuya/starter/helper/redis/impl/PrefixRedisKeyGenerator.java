package com.github.ksewen.yorozuya.starter.helper.redis.impl;

import com.github.ksewen.yorozuya.common.constant.SystemConstants;
import com.github.ksewen.yorozuya.starter.helper.redis.RedisHelperProperties;
import com.github.ksewen.yorozuya.starter.helper.redis.RedisKeyGenerator;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 24.09.2023 22:26
 */
@RequiredArgsConstructor
public class PrefixRedisKeyGenerator implements RedisKeyGenerator {

  private final RedisHelperProperties properties;

  @Override
  public String generate(String key) {
    if (!needGenerate()) {
      return key;
    }
    StringBuilder builder = new StringBuilder(this.properties.getKeyPrefix());
    builder.append(this.properties.getKeySplit());
    builder.append(key);
    return builder.toString();
  }

  @Override
  public boolean needGenerate() {
    return SystemConstants.PROPERTIES_NOT_SET_VALUE.equals(this.properties.getKeyPrefix())
        ? Boolean.FALSE
        : Boolean.TRUE;
  }
}
