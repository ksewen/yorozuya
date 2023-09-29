package com.github.ksewen.yorozuya.starter.helper.redis.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import com.github.ksewen.yorozuya.common.exception.CommonException;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import com.github.ksewen.yorozuya.starter.helper.redis.RedisHelperProperties;
import com.github.ksewen.yorozuya.starter.helper.redis.RedisHelpers;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

/**
 * @author ksewen
 * @date 27.09.2023 20:31
 */
@SpringBootTest(classes = RedisCommonHelpers.class)
public class RedisHelpersForHashImplTest extends RedisCommonHelpersImplConfigurations {

  @Autowired private RedisHelpers redisHelpers;

  @MockBean private StringRedisTemplate redisTemplate;

  @MockBean private HashOperations<String, String, String> hashOperations;

  @MockBean private RedisHelperProperties properties;

  @MockBean private JsonHelpers jsonHelpers;

  private final String HASH_KEY = "hashKey";

  @BeforeEach
  void beforeEach() {
    when(this.redisTemplate.<String, String>opsForHash()).thenReturn(this.hashOperations);
    when(this.properties.getKeyPrefix()).thenReturn(this.KEY_PREFIX);
    when(this.properties.getKeySplit()).thenReturn(this.KEY_SPLIT);
    when(this.jsonHelpers.toJsonString(any(TestObject.class))).thenReturn(this.JSON);
  }

  @Test
  void hashSet() {
    this.redisHelpers.hashSet(this.KEY, this.HASH_KEY, this.VALUE);
    verify(this.redisTemplate, times(1)).opsForHash();
    verify(this.hashOperations, times(1))
        .put(this.EXPECT_PREFIX + this.KEY, this.HASH_KEY, this.JSON);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toJsonString(any(RedisCommonHelpersImplConfigurations.TestObject.class));
  }

