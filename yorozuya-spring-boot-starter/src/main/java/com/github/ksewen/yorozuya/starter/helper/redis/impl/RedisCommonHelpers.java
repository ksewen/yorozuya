package com.github.ksewen.yorozuya.starter.helper.redis.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import com.github.ksewen.yorozuya.common.exception.CommonException;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import com.github.ksewen.yorozuya.starter.helper.redis.RedisHelperProperties;
import com.github.ksewen.yorozuya.starter.helper.redis.RedisHelpers;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * @author ksewen
 * @date 24.09.2023 22:05
 */
// TODO: Integration testing.
public class RedisCommonHelpers extends PrefixRedisKeyGenerator implements RedisHelpers {

  public static final String PIPELINE_OR_TRANSACTION_NOT_SUPPORT =
      "can not use pipeline/transaction with RedisHelper";

  private final StringRedisTemplate redisTemplate;

  private final JsonHelpers jsonHelpers;

  public RedisCommonHelpers(
      StringRedisTemplate redisTemplate,
      JsonHelpers jsonHelpers,
      RedisHelperProperties properties) {
    super(properties);
    this.redisTemplate = redisTemplate;
    this.jsonHelpers = jsonHelpers;
  }

  @Override
  public boolean hasKey(String key) {
    return Optional.ofNullable(this.redisTemplate.hasKey(this.generate(key)))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public boolean expire(String key, Duration timeout) {
    Assert.isTrue(!timeout.isZero(), "timeout must be a positive number");
    return Optional.ofNullable(this.redisTemplate.expire(this.generate(key), timeout))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public boolean expireAt(String key, Date date) {
    return Optional.ofNullable(this.redisTemplate.expireAt(this.generate(key), date))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public long getExpire(String key) {
    return Optional.ofNullable(this.redisTemplate.getExpire(this.generate(key)))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public boolean delete(String key) {
    return Optional.ofNullable(this.redisTemplate.delete(this.generate(key)))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public long delete(Collection<String> keys) {
    if (CollectionUtils.isEmpty(keys)) {
      return 0L;
    }
    return Optional.ofNullable(
            this.redisTemplate.delete(
                keys.stream().map(this::generate).collect(Collectors.toList())))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public long deleteByPrefix(String prefix) {
    Set<String> keys = this.redisTemplate.keys(this.generate(prefix));
    if (CollectionUtils.isEmpty(keys)) {
      return 0L;
    }
    return Optional.ofNullable(this.redisTemplate.delete(keys))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public void set(String key, Object value) {
    this.redisTemplate.opsForValue().set(this.generate(key), this.jsonHelpers.toJsonString(value));
  }

  @Override
  public void set(String key, Object value, Duration timeout) {
    this.redisTemplate
        .opsForValue()
        .set(this.generate(key), this.jsonHelpers.toJsonString(value), timeout);
  }

  @Override
  public boolean setIfAbsent(String key, Object value) {
    return Optional.ofNullable(
            this.redisTemplate
                .opsForValue()
                .setIfAbsent(this.generate(key), this.jsonHelpers.toJsonString(value)))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public boolean setIfAbsent(String key, Object value, Duration timeout) {
    return Optional.ofNullable(
            this.redisTemplate
                .opsForValue()
                .setIfAbsent(this.generate(key), this.jsonHelpers.toJsonString(value), timeout))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public boolean setIfPresent(String key, Object value) {
    return Optional.ofNullable(
            this.redisTemplate
                .opsForValue()
                .setIfPresent(this.generate(key), this.jsonHelpers.toJsonString(value)))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public boolean setIfPresent(String key, Object value, Duration timeout) {
    return Optional.ofNullable(
            this.redisTemplate
                .opsForValue()
                .setIfPresent(this.generate(key), this.jsonHelpers.toJsonString(value), timeout))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public void multiSet(Map<String, Object> map) {
    if (map.isEmpty()) {
      return;
    }
    Map<String, String> forMultiSet =
        map.entrySet().stream()
            .collect(
                Collectors.toMap(
                    e -> this.generate(e.getKey()),
                    e -> this.jsonHelpers.toJsonString(e.getValue())));
    this.redisTemplate.opsForValue().multiSet(forMultiSet);
  }

  @Override
  public <T> T get(String key, Class<T> tClass) {
    return Optional.ofNullable(this.redisTemplate.opsForValue().get(this.generate(key)))
        .map(x -> this.jsonHelpers.toObject(x, tClass))
        .orElse(null);
  }

  @Override
  public <T> T get(String key, TypeReference<T> valueTypeRef) {
    return Optional.ofNullable(this.redisTemplate.opsForValue().get(this.generate(key)))
        .map(x -> this.jsonHelpers.toObject(x, valueTypeRef))
        .orElse(null);
  }

  @Override
  public <T> T getAndDelete(String key, Class<T> tClass) {
    return Optional.ofNullable(this.redisTemplate.opsForValue().getAndDelete(this.generate(key)))
        .map(x -> this.jsonHelpers.toObject(x, tClass))
        .orElse(null);
  }

  @Override
  public <T> T getAndDelete(String key, TypeReference<T> valueTypeRef) {
    return Optional.ofNullable(this.redisTemplate.opsForValue().getAndDelete(this.generate(key)))
        .map(x -> this.jsonHelpers.toObject(x, valueTypeRef))
        .orElse(null);
  }

  @Override
  public <T> T getAndExpire(String key, Duration timeout, Class<T> tClass) {
    return Optional.ofNullable(
            this.redisTemplate.opsForValue().getAndExpire(this.generate(key), timeout))
        .map(x -> this.jsonHelpers.toObject(x, tClass))
        .orElse(null);
  }

  @Override
  public <T> T getAndExpire(String key, Duration timeout, TypeReference<T> valueTypeRef) {
    return Optional.ofNullable(
            this.redisTemplate.opsForValue().getAndExpire(this.generate(key), timeout))
        .map(x -> this.jsonHelpers.toObject(x, valueTypeRef))
        .orElse(null);
  }

  @Override
  public <T> T getAndSet(String key, Object value, Class<T> tClass) {
    return Optional.ofNullable(
            this.redisTemplate
                .opsForValue()
                .getAndSet(this.generate(key), this.jsonHelpers.toJsonString(value)))
        .map(x -> this.jsonHelpers.toObject(x, tClass))
        .orElse(null);
  }

  @Override
  public <T> T getAndSet(String key, Object value, TypeReference<T> valueTypeRef) {
    return Optional.ofNullable(
            this.redisTemplate
                .opsForValue()
                .getAndSet(this.generate(key), this.jsonHelpers.toJsonString(value)))
        .map(x -> this.jsonHelpers.toObject(x, valueTypeRef))
        .orElse(null);
  }

  @Override
  public <T> List<T> multiGet(Collection<String> keys, Class<T> tClass) {
    List<String> results = this.multiGet(keys);
    return CollectionUtils.isEmpty(results)
        ? Collections.emptyList()
        : results.stream().map(i -> this.jsonHelpers.toObject(i, tClass)).toList();
  }

  @Override
  public <T> List<T> multiGet(Collection<String> keys, TypeReference<T> valueTypeRef) {
    List<String> results = this.multiGet(keys);
    return CollectionUtils.isEmpty(results)
        ? Collections.emptyList()
        : results.stream().map(i -> this.jsonHelpers.toObject(i, valueTypeRef)).toList();
  }

  @Override
  public long increment(String key, long delta) {
    return Optional.ofNullable(
            this.redisTemplate.opsForValue().increment(this.generate(key), delta))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public long decrement(String key, long delta) {
    return Optional.ofNullable(
            this.redisTemplate.opsForValue().decrement(this.generate(key), delta))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public int append(String key, String value) {
    return Optional.ofNullable(this.redisTemplate.opsForValue().append(this.generate(key), value))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public long size(String key) {
    return Optional.ofNullable(this.redisTemplate.opsForValue().size(this.generate(key)))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public void hashSet(String key, String hashKey, Object value) {
    this.redisTemplate
        .<String, String>opsForHash()
        .put(this.generate(key), hashKey, this.jsonHelpers.toJsonString(value));
  }

  @Override
  public void hashSetAll(String key, Map<String, Object> map) {
    if (CollectionUtils.isEmpty(map)) {
      return;
    }
    Map<String, String> forPutAll =
        map.entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey, e -> this.jsonHelpers.toJsonString(e.getValue())));
    this.redisTemplate.<String, String>opsForHash().putAll(this.generate(key), forPutAll);
  }

  @Override
  public boolean hashHasKey(String key, String hashKey) {
    return Optional.ofNullable(
            this.redisTemplate.<String, String>opsForHash().hasKey(this.generate(key), hashKey))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public long hashDelete(String key, String... hashKeys) {
    return Optional.ofNullable(
            this.redisTemplate
                .<String, String>opsForHash()
                .delete(this.generate(key), Arrays.stream(hashKeys).toArray()))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public <T> T hashGet(String key, String hashKey, Class<T> tClass) {
    return Optional.ofNullable(
            this.redisTemplate.<String, String>opsForHash().get(this.generate(key), hashKey))
        .map(i -> this.jsonHelpers.toObject(i, tClass))
        .orElse(null);
  }

  @Override
  public <T> List<T> hashMultiGet(String key, Collection<String> hashKeys, Class<T> tClass) {
    if (CollectionUtils.isEmpty(hashKeys)) {
      return Collections.emptyList();
    }
    List<String> results =
        this.redisTemplate.<String, String>opsForHash().multiGet(this.generate(key), hashKeys);
    return CollectionUtils.isEmpty(results)
        ? Collections.emptyList()
        : results.stream().map(i -> this.jsonHelpers.toObject(i, tClass)).toList();
  }

  @Override
  public long hashSize(String key) {
    return Optional.ofNullable(
            this.redisTemplate.<String, String>opsForHash().size(this.generate(key)))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public <T> List<T> listRange(String key, long start, long end, Class<T> tClass) {
    List<String> results = this.redisTemplate.opsForList().range(this.generate(key), start, end);
    return CollectionUtils.isEmpty(results)
        ? Collections.emptyList()
        : results.stream().map(i -> this.jsonHelpers.toObject(i, tClass)).toList();
  }

  @Override
  public void listTrim(String key, long start, long end) {
    this.redisTemplate.opsForList().trim(this.generate(key), start, end);
  }

  @Override
  public long listSize(String key) {
    return Optional.ofNullable(this.redisTemplate.opsForList().size(this.generate(key)))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public <T> T listIndex(String key, long index, Class<T> tClass) {
    return Optional.ofNullable(this.redisTemplate.opsForList().index(this.generate(key), index))
        .map(x -> this.jsonHelpers.toObject(x, tClass))
        .orElse(null);
  }

  @Override
  public long listLeftPush(String key, Object value) {
    return Optional.ofNullable(
            this.redisTemplate
                .opsForList()
                .leftPush(this.generate(key), this.jsonHelpers.toJsonString(value)))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public long listLeftPushAll(String key, Collection<Object> values) {
    return Optional.ofNullable(
            this.redisTemplate
                .opsForList()
                .leftPushAll(
                    this.generate(key),
                    values.stream().map(this.jsonHelpers::toJsonString).toList()))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public long listRightPush(String key, Object value) {
    return Optional.ofNullable(
            this.redisTemplate
                .opsForList()
                .rightPush(generate(key), this.jsonHelpers.toJsonString(value)))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public long listRightPushAll(String key, Collection<Object> values) {
    return Optional.ofNullable(
            this.redisTemplate
                .opsForList()
                .rightPushAll(
                    this.generate(key),
                    values.stream().map(this.jsonHelpers::toJsonString).toList()))
        .orElseThrow(this::throwInvalidOperationException);
  }

  @Override
  public void listSet(String key, long index, Object value) {
    this.redisTemplate
        .opsForList()
        .set(this.generate(key), index, this.jsonHelpers.toJsonString(value));
  }

  @Override
  public <T> T listLeftPop(String key, Class<T> tClass) {
    return Optional.ofNullable(this.redisTemplate.opsForList().leftPop(this.generate(key)))
        .map(x -> this.jsonHelpers.toObject(x, tClass))
        .orElse(null);
  }

  @Override
  public <T> List<T> listLeftPop(String key, long count, Class<T> tClass) {
    List<String> results = this.redisTemplate.opsForList().leftPop(this.generate(key), count);
    return CollectionUtils.isEmpty(results)
        ? Collections.emptyList()
        : results.stream().map(x -> this.jsonHelpers.toObject(x, tClass)).toList();
  }

  @Override
  public <T> T listRightPop(String key, Class<T> tClass) {
    return Optional.ofNullable(this.redisTemplate.opsForList().rightPop(this.generate(key)))
        .map(x -> this.jsonHelpers.toObject(x, tClass))
        .orElse(null);
  }

  @Override
  public <T> List<T> listRightPop(String key, long count, Class<T> tClass) {
    List<String> results = this.redisTemplate.opsForList().rightPop(this.generate(key), count);
    return CollectionUtils.isEmpty(results)
        ? Collections.emptyList()
        : results.stream().map(x -> this.jsonHelpers.toObject(x, tClass)).toList();
  }

  private List<String> multiGet(Collection<String> keys) {
    if (CollectionUtils.isEmpty(keys)) {
      return Collections.emptyList();
    }
    return this.redisTemplate.opsForValue().multiGet(keys.stream().map(this::generate).toList());
  }

  private CommonException throwInvalidOperationException() {
    return new CommonException(
        DefaultResultCodeEnums.INVALID_OPERATION,
        RedisCommonHelpers.PIPELINE_OR_TRANSACTION_NOT_SUPPORT);
  }
}
