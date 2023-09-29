package com.github.ksewen.yorozuya.starter.helper.redis.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import com.github.ksewen.yorozuya.common.exception.CommonException;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import com.github.ksewen.yorozuya.starter.helper.redis.RedisHelperProperties;
import com.github.ksewen.yorozuya.starter.helper.redis.RedisHelpers;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;

/**
 * @author ksewen
 * @date 27.09.2023 20:20
 */
@SpringBootTest(classes = RedisCommonHelpers.class)
public class RedisHelpersForValueImplTest extends RedisCommonHelpersImplConfigurations {

  @Autowired private RedisHelpers redisHelpers;

  @MockBean private StringRedisTemplate redisTemplate;

  @MockBean private ValueOperations valueOperations;

  @MockBean private RedisHelperProperties properties;

  @MockBean private JsonHelpers jsonHelpers;

  @BeforeEach
  void beforeEach() {
    when(this.redisTemplate.opsForValue()).thenReturn(this.valueOperations);
    when(this.properties.getKeyPrefix()).thenReturn(this.KEY_PREFIX);
    when(this.properties.getKeySplit()).thenReturn(this.KEY_SPLIT);
    when(this.jsonHelpers.toJsonString(any(TestObject.class))).thenReturn(this.JSON);
  }

  @Test
  void set() {
    this.redisHelpers.set(this.KEY, this.VALUE);
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).set(this.EXPECT_PREFIX + this.KEY, this.JSON);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toJsonString(any(RedisCommonHelpersImplConfigurations.TestObject.class));
  }

  @Test
  void testSet() {
    this.redisHelpers.set(this.KEY, this.VALUE, this.TIMEOUT);
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1))
        .set(this.EXPECT_PREFIX + this.KEY, this.JSON, this.TIMEOUT);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toJsonString(any(RedisCommonHelpersImplConfigurations.TestObject.class));
  }

  @Test
  void setIfAbsent() {
    when(this.valueOperations.setIfAbsent(this.EXPECT_PREFIX + this.KEY, this.JSON))
        .thenReturn(Boolean.TRUE);
    boolean test = this.redisHelpers.setIfAbsent(this.KEY, this.VALUE);
    assertThat(test).isTrue();
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).setIfAbsent(this.EXPECT_PREFIX + this.KEY, this.JSON);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toJsonString(any(RedisCommonHelpersImplConfigurations.TestObject.class));
  }

  @Test
  void setIfAbsentWithInvalidOperation() {
    when(this.valueOperations.setIfAbsent(anyString(), anyString())).thenReturn(null);
    CommonException exception =
        assertThrows(
            CommonException.class, () -> this.redisHelpers.setIfAbsent(this.KEY, this.VALUE));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).setIfAbsent(this.EXPECT_PREFIX + this.KEY, this.JSON);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toJsonString(any(RedisCommonHelpersImplConfigurations.TestObject.class));
  }

  @Test
  void testSetIfAbsent() {
    when(this.valueOperations.setIfAbsent(this.EXPECT_PREFIX + this.KEY, this.JSON, this.TIMEOUT))
        .thenReturn(Boolean.TRUE);
    boolean test = this.redisHelpers.setIfAbsent(this.KEY, this.VALUE, this.TIMEOUT);
    assertThat(test).isTrue();
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1))
        .setIfAbsent(this.EXPECT_PREFIX + this.KEY, this.JSON, this.TIMEOUT);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toJsonString(any(RedisCommonHelpersImplConfigurations.TestObject.class));
  }

  @Test
  void testSetIfAbsentWithInvalidOperation() {
    when(this.valueOperations.setIfAbsent(anyString(), anyString(), any(Duration.class)))
        .thenReturn(null);
    CommonException exception =
        assertThrows(
            CommonException.class,
            () -> this.redisHelpers.setIfAbsent(this.KEY, this.VALUE, this.TIMEOUT));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1))
        .setIfAbsent(this.EXPECT_PREFIX + this.KEY, this.JSON, this.TIMEOUT);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toJsonString(any(RedisCommonHelpersImplConfigurations.TestObject.class));
  }

  @Test
  void setIfPresent() {
    when(this.valueOperations.setIfPresent(this.EXPECT_PREFIX + this.KEY, this.JSON))
        .thenReturn(Boolean.TRUE);
    boolean test = this.redisHelpers.setIfPresent(this.KEY, this.VALUE);
    assertThat(test).isTrue();
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).setIfPresent(this.EXPECT_PREFIX + this.KEY, this.JSON);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toJsonString(any(RedisCommonHelpersImplConfigurations.TestObject.class));
  }

  @Test
  void setIfPresentWithInvalidOperation() {
    when(this.valueOperations.setIfPresent(anyString(), anyString())).thenReturn(null);
    CommonException exception =
        assertThrows(
            CommonException.class, () -> this.redisHelpers.setIfPresent(this.KEY, this.VALUE));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).setIfPresent(this.EXPECT_PREFIX + this.KEY, this.JSON);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toJsonString(any(RedisCommonHelpersImplConfigurations.TestObject.class));
  }

  @Test
  void testSetIfPresent() {
    when(this.valueOperations.setIfPresent(this.EXPECT_PREFIX + this.KEY, this.JSON, this.TIMEOUT))
        .thenReturn(Boolean.TRUE);
    boolean test = this.redisHelpers.setIfPresent(this.KEY, this.VALUE, this.TIMEOUT);
    assertThat(test).isTrue();
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1))
        .setIfPresent(this.EXPECT_PREFIX + this.KEY, this.JSON, this.TIMEOUT);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toJsonString(any(RedisCommonHelpersImplConfigurations.TestObject.class));
  }

  @Test
  void testSetIfPresentWithInvalidOperation() {
    when(this.valueOperations.setIfPresent(anyString(), anyString(), any(Duration.class)))
        .thenReturn(null);
    CommonException exception =
        assertThrows(
            CommonException.class,
            () -> this.redisHelpers.setIfPresent(this.KEY, this.VALUE, this.TIMEOUT));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1))
        .setIfPresent(this.EXPECT_PREFIX + this.KEY, this.JSON, this.TIMEOUT);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toJsonString(any(RedisCommonHelpersImplConfigurations.TestObject.class));
  }

  @Test
  void multiSet() {
    this.redisHelpers.multiSet(this.KEY_MAP);
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1))
        .multiSet(argThat(m -> !CollectionUtils.isEmpty(m) && m.size() == this.KEY_MAP.size()));
    verify(this.properties, times(6)).getKeyPrefix();
    verify(this.properties, times(3)).getKeySplit();
    verify(this.jsonHelpers, times(3))
        .toJsonString(any(RedisCommonHelpersImplConfigurations.TestObject.class));
  }

  @Test
  void get() {
    when(this.valueOperations.get(this.EXPECT_PREFIX + this.KEY)).thenReturn(this.JSON);
    when(this.jsonHelpers.toObject(
            this.JSON, RedisCommonHelpersImplConfigurations.TestObject.class))
        .thenReturn(
            RedisCommonHelpersImplConfigurations.TestObject.builder()
                .id(1L)
                .name(this.NAME)
                .build());
    RedisCommonHelpersImplConfigurations.TestObject result =
        this.redisHelpers.get(this.KEY, RedisCommonHelpersImplConfigurations.TestObject.class);
    assertThat(result)
        .isNotNull()
        .matches(t -> t.getId() == 1L)
        .matches(t -> this.NAME.equals(t.getName()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).get(this.EXPECT_PREFIX + this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toObject(this.JSON, RedisCommonHelpersImplConfigurations.TestObject.class);
  }

  @Test
  void testGet() {
    TypeReference<RedisCommonHelpersImplConfigurations.TestObject> typeReference =
        new TypeReference<>() {};
    when(this.valueOperations.get(this.EXPECT_PREFIX + this.KEY)).thenReturn(this.JSON);
    when(this.jsonHelpers.toObject(this.JSON, typeReference))
        .thenReturn(
            RedisCommonHelpersImplConfigurations.TestObject.builder()
                .id(1L)
                .name(this.NAME)
                .build());
    RedisCommonHelpersImplConfigurations.TestObject result =
        this.redisHelpers.get(this.KEY, typeReference);
    assertThat(result)
        .isNotNull()
        .matches(t -> t.getId() == 1L)
        .matches(t -> this.NAME.equals(t.getName()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).get(this.EXPECT_PREFIX + this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1)).toObject(this.JSON, typeReference);
  }

  @Test
  void getAndDelete() {
    when(this.valueOperations.getAndDelete(this.EXPECT_PREFIX + this.KEY)).thenReturn(this.JSON);
    when(this.jsonHelpers.toObject(
            this.JSON, RedisCommonHelpersImplConfigurations.TestObject.class))
        .thenReturn(
            RedisCommonHelpersImplConfigurations.TestObject.builder()
                .id(1L)
                .name(this.NAME)
                .build());
    RedisCommonHelpersImplConfigurations.TestObject result =
        this.redisHelpers.getAndDelete(
            this.KEY, RedisCommonHelpersImplConfigurations.TestObject.class);
    assertThat(result)
        .isNotNull()
        .matches(t -> t.getId() == 1L)
        .matches(t -> this.NAME.equals(t.getName()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).getAndDelete(this.EXPECT_PREFIX + this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toObject(this.JSON, RedisCommonHelpersImplConfigurations.TestObject.class);
  }

  @Test
  void testGetAndDelete() {
    TypeReference<RedisCommonHelpersImplConfigurations.TestObject> typeReference =
        new TypeReference<>() {};
    when(this.valueOperations.getAndDelete(this.EXPECT_PREFIX + this.KEY)).thenReturn(this.JSON);
    when(this.jsonHelpers.toObject(this.JSON, typeReference))
        .thenReturn(
            RedisCommonHelpersImplConfigurations.TestObject.builder()
                .id(1L)
                .name(this.NAME)
                .build());
    RedisCommonHelpersImplConfigurations.TestObject result =
        this.redisHelpers.getAndDelete(this.KEY, typeReference);
    assertThat(result)
        .isNotNull()
        .matches(t -> t.getId() == 1L)
        .matches(t -> this.NAME.equals(t.getName()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).getAndDelete(this.EXPECT_PREFIX + this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1)).toObject(this.JSON, typeReference);
  }

  @Test
  void getAndExpire() {
    when(this.valueOperations.getAndExpire(this.EXPECT_PREFIX + this.KEY, this.TIMEOUT))
        .thenReturn(this.JSON);
    when(this.jsonHelpers.toObject(
            this.JSON, RedisCommonHelpersImplConfigurations.TestObject.class))
        .thenReturn(
            RedisCommonHelpersImplConfigurations.TestObject.builder()
                .id(1L)
                .name(this.NAME)
                .build());
    RedisCommonHelpersImplConfigurations.TestObject result =
        this.redisHelpers.getAndExpire(
            this.KEY, this.TIMEOUT, RedisCommonHelpersImplConfigurations.TestObject.class);
    assertThat(result)
        .isNotNull()
        .matches(t -> t.getId() == 1L)
        .matches(t -> this.NAME.equals(t.getName()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1))
        .getAndExpire(this.EXPECT_PREFIX + this.KEY, this.TIMEOUT);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toObject(this.JSON, RedisCommonHelpersImplConfigurations.TestObject.class);
  }

  @Test
  void testGetAndExpire() {
    TypeReference<RedisCommonHelpersImplConfigurations.TestObject> typeReference =
        new TypeReference<>() {};
    when(this.valueOperations.getAndExpire(this.EXPECT_PREFIX + this.KEY, this.TIMEOUT))
        .thenReturn(this.JSON);
    when(this.jsonHelpers.toObject(this.JSON, typeReference))
        .thenReturn(
            RedisCommonHelpersImplConfigurations.TestObject.builder()
                .id(1L)
                .name(this.NAME)
                .build());
    RedisCommonHelpersImplConfigurations.TestObject result =
        this.redisHelpers.getAndExpire(this.KEY, this.TIMEOUT, typeReference);
    assertThat(result)
        .isNotNull()
        .matches(t -> t.getId() == 1L)
        .matches(t -> this.NAME.equals(t.getName()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1))
        .getAndExpire(this.EXPECT_PREFIX + this.KEY, this.TIMEOUT);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1)).toObject(this.JSON, typeReference);
  }

  @Test
  void getAndSet() {
    when(this.valueOperations.getAndSet(this.EXPECT_PREFIX + this.KEY, this.JSON))
        .thenReturn(this.JSON);
    when(this.jsonHelpers.toObject(
            this.JSON, RedisCommonHelpersImplConfigurations.TestObject.class))
        .thenReturn(
            RedisCommonHelpersImplConfigurations.TestObject.builder()
                .id(1L)
                .name(this.NAME)
                .build());
    RedisCommonHelpersImplConfigurations.TestObject result =
        this.redisHelpers.getAndSet(
            this.KEY, this.VALUE, RedisCommonHelpersImplConfigurations.TestObject.class);
    assertThat(result)
        .isNotNull()
        .matches(t -> t.getId() == 1L)
        .matches(t -> this.NAME.equals(t.getName()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).getAndSet(this.EXPECT_PREFIX + this.KEY, this.JSON);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toObject(this.JSON, RedisCommonHelpersImplConfigurations.TestObject.class);
  }

  @Test
  void testGetAndSet() {
    TypeReference<RedisCommonHelpersImplConfigurations.TestObject> typeReference =
        new TypeReference<>() {};
    when(this.valueOperations.getAndSet(this.EXPECT_PREFIX + this.KEY, this.JSON))
        .thenReturn(this.JSON);
    when(this.jsonHelpers.toObject(this.JSON, typeReference))
        .thenReturn(
            RedisCommonHelpersImplConfigurations.TestObject.builder()
                .id(1L)
                .name(this.NAME)
                .build());
    RedisCommonHelpersImplConfigurations.TestObject result =
        this.redisHelpers.getAndSet(this.KEY, this.VALUE, typeReference);
    assertThat(result)
        .isNotNull()
        .matches(t -> t.getId() == 1L)
        .matches(t -> this.NAME.equals(t.getName()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).getAndSet(this.EXPECT_PREFIX + this.KEY, this.JSON);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1)).toObject(this.JSON, typeReference);
  }

  @Test
  void multiGet() {
    when(this.valueOperations.multiGet(
            argThat(
                ks ->
                    ks.size() == this.JSON_LIST.size()
                        && ks.stream().anyMatch((this.EXPECT_PREFIX + "a")::equals)
                        && ks.stream().anyMatch((this.EXPECT_PREFIX + "b")::equals)
                        && ks.stream().anyMatch((this.EXPECT_PREFIX + "c")::equals))))
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
    List<RedisCommonHelpersImplConfigurations.TestObject> results =
        this.redisHelpers.multiGet(
            this.KEYS, RedisCommonHelpersImplConfigurations.TestObject.class);
    assertThat(results)
        .isNotEmpty()
        .hasSize(3)
        .matches(
            l -> l.stream().anyMatch(i -> i.getId() == 1L && i.getName().equals(this.NAME + 1)))
        .matches(
            l -> l.stream().anyMatch(i -> i.getId() == 2L && i.getName().equals(this.NAME + 2)))
        .matches(
            l -> l.stream().anyMatch(i -> i.getId() == 3L && i.getName().equals(this.NAME + 3)));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).multiGet(anyCollection());
    verify(this.properties, times(6)).getKeyPrefix();
    verify(this.properties, times(3)).getKeySplit();
    verify(this.jsonHelpers, times(1))
        .toObject(this.JSON_LIST.get(0), RedisCommonHelpersImplConfigurations.TestObject.class);
    verify(this.jsonHelpers, times(1))
        .toObject(this.JSON_LIST.get(1), RedisCommonHelpersImplConfigurations.TestObject.class);
    verify(this.jsonHelpers, times(1))
        .toObject(this.JSON_LIST.get(2), RedisCommonHelpersImplConfigurations.TestObject.class);
  }

  @Test
  void testMultiGet() {
    TypeReference<RedisCommonHelpersImplConfigurations.TestObject> typeReference =
        new TypeReference<>() {};
    when(this.valueOperations.multiGet(
            argThat(
                ks ->
                    ks.size() == this.JSON_LIST.size()
                        && ks.stream().anyMatch((this.EXPECT_PREFIX + "a")::equals)
                        && ks.stream().anyMatch((this.EXPECT_PREFIX + "b")::equals)
                        && ks.stream().anyMatch((this.EXPECT_PREFIX + "c")::equals))))
        .thenReturn(this.JSON_LIST);
    when(this.jsonHelpers.toObject(this.JSON_LIST.get(0), typeReference))
        .thenReturn(
            RedisCommonHelpersImplConfigurations.TestObject.builder()
                .id(1L)
                .name(this.NAME + 1)
                .build());
    when(this.jsonHelpers.toObject(this.JSON_LIST.get(1), typeReference))
        .thenReturn(
            RedisCommonHelpersImplConfigurations.TestObject.builder()
                .id(2L)
                .name(this.NAME + 2)
                .build());
    when(this.jsonHelpers.toObject(this.JSON_LIST.get(2), typeReference))
        .thenReturn(
            RedisCommonHelpersImplConfigurations.TestObject.builder()
                .id(3L)
                .name(this.NAME + 3)
                .build());
    List<RedisCommonHelpersImplConfigurations.TestObject> results =
        this.redisHelpers.multiGet(this.KEYS, typeReference);
    assertThat(results)
        .isNotEmpty()
        .hasSize(3)
        .matches(
            l -> l.stream().anyMatch(i -> i.getId() == 1L && i.getName().equals(this.NAME + 1)))
        .matches(
            l -> l.stream().anyMatch(i -> i.getId() == 2L && i.getName().equals(this.NAME + 2)))
        .matches(
            l -> l.stream().anyMatch(i -> i.getId() == 3L && i.getName().equals(this.NAME + 3)));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).multiGet(anyCollection());
    verify(this.properties, times(6)).getKeyPrefix();
    verify(this.properties, times(3)).getKeySplit();
    verify(this.jsonHelpers, times(1)).toObject(this.JSON_LIST.get(0), typeReference);
    verify(this.jsonHelpers, times(1)).toObject(this.JSON_LIST.get(1), typeReference);
    verify(this.jsonHelpers, times(1)).toObject(this.JSON_LIST.get(2), typeReference);
  }

  @Test
  void multiGetWithEmptyKeys() {
    List<RedisCommonHelpersImplConfigurations.TestObject> result =
        this.redisHelpers.multiGet(null, RedisCommonHelpersImplConfigurations.TestObject.class);
    assertThat(result).isNotNull().isEmpty();
    verify(this.redisTemplate, never()).opsForValue();
    verify(this.valueOperations, never()).multiGet(anyCollection());
    verify(this.properties, never()).getKeyPrefix();
    verify(this.properties, never()).getKeySplit();
    verify(this.jsonHelpers, never()).toObject(anyString(), (Class<Object>) any());
  }

  @Test
  void increment() {
    when(this.valueOperations.increment(this.EXPECT_PREFIX + this.KEY, 1L)).thenReturn(2L);
    long test = this.redisHelpers.increment(this.KEY, 1L);
    assertThat(test).isEqualTo(2L);
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).increment(this.EXPECT_PREFIX + this.KEY, 1L);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void incrementWithInvalidOperation() {
    when(this.valueOperations.increment(anyString(), anyLong())).thenReturn(null);
    CommonException exception =
        assertThrows(CommonException.class, () -> this.redisHelpers.increment(this.KEY, 1L));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).increment(this.EXPECT_PREFIX + this.KEY, 1L);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void decrement() {
    when(this.valueOperations.decrement(this.EXPECT_PREFIX + this.KEY, 1L)).thenReturn(2L);
    long test = this.redisHelpers.decrement(this.KEY, 1L);
    assertThat(test).isEqualTo(2L);
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).decrement(this.EXPECT_PREFIX + this.KEY, 1L);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void decrementWithInvalidOperation() {
    when(this.valueOperations.decrement(anyString(), anyLong())).thenReturn(null);
    CommonException exception =
        assertThrows(CommonException.class, () -> this.redisHelpers.decrement(this.KEY, 1L));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).decrement(this.EXPECT_PREFIX + this.KEY, 1L);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void append() {
    when(this.valueOperations.append(this.EXPECT_PREFIX + this.KEY, this.KEY))
        .thenReturn(this.KEY.length());
    long test = this.redisHelpers.append(this.KEY, this.KEY);
    assertThat(test).isEqualTo(this.KEY.length());
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).append(this.EXPECT_PREFIX + this.KEY, this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void appendWithInvalidOperation() {
    when(this.valueOperations.append(anyString(), anyString())).thenReturn(null);
    CommonException exception =
        assertThrows(CommonException.class, () -> this.redisHelpers.append(this.KEY, this.KEY));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).append(this.EXPECT_PREFIX + this.KEY, this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void size() {
    when(this.valueOperations.size(this.EXPECT_PREFIX + this.KEY)).thenReturn(64L);
    long test = this.redisHelpers.size(this.KEY);
    assertThat(test).isEqualTo(64L);
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).size(this.EXPECT_PREFIX + this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void sizeWithInvalidOperation() {
    when(this.valueOperations.size(anyString())).thenReturn(null);
    CommonException exception =
        assertThrows(CommonException.class, () -> this.redisHelpers.size(this.KEY));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForValue();
    verify(this.valueOperations, times(1)).size(this.EXPECT_PREFIX + this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }
}
