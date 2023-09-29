package com.github.ksewen.yorozuya.starter.helper.redis.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.github.ksewen.yorozuya.common.constant.SystemConstants;
import com.github.ksewen.yorozuya.starter.helper.redis.RedisHelperProperties;
import com.github.ksewen.yorozuya.starter.helper.redis.RedisKeyGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author ksewen
 * @date 27.09.2023 17:45
 */
@SpringBootTest(classes = PrefixRedisKeyGenerator.class)
class PrefixRedisKeyGeneratorTest {

  @Autowired private RedisKeyGenerator redisKeyGenerator;

  @MockBean private RedisHelperProperties properties;

  private final String KEY_PREFIX = "test";

  private final String KEY = "key";

  private final String KEY_SPLIT = ":";

  @Test
  void generate() {
    when(this.properties.getKeyPrefix()).thenReturn(this.KEY_PREFIX);
    when(this.properties.getKeySplit()).thenReturn(this.KEY_SPLIT);
    String key = this.redisKeyGenerator.generate(this.KEY);
    assertThat(key).isNotNull().isEqualTo(this.KEY_PREFIX + this.KEY_SPLIT + this.KEY);
  }

  @Test
  void notGenerate() {
    when(this.properties.getKeyPrefix()).thenReturn(SystemConstants.PROPERTIES_NOT_SET_VALUE);
    when(this.properties.getKeySplit()).thenReturn(this.KEY_SPLIT);
    String key = this.redisKeyGenerator.generate(this.KEY);
    assertThat(key).isNotNull().isEqualTo(this.KEY);
  }

  @Test
  void needGenerate() {
    when(this.properties.getKeyPrefix()).thenReturn(this.KEY_PREFIX);
    boolean result = this.redisKeyGenerator.needGenerate();
    assertThat(result).isTrue();
  }

  @Test
  void notNeedGenerate() {
    when(this.properties.getKeyPrefix()).thenReturn(SystemConstants.PROPERTIES_NOT_SET_VALUE);
    boolean result = this.redisKeyGenerator.needGenerate();
    assertThat(result).isFalse();
  }
}
