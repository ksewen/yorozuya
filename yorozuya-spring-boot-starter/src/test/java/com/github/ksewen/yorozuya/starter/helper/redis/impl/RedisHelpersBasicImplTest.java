package com.github.ksewen.yorozuya.starter.helper.redis.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import com.github.ksewen.yorozuya.common.exception.CommonException;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import com.github.ksewen.yorozuya.starter.helper.redis.RedisHelperProperties;
import com.github.ksewen.yorozuya.starter.helper.redis.RedisHelpers;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author ksewen
 * @date 25.09.2023 12:15
 */
@SpringBootTest(classes = RedisCommonHelpers.class)
class RedisHelpersBasicImplTest extends RedisCommonHelpersImplConfigurations {

  @Autowired private RedisHelpers redisHelpers;

  @MockBean private StringRedisTemplate redisTemplate;

  @MockBean private RedisHelperProperties properties;

  @SuppressWarnings("unused")
  @MockBean
  private JsonHelpers jsonHelpers;

  private final Date NOW = new Date();

  @BeforeEach
  void beforeEach() {
    when(this.properties.getKeyPrefix()).thenReturn(this.KEY_PREFIX);
    when(this.properties.getKeySplit()).thenReturn(this.KEY_SPLIT);
  }

  @Test
  void hasKey() {
    when(this.redisTemplate.hasKey(anyString())).thenReturn(Boolean.TRUE);
    boolean test = this.redisHelpers.hasKey(this.KEY);
    assertThat(test).isTrue();
    verify(this.redisTemplate, times(1)).hasKey(this.EXPECT_PREFIX + this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void hasKeyWithInvalidOperation() {
    when(this.redisTemplate.hasKey(anyString())).thenReturn(null);
    CommonException exception =
        assertThrows(CommonException.class, () -> this.redisHelpers.hasKey(this.KEY));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).hasKey(this.EXPECT_PREFIX + this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void expire() {
    when(this.redisTemplate.expire(anyString(), any(Duration.class))).thenReturn(Boolean.TRUE);
    boolean test = this.redisHelpers.expire(this.KEY, this.TIMEOUT);
    assertThat(test).isTrue();
    verify(this.redisTemplate, times(1))
        .expire(
            argThat((this.EXPECT_PREFIX + this.KEY)::equals), argThat(d -> this.TIMEOUT.equals(d)));
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void expireWithInvalidTimeout() {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> this.redisHelpers.expire(this.KEY, Duration.ZERO));
    assertThat(exception).matches(e -> "timeout must be a positive number".equals(e.getMessage()));
    verify(this.redisTemplate, times(0)).expire(anyString(), any(Duration.class));
    verify(this.properties, times(0)).getKeyPrefix();
    verify(this.properties, times(0)).getKeySplit();
  }

  @Test
  void expireWithInvalidOperation() {
    when(this.redisTemplate.expire(anyString(), any(Duration.class))).thenReturn(null);
    CommonException exception =
        assertThrows(CommonException.class, () -> this.redisHelpers.expire(this.KEY, this.TIMEOUT));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1))
        .expire(
            argThat((this.EXPECT_PREFIX + this.KEY)::equals), argThat(d -> this.TIMEOUT.equals(d)));
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void expireAt() {
    when(this.redisTemplate.expireAt(anyString(), any(Date.class))).thenReturn(Boolean.TRUE);
    boolean test = this.redisHelpers.expireAt(this.KEY, this.NOW);
    assertThat(test).isTrue();
    verify(this.redisTemplate, times(1))
        .expireAt(
            argThat((this.EXPECT_PREFIX + this.KEY)::equals), (Date) argThat(this.NOW::equals));
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void expireAtWithInvalidOperation() {
    when(this.redisTemplate.expireAt(anyString(), any(Date.class))).thenReturn(null);
    CommonException exception =
        assertThrows(CommonException.class, () -> this.redisHelpers.expireAt(this.KEY, this.NOW));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1))
        .expireAt(
            argThat((this.EXPECT_PREFIX + this.KEY)::equals), (Date) argThat(this.NOW::equals));
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void getExpire() {
    when(this.redisTemplate.getExpire(anyString())).thenReturn(100L);
    long test = this.redisHelpers.getExpire(this.KEY);
    assertThat(test).isEqualTo(100L);
    verify(this.redisTemplate, times(1))
        .getExpire(argThat((this.EXPECT_PREFIX + this.KEY)::equals));
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void getExpireWithInvalidOperation() {
    when(this.redisTemplate.getExpire(anyString())).thenReturn(null);
    CommonException exception =
        assertThrows(CommonException.class, () -> this.redisHelpers.getExpire(this.KEY));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1))
        .getExpire(argThat((this.EXPECT_PREFIX + this.KEY)::equals));
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void delete() {
    when(this.redisTemplate.delete(anyString())).thenReturn(Boolean.TRUE);
    boolean test = this.redisHelpers.delete(this.KEY);
    assertThat(test).isTrue();
    verify(this.redisTemplate, times(1))
        .delete((String) argThat((this.EXPECT_PREFIX + this.KEY)::equals));
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void deleteWithEmptyKeys() {
    long test = this.redisHelpers.delete(new ArrayList<>());
    assertThat(test).isEqualTo(0L);
    verify(this.redisTemplate, times(0)).delete(anyString());
    verify(this.properties, times(0)).getKeyPrefix();
    verify(this.properties, times(0)).getKeySplit();
  }

  @Test
  void deleteWithInvalidOperation() {
    when(this.redisTemplate.delete(anyString())).thenReturn(null);
    CommonException exception =
        assertThrows(CommonException.class, () -> this.redisHelpers.delete(this.KEY));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1))
        .delete((String) argThat((this.EXPECT_PREFIX + this.KEY)::equals));
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void testDelete() {
    when(this.redisTemplate.delete(anyCollection())).thenReturn(3L);
    long test = this.redisHelpers.delete(this.KEYS);
    assertThat(test).isEqualTo(3L);
    verify(this.redisTemplate, times(1)).delete(anyCollection());
    verify(this.properties, times(6)).getKeyPrefix();
    verify(this.properties, times(3)).getKeySplit();
  }

  @Test
  void testDeleteWithInvalidOperation() {
    when(this.redisTemplate.delete(anyCollection())).thenReturn(null);
    CommonException exception =
        assertThrows(CommonException.class, () -> this.redisHelpers.delete(this.KEYS));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).delete(anyCollection());
    verify(this.properties, times(6)).getKeyPrefix();
    verify(this.properties, times(3)).getKeySplit();
  }

  @Test
  void deleteByPrefix() {
    when(this.redisTemplate.keys(anyString()))
        .thenReturn(
            Stream.of(
                    this.EXPECT_PREFIX + this.KEY + "a",
                    this.EXPECT_PREFIX + this.KEY + "b",
                    this.EXPECT_PREFIX + this.KEY + "c")
                .collect(Collectors.toSet()));
    when(this.redisTemplate.delete(anyCollection())).thenReturn(3L);
    long test = this.redisHelpers.deleteByPrefix(this.KEY);
    assertThat(test).isEqualTo(3L);
    verify(this.redisTemplate, times(1)).delete(anyCollection());
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void deleteByPrefixWithEmptyResult() {
    when(this.redisTemplate.keys(anyString())).thenReturn(null);
    long test = this.redisHelpers.deleteByPrefix(this.KEY);
    assertThat(test).isEqualTo(0L);
    verify(this.redisTemplate, times(0)).delete(anyCollection());
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void deleteByPrefixWithInvalidOperation() {
    when(this.redisTemplate.keys(anyString()))
        .thenReturn(Stream.of(this.EXPECT_PREFIX + this.KEY + "a").collect(Collectors.toSet()));
    when(this.redisTemplate.delete(anyCollection())).thenReturn(null);
    CommonException exception =
        assertThrows(CommonException.class, () -> this.redisHelpers.deleteByPrefix(this.KEY));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).delete(anyCollection());
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }
}
