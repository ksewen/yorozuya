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
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author ksewen
 * @date 28.09.2023 22:51
 */
@SpringBootTest(classes = RedisCommonHelpers.class)
public class RedisHelpersForListImplTest extends RedisCommonHelpersImplConfigurations {

  @Autowired private RedisHelpers redisHelpers;

  @MockBean private StringRedisTemplate redisTemplate;

  @MockBean private ListOperations<String, String> listOperations;

  @MockBean private RedisHelperProperties properties;

  @MockBean private JsonHelpers jsonHelpers;

  private final int START = 0;

  private final int END = 2;

  private final int INDEX = 7;

  private final List<Object> VALUES =
      Arrays.asList(
          TestObject.builder().id(1L).name(this.NAME + 1).build(),
          TestObject.builder().id(2L).name(this.NAME + 2).build(),
          TestObject.builder().id(3L).name(this.NAME + 3).build(),
          TestObject.builder().id(4L).name(this.NAME + 4).build(),
          TestObject.builder().id(5L).name(this.NAME + 5).build());

  @BeforeEach
  void beforeEach() {
    when(this.redisTemplate.opsForList()).thenReturn(this.listOperations);
    when(this.properties.getKeyPrefix()).thenReturn(this.KEY_PREFIX);
    when(this.properties.getKeySplit()).thenReturn(this.KEY_SPLIT);
    when(this.jsonHelpers.toJsonString(any(TestObject.class))).thenReturn(this.JSON);
  }