  @Test
  void hashSetAll() {
    this.redisHelpers.hashSetAll(this.KEY, this.KEY_MAP);
    verify(this.redisTemplate, times(1)).opsForHash();
    verify(this.hashOperations, times(1))
        .putAll(
            argThat((this.EXPECT_PREFIX + this.KEY)::equals),
            argThat(m -> !CollectionUtils.isEmpty(m) && m.size() == this.KEY_MAP.size()));
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(3))
        .toJsonString(any(RedisCommonHelpersImplConfigurations.TestObject.class));
  }

  @Test
  void hashSetAllWithEmptyMap() {
    this.redisHelpers.hashSetAll(this.KEY, null);
    verify(this.redisTemplate, never()).opsForHash();
    verify(this.hashOperations, never()).putAll(anyString(), anyMap());
    verify(this.properties, never()).getKeyPrefix();
    verify(this.properties, never()).getKeySplit();
    verify(this.jsonHelpers, never()).toJsonString(any());
  }

  @Test
  void hashHasKey() {
    when(this.hashOperations.hasKey(this.EXPECT_PREFIX + this.KEY, this.HASH_KEY))
        .thenReturn(Boolean.TRUE);
    boolean test = this.redisHelpers.hashHasKey(this.KEY, this.HASH_KEY);
    assertThat(test).isTrue();
    verify(this.redisTemplate, times(1)).opsForHash();
    verify(this.hashOperations, times(1)).hasKey(this.EXPECT_PREFIX + this.KEY, this.HASH_KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void hashHasKeyWithInvalidOperation() {
    when(this.hashOperations.hasKey(anyString(), anyString())).thenReturn(null);
    CommonException exception =
        assertThrows(
            CommonException.class, () -> this.redisHelpers.hashHasKey(this.KEY, this.HASH_KEY));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForHash();
    verify(this.hashOperations, times(1)).hasKey(this.EXPECT_PREFIX + this.KEY, this.HASH_KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void hashDelete() {
    when(this.hashOperations.delete(
            this.EXPECT_PREFIX + this.KEY, this.HASH_KEY + 1, this.HASH_KEY + 2))
        .thenReturn(2L);
    long test = this.redisHelpers.hashDelete(this.KEY, this.HASH_KEY + 1, this.HASH_KEY + 2);
    assertThat(test).isEqualTo(2L);
    verify(this.redisTemplate, times(1)).opsForHash();
    verify(this.hashOperations, times(1))
        .delete(this.EXPECT_PREFIX + this.KEY, this.HASH_KEY + 1, this.HASH_KEY + 2);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void hashDeleteWithInvalidOperation() {
    when(this.hashOperations.delete(anyString(), anyString())).thenReturn(null);
    CommonException exception =
        assertThrows(
            CommonException.class, () -> this.redisHelpers.hashDelete(this.KEY, this.HASH_KEY));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForHash();
    verify(this.hashOperations, times(1)).delete(this.EXPECT_PREFIX + this.KEY, this.HASH_KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void hashGet() {
    when(this.hashOperations.get(this.EXPECT_PREFIX + this.KEY, this.HASH_KEY))
        .thenReturn(this.JSON);
    when(this.jsonHelpers.toObject(this.JSON, TestObject.class))
        .thenReturn(TestObject.builder().id(1L).name(this.NAME).build());
    TestObject result = this.redisHelpers.hashGet(this.KEY, this.HASH_KEY, TestObject.class);
    assertThat(result)
        .isNotNull()
        .matches(t -> t.getId() == 1L)
        .matches(t -> this.NAME.equals(t.getName()));
    verify(this.redisTemplate, times(1)).opsForHash();
    verify(this.hashOperations, times(1)).get(this.EXPECT_PREFIX + this.KEY, this.HASH_KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1)).toObject(this.JSON, TestObject.class);
  }

  @Test
  void hashMultiGet() {
    when(this.hashOperations.multiGet(
            argThat((this.EXPECT_PREFIX + this.KEY)::equals),
            argThat(
                ks ->
                    ks.size() == this.JSON_LIST.size()
                        && ks.stream().anyMatch("a"::equals)
                        && ks.stream().anyMatch("b"::equals)
                        && ks.stream().anyMatch("c"::equals))))
        .thenReturn(this.JSON_LIST);
    when(this.jsonHelpers.toObject(
            this.JSON_LIST.get(0), RedisCommonHelpersImplConfigurations.TestObject.class))
        .thenReturn(
            RedisCommonHelpersImplConfigurations.TestObject.builder()
                .id(1L)
                .name(this.NAME + 1)
                .build());
    when(this.jsonHelpers.toObject(
            this.JSON_LIST.get(1), RedisCommonHelpersImplConfigurations.TestObject.class))
        .thenReturn(
            RedisCommonHelpersImplConfigurations.TestObject.builder()
                .id(2L)
                .name(this.NAME + 2)
                .build());
    when(this.jsonHelpers.toObject(
            this.JSON_LIST.get(2), RedisCommonHelpersImplConfigurations.TestObject.class))
        .thenReturn(
            RedisCommonHelpersImplConfigurations.TestObject.builder()
                .id(3L)
                .name(this.NAME + 3)
                .build());
    List<TestObject> results =
        this.redisHelpers.hashMultiGet(
            this.KEY, this.KEYS, RedisCommonHelpersImplConfigurations.TestObject.class);
    assertThat(results)
        .isNotEmpty()
        .hasSize(3)
        .matches(
            l -> l.stream().anyMatch(i -> i.getId() == 1L && i.getName().equals(this.NAME + 1)))
        .matches(
            l -> l.stream().anyMatch(i -> i.getId() == 2L && i.getName().equals(this.NAME + 2)))
        .matches(
            l -> l.stream().anyMatch(i -> i.getId() == 3L && i.getName().equals(this.NAME + 3)));
    verify(this.redisTemplate, times(1)).opsForHash();
    verify(this.hashOperations, times(1))
        .multiGet(argThat((this.EXPECT_PREFIX + this.KEY)::equals), anyCollection());
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toObject(this.JSON_LIST.get(0), RedisCommonHelpersImplConfigurations.TestObject.class);
    verify(this.jsonHelpers, times(1))
        .toObject(this.JSON_LIST.get(1), RedisCommonHelpersImplConfigurations.TestObject.class);
    verify(this.jsonHelpers, times(1))
        .toObject(this.JSON_LIST.get(2), RedisCommonHelpersImplConfigurations.TestObject.class);
  }

  @Test
  void hashMultiGetWithEmptyHashKeys() {
    List<RedisCommonHelpersImplConfigurations.TestObject> result =
        this.redisHelpers.hashMultiGet(
            this.KEY, null, RedisCommonHelpersImplConfigurations.TestObject.class);
    assertThat(result).isNotNull().isEmpty();
    verify(this.redisTemplate, never()).opsForHash();
    verify(this.hashOperations, never()).multiGet(anyString(), anyCollection());
    verify(this.properties, never()).getKeyPrefix();
    verify(this.properties, never()).getKeySplit();
    verify(this.jsonHelpers, never()).toObject(anyString(), ArgumentMatchers.<Class<?>>any());
  }

  @Test
  void hashSize() {
    when(this.hashOperations.size(this.EXPECT_PREFIX + this.KEY)).thenReturn(5L);
    long test = this.redisHelpers.hashSize(this.KEY);
    assertThat(test).isEqualTo(5);
    verify(this.redisTemplate, times(1)).opsForHash();
    verify(this.hashOperations, times(1)).size(this.EXPECT_PREFIX + this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void hashSizeWithInvalidOperation() {
    when(this.hashOperations.size(anyString())).thenReturn(null);
    CommonException exception =
        assertThrows(CommonException.class, () -> this.redisHelpers.hashSize(this.KEY));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForHash();
    verify(this.hashOperations, times(1)).size(this.EXPECT_PREFIX + this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }
}
