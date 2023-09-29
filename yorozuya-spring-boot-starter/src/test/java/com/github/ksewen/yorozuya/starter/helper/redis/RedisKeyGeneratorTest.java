package com.github.ksewen.yorozuya.starter.helper.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author ksewen
 * @date 25.09.2023 22:10
 */
class RedisKeyGeneratorTest {

  private RedisKeyGenerator redisKeyGenerator = new MockRedisKeyGenerator();

  @Test
  void generate() {
    final String key = "test";
    String result = this.redisKeyGenerator.generate(key);
    assertThat(result).isEqualTo(key);
  }

  @Test
  void needGenerate() {
    boolean result = this.redisKeyGenerator.needGenerate();
    assertThat(result).isFalse();
  }

  class MockRedisKeyGenerator implements RedisKeyGenerator {}
}