  @Test
  void listRange() {
    when(this.listOperations.range(this.EXPECT_PREFIX + this.KEY, this.START, this.END))
        .thenReturn(this.JSON_LIST);
    when(this.jsonHelpers.toObject(argThat(v -> v.startsWith(this.JSON)), eq(TestObject.class)))
        .thenAnswer(
            (Answer<TestObject>)
                invocation -> {
                  String value = invocation.getArgument(0);
                  int id = Integer.parseInt(value.substring(JSON.length()));
                  return TestObject.builder().id((long) id).name(NAME + id).build();
                });
    List<TestObject> result =
        this.redisHelpers.listRange(this.KEY, this.START, this.END, TestObject.class);
    assertThat(result)
        .isNotEmpty()
        .hasSize(this.JSON_LIST.size())
        .matches(l -> l.stream().anyMatch(i -> i.getId() == 1L))
        .matches(l -> l.stream().anyMatch(i -> (this.NAME + 1).equals(i.getName())))
        .matches(l -> l.stream().anyMatch(i -> i.getId() == 2L))
        .matches(l -> l.stream().anyMatch(i -> (this.NAME + 2).equals(i.getName())))
        .matches(l -> l.stream().anyMatch(i -> i.getId() == 3L))
        .matches(l -> l.stream().anyMatch(i -> (this.NAME + 3).equals(i.getName())));
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1))
        .range(this.EXPECT_PREFIX + this.KEY, this.START, this.END);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1)).toObject(this.JSON + 1, TestObject.class);
    verify(this.jsonHelpers, times(1)).toObject(this.JSON + 2, TestObject.class);
    verify(this.jsonHelpers, times(1)).toObject(this.JSON + 3, TestObject.class);
  }

  @Test
  void listTrim() {
    this.redisHelpers.listTrim(this.KEY, this.START, this.END);
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1)).trim(this.EXPECT_PREFIX + this.KEY, this.START, this.END);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void listSize() {
    when(this.listOperations.size(this.EXPECT_PREFIX + this.KEY)).thenReturn(7L);
    long test = this.redisHelpers.listSize(this.KEY);
    assertThat(test).isEqualTo(7L);
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1)).size(this.EXPECT_PREFIX + this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void listSizeWithInvalidOperation() {
    when(this.listOperations.size(anyString())).thenReturn(null);
    CommonException exception =
        assertThrows(CommonException.class, () -> this.redisHelpers.listSize(this.KEY));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1)).size(this.EXPECT_PREFIX + this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void listIndex() {
    when(this.listOperations.index(this.EXPECT_PREFIX + this.KEY, this.INDEX))
        .thenReturn(this.JSON);
    when(this.jsonHelpers.toObject(argThat(this.JSON::equals), eq(TestObject.class)))
        .thenReturn(TestObject.builder().id(1L).name(this.NAME).build());
    TestObject result = this.redisHelpers.listIndex(this.KEY, this.INDEX, TestObject.class);
    assertThat(result)
        .isNotNull()
        .matches(t -> t.getId() == 1L)
        .matches(t -> this.NAME.equals(t.getName()));
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1)).index(this.EXPECT_PREFIX + this.KEY, this.INDEX);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1)).toObject(argThat(this.JSON::equals), eq(TestObject.class));
  }

  @Test
  void listLeftPush() {
    when(this.listOperations.leftPush(
            argThat((this.EXPECT_PREFIX + this.KEY)::equals), anyString()))
        .thenReturn(1L);
    when(this.jsonHelpers.toJsonString(any(TestObject.class))).thenReturn(this.JSON);
    long test = this.redisHelpers.listLeftPush(this.KEY, this.VALUE);
    assertThat(test).isEqualTo(1L);
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1)).leftPush(this.EXPECT_PREFIX + this.KEY, this.JSON);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1)).toJsonString(any(TestObject.class));
  }

  @Test
  void listLeftPushWithInvalidOperation() {
    when(this.listOperations.leftPush(anyString(), anyString())).thenReturn(null);
    CommonException exception =
        assertThrows(
            CommonException.class, () -> this.redisHelpers.listLeftPush(this.KEY, this.VALUE));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1)).leftPush(this.EXPECT_PREFIX + this.KEY, this.JSON);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1)).toJsonString(any(TestObject.class));
  }

  @Test
  void listLeftPushAll() {
    when(this.listOperations.leftPushAll(
            argThat((this.EXPECT_PREFIX + this.KEY)::equals), anyCollection()))
        .thenReturn((long) this.VALUES.size());
    when(this.jsonHelpers.toJsonString(any(TestObject.class)))
        .thenAnswer(
            (Answer<String>)
                invocation -> {
                  TestObject argument = invocation.getArgument(0);
                  return JSON + argument.getId();
                });
    long test = this.redisHelpers.listLeftPushAll(this.KEY, this.VALUES);
    assertThat(test).isEqualTo(this.VALUES.size());
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1))
        .leftPushAll(argThat((this.EXPECT_PREFIX + this.KEY)::equals), anyCollection());
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(this.VALUES.size())).toJsonString(any(TestObject.class));
  }

  @Test
  void listLeftPushAllWithInvalidOperation() {
    when(this.listOperations.leftPushAll(anyString(), anyCollection())).thenReturn(null);
    CommonException exception =
        assertThrows(
            CommonException.class, () -> this.redisHelpers.listLeftPushAll(this.KEY, this.VALUES));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1))
        .leftPushAll(argThat((this.EXPECT_PREFIX + this.KEY)::equals), anyCollection());
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(this.VALUES.size())).toJsonString(any(TestObject.class));
  }

  @Test
  void listRightPush() {
    when(this.listOperations.rightPush(
            argThat((this.EXPECT_PREFIX + this.KEY)::equals), anyString()))
        .thenReturn(1L);
    when(this.jsonHelpers.toJsonString(any(TestObject.class))).thenReturn(this.JSON);
    long test = this.redisHelpers.listRightPush(this.KEY, this.VALUE);
    assertThat(test).isEqualTo(1L);
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1)).rightPush(this.EXPECT_PREFIX + this.KEY, this.JSON);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1)).toJsonString(any(TestObject.class));
  }

  @Test
  void listRightPushWithInvalidOperation() {
    when(this.listOperations.rightPush(anyString(), anyString())).thenReturn(null);
    CommonException exception =
        assertThrows(
            CommonException.class, () -> this.redisHelpers.listRightPush(this.KEY, this.VALUE));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1)).rightPush(this.EXPECT_PREFIX + this.KEY, this.JSON);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1)).toJsonString(any(TestObject.class));
  }

  @Test
  void listRightPushAll() {
    when(this.listOperations.rightPushAll(
            argThat((this.EXPECT_PREFIX + this.KEY)::equals), anyCollection()))
        .thenReturn((long) this.VALUES.size());
    when(this.jsonHelpers.toJsonString(any(TestObject.class)))
        .thenAnswer(
            (Answer<String>)
                invocation -> {
                  TestObject argument = invocation.getArgument(0);
                  return JSON + argument.getId();
                });
    long test = this.redisHelpers.listRightPushAll(this.KEY, this.VALUES);
    assertThat(test).isEqualTo(this.VALUES.size());
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1))
        .rightPushAll(argThat((this.EXPECT_PREFIX + this.KEY)::equals), anyCollection());
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(this.VALUES.size())).toJsonString(any(TestObject.class));
  }

  @Test
  void listRightPushAllWithInvalidOperation() {
    when(this.listOperations.rightPushAll(anyString(), anyCollection())).thenReturn(null);
    CommonException exception =
        assertThrows(
            CommonException.class, () -> this.redisHelpers.listRightPushAll(this.KEY, this.VALUES));
    assertThat(exception)
        .matches(e -> DefaultResultCodeEnums.INVALID_OPERATION.equals(e.getCode()))
        .matches(
            e -> RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT.equals(e.getMessage()));
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1))
        .rightPushAll(argThat((this.EXPECT_PREFIX + this.KEY)::equals), anyCollection());
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(this.VALUES.size())).toJsonString(any(TestObject.class));
  }

  @Test
  void listSet() {
    when(this.jsonHelpers.toJsonString(any(TestObject.class))).thenReturn(this.JSON);
    this.redisHelpers.listSet(this.KEY, this.INDEX, this.VALUE);
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1)).set(this.EXPECT_PREFIX + this.KEY, this.INDEX, this.JSON);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
  }

  @Test
  void listLeftPop() {
    when(this.listOperations.leftPop(this.EXPECT_PREFIX + this.KEY)).thenReturn(this.JSON);
    when(this.jsonHelpers.toObject(argThat(this.JSON::equals), eq(TestObject.class)))
        .thenReturn(TestObject.builder().id(1L).name(this.NAME).build());
    TestObject result = this.redisHelpers.listLeftPop(this.KEY, TestObject.class);
    assertThat(result)
        .isNotNull()
        .matches(t -> t.getId() == 1L)
        .matches(t -> this.NAME.equals(t.getName()));
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1)).leftPop(this.EXPECT_PREFIX + this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1)).toObject(argThat(this.JSON::equals), eq(TestObject.class));
  }

  @Test
  void testListLeftPop() {
    when(this.listOperations.leftPop(this.EXPECT_PREFIX + this.KEY, this.JSON_LIST.size()))
        .thenReturn(this.JSON_LIST);
    when(this.jsonHelpers.toObject(argThat(v -> v.startsWith(this.JSON)), eq(TestObject.class)))
        .thenAnswer(
            (Answer<TestObject>)
                invocation -> {
                  String argument = invocation.getArgument(0);
                  int id = Integer.parseInt(argument.substring(JSON.length()));
                  return TestObject.builder().id((long) id).name(NAME + id).build();
                });
    List<TestObject> result =
        this.redisHelpers.listLeftPop(this.KEY, this.JSON_LIST.size(), TestObject.class);
    assertThat(result)
        .isNotEmpty()
        .hasSize(this.JSON_LIST.size())
        .matches(t -> t.stream().anyMatch(i -> i.getId() == 1L))
        .matches(t -> t.stream().anyMatch(i -> (this.NAME + 1).equals(i.getName())))
        .matches(t -> t.stream().anyMatch(i -> i.getId() == 2L))
        .matches(t -> t.stream().anyMatch(i -> (this.NAME + 2).equals(i.getName())))
        .matches(t -> t.stream().anyMatch(i -> i.getId() == 3L))
        .matches(t -> t.stream().anyMatch(i -> (this.NAME + 3).equals(i.getName())));
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1))
        .leftPop(this.EXPECT_PREFIX + this.KEY, this.JSON_LIST.size());
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(this.JSON_LIST.size()))
        .toObject(argThat(v -> v.startsWith(this.JSON)), eq(TestObject.class));
  }

  @Test
  void listRightPop() {
    when(this.listOperations.rightPop(this.EXPECT_PREFIX + this.KEY)).thenReturn(this.JSON);
    when(this.jsonHelpers.toObject(argThat(this.JSON::equals), eq(TestObject.class)))
        .thenReturn(TestObject.builder().id(1L).name(this.NAME).build());
    TestObject result = this.redisHelpers.listRightPop(this.KEY, TestObject.class);
    assertThat(result)
        .isNotNull()
        .matches(t -> t.getId() == 1L)
        .matches(t -> this.NAME.equals(t.getName()));
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1)).rightPop(this.EXPECT_PREFIX + this.KEY);
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(1)).toObject(argThat(this.JSON::equals), eq(TestObject.class));
  }

  @Test
  void testListRightPop() {
    when(this.listOperations.rightPop(this.EXPECT_PREFIX + this.KEY, this.JSON_LIST.size()))
        .thenReturn(this.JSON_LIST);
    when(this.jsonHelpers.toObject(argThat(v -> v.startsWith(this.JSON)), eq(TestObject.class)))
        .thenAnswer(
            (Answer<TestObject>)
                invocation -> {
                  String argument = invocation.getArgument(0);
                  int id = Integer.parseInt(argument.substring(JSON.length()));
                  return TestObject.builder().id((long) id).name(NAME + id).build();
                });
    List<TestObject> result =
        this.redisHelpers.listRightPop(this.KEY, this.JSON_LIST.size(), TestObject.class);
    assertThat(result)
        .isNotEmpty()
        .hasSize(this.JSON_LIST.size())
        .matches(t -> t.stream().anyMatch(i -> i.getId() == 1L))
        .matches(t -> t.stream().anyMatch(i -> (this.NAME + 1).equals(i.getName())))
        .matches(t -> t.stream().anyMatch(i -> i.getId() == 2L))
        .matches(t -> t.stream().anyMatch(i -> (this.NAME + 2).equals(i.getName())))
        .matches(t -> t.stream().anyMatch(i -> i.getId() == 3L))
        .matches(t -> t.stream().anyMatch(i -> (this.NAME + 3).equals(i.getName())));
    verify(this.redisTemplate, times(1)).opsForList();
    verify(this.listOperations, times(1))
        .rightPop(this.EXPECT_PREFIX + this.KEY, this.JSON_LIST.size());
    verify(this.properties, times(2)).getKeyPrefix();
    verify(this.properties, times(1)).getKeySplit();
    verify(this.jsonHelpers, times(this.JSON_LIST.size()))
        .toObject(argThat(v -> v.startsWith(this.JSON)), eq(TestObject.class));
  }
}
