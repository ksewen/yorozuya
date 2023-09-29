package com.github.ksewen.yorozuya.starter.helper.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import java.time.Duration;
import java.util.*;

/**
 * @author ksewen
 * @date 24.09.2023 22:01
 */
public interface RedisHelpers {

  /**
   * Determine if given {@code key} exists.
   *
   * @param key must not be null.
   * @return
   */
  boolean hasKey(String key);

  /**
   * Set time to live for given {@code key}.
   *
   * @param key must not be {@literal null}.
   * @param timeout must not be {@literal null}.
   * @return
   */
  boolean expire(String key, Duration timeout);

  /**
   * Set the expiration for given {@code key} as a {@code date} timestamp.
   *
   * @param key must not be {@literal null}.
   * @param date must not be {@literal null}.
   * @return
   */
  boolean expireAt(String key, Date date);

  /**
   * Get the time to live for {@code key} in seconds.
   *
   * @param key must not be {@literal null}.
   * @return
   */
  long getExpire(String key);

  /**
   * Delete given {@code key}.
   *
   * @param key must not be {@literal null}.
   * @return {@literal true} if the key was removed.
   */
  boolean delete(String key);

  /**
   * Delete given {@code keys}.
   *
   * @param keys must not be {@literal null}.
   * @return The number of keys that were removed.
   */
  long delete(Collection<String> keys);

  /**
   * Delete keys by given {@code prefix}.
   *
   * @param prefix must not be {@literal null}.
   * @return The number of keys that were removed.
   */
  long deleteByPrefix(String prefix);

  /**
   * Set {@code value} for {@code key}.
   *
   * @param key
   * @param value
   */
  void set(String key, Object value);

  /**
   * @param key must not be null.
   * @param value must not be null.
   * @param timeout must not be null.
   */
  void set(String key, Object value, Duration timeout);

  /**
   * Set key to hold the string value if key is absent.
   *
   * @param key must not be null.
   * @param value must not be null.
   * @return
   */
  boolean setIfAbsent(String key, Object value);

  /**
   * Set key to hold the string value and expiration timeout if key is absent.
   *
   * @param key must not be null.
   * @param value must not be null.
   * @param timeout must not be null.
   * @return
   */
  boolean setIfAbsent(String key, Object value, Duration timeout);

  /**
   * Set key to hold the string value if key is present.
   *
   * @param key must not be null.
   * @param value must not be null.
   * @return command result indicating if the key has been set.
   */
  boolean setIfPresent(String key, Object value);

  /**
   * Set key to hold the string value and expiration timeout if key is present.
   *
   * @param key must not be null.
   * @param value must not be null.
   * @param timeout must not be null.
   * @return command result indicating if the key has been set.
   */
  boolean setIfPresent(String key, Object value, Duration timeout);

  /**
   * Set multiple keys to multiple values using key-value pairs provided in tuple.
   *
   * @param map must not be null.
   */
  void multiSet(Map<String, Object> map);

  /**
   * Get the value of key.
   *
   * @param key must not be null.
   * @param tClass must not be null.
   * @return null when key does not exist or used in pipeline / transaction.
   */
  <T> T get(String key, Class<T> tClass);

  /**
   * Get the value of key.
   *
   * @param key must not be null.
   * @param valueTypeRef must not be null.
   * @return null when key does not exist or used in pipeline / transaction.
   */
  <T> T get(String key, TypeReference<T> valueTypeRef);

  /**
   * Return the value at key and delete the key.
   *
   * @param key must not be null.
   * @param tClass must not be null.
   * @return null when key does not exist or used in pipeline / transaction.
   */
  <T> T getAndDelete(String key, Class<T> tClass);

  /**
   * Return the value at key and delete the key.
   *
   * @param key must not be null.
   * @param valueTypeRef must not be null.
   * @return null when key does not exist or used in pipeline / transaction.
   */
  <T> T getAndDelete(String key, TypeReference<T> valueTypeRef);

  /**
   * Return the value at key and expire the key by applying timeout.
   *
   * @param key must not be null.
   * @param timeout must not be null.
   * @param tClass must not be null.
   * @return null when key does not exist or used in pipeline / transaction.
   */
  <T> T getAndExpire(String key, Duration timeout, Class<T> tClass);

  /**
   * Return the value at key and expire the key by applying timeout.
   *
   * @param key must not be null.
   * @param timeout must not be null.
   * @param valueTypeRef must not be null.
   * @return null when key does not exist or used in pipeline / transaction.
   */
  <T> T getAndExpire(String key, Duration timeout, TypeReference<T> valueTypeRef);

  /**
   * Set value of key and return its old value
   *
   * @param key must not be null.
   * @param value must not be null.
   * @param tClass must not be null.
   * @return null when key does not exist or used in pipeline / transaction.
   */
  <T> T getAndSet(String key, Object value, Class<T> tClass);

  /**
   * Set value of key and return its old value
   *
   * @param key must not be null.
   * @param value must not be null.
   * @param valueTypeRef must not be null.
   * @return null when key does not exist or used in pipeline / transaction.
   */
  <T> T getAndSet(String key, Object value, TypeReference<T> valueTypeRef);

  /**
   * Get multiple keys. Values are in the order of the requested keys Absent field values are
   * represented using null in the resulting List.
   *
   * @param keys must not be null.
   * @param tClass must not be null.
   * @return null when used in pipeline / transaction.
   */
  <T> List<T> multiGet(Collection<String> keys, Class<T> tClass);

  /**
   * Get multiple keys. Values are in the order of the requested keys Absent field values are
   * represented using null in the resulting List.
   *
   * @param keys must not be null.
   * @param valueTypeRef must not be null.
   * @return null when used in pipeline / transaction.
   */
  <T> List<T> multiGet(Collection<String> keys, TypeReference<T> valueTypeRef);

  /**
   * Increment an integer value stored as string value under key by delta.
   *
   * @param key must not be null.
   * @param delta
   * @return
   */
  long increment(String key, long delta);

  /**
   * Decrement an integer value stored as string value under key by one.
   *
   * @param key must not be null.
   * @param delta
   * @return
   */
  long decrement(String key, long delta);

  /**
   * Append a value to key.
   *
   * @param key must not be null.
   * @param value
   * @return
   */
  int append(String key, String value);

  /**
   * Get the length of the value stored at key.
   *
   * @param key must not be null.
   * @return
   */
  long size(String key);

  /**
   * Set the {@code value} of a hash {@code hashKey}.
   *
   * @param key must not be null.
   * @param hashKey must not be null.
   * @param value must not be null.
   */
  void hashSet(String key, String hashKey, Object value);

  /**
   * Set multiple hash fields to multiple values using data provided in m.
   *
   * @param key must not be null.
   * @param map must not be null.
   */
  void hashSetAll(String key, Map<String, Object> map);

  /**
   * Determine if given hash hashKey exists.
   *
   * @param key must not be null.
   * @param hashKey must not be null.
   * @return
   */
  boolean hashHasKey(String key, String hashKey);

  /**
   * Delete given hash hashKeys.
   *
   * @param key must not be null.
   * @param hashKeys must not be null.
   * @return
   */
  long hashDelete(String key, String... hashKeys);

  /**
   * Get value for given hashKey from hash at key.
   *
   * @param key must not be null.
   * @param hashKey must not be null.
   * @param tClass must not be null.
   * @return null when key or hashKey does not exist
   */
  <T> T hashGet(String key, String hashKey, Class<T> tClass);

  /**
   * Get values for given hashKeys from hash at key. Values are in the order of the requested keys
   * Absent field values are represented using null in the resulting List.
   *
   * @param key must not be null.
   * @param hashKeys must not be null.
   * @param tClass must not be null.
   * @return empty List when key or hashKey does not exist.
   */
  <T> List<T> hashMultiGet(String key, Collection<String> hashKeys, Class<T> tClass);

  /**
   * Get size of hash at key.
   *
   * @param key must not be null.
   * @return
   */
  long hashSize(String key);

  /**
   * Get elements between begin and end from list at key.
   *
   * @param key must not be null.
   * @param start
   * @param end
   * @param tClass must not be null.
   * @return
   */
  <T> List<T> listRange(String key, long start, long end, Class<T> tClass);

  /**
   * Trim list at key to elements between start and end.
   *
   * @param key must not be null.
   * @param start
   * @param end
   */
  void listTrim(String key, long start, long end);

  /**
   * Get the size of list stored at key.
   *
   * @param key must not be null.
   * @return
   */
  long listSize(String key);

  /**
   * Get element at {@code index} form list at {@code key}.
   *
   * @param key must not be null.
   * @param index must not be null.
   * @param tClass
   * @return
   */
  <T> T listIndex(String key, long index, Class<T> tClass);

  /**
   * Prepend value to key.
   *
   * @param key must not be null.
   * @param value
   * @return
   */
  long listLeftPush(String key, Object value);

  /**
   * Prepend value to key.
   *
   * @param key must not be null.
   * @param values must not be null.
   * @return
   */
  long listLeftPushAll(String key, Collection<Object> values);

  /**
   * Append value to key.
   *
   * @param key must not be null.
   * @param value
   * @return
   */
  long listRightPush(String key, Object value);

  /**
   * Append value to key.
   *
   * @param key must not be null.
   * @param values must not be null.
   * @return
   */
  long listRightPushAll(String key, Collection<Object> values);

  /**
   * Set the {@code value} list element at {@code index}.
   *
   * @param key must not be {@literal null}.
   * @param index
   * @param value
   */
  void listSet(String key, long index, Object value);

  /**
   * Removes and returns first element in list stored at key.
   *
   * @param key must not be null.
   * @param tClass must not be null.
   * @return
   */
  <T> T listLeftPop(String key, Class<T> tClass);

  /**
   * Removes and returns first elements in list stored at key.
   *
   * @param key must not be null.
   * @param count must not be null.
   * @param tClass must not be null.
   * @return
   */
  <T> List<T> listLeftPop(String key, long count, Class<T> tClass);

  /**
   * Removes and returns last elements in list stored at key.
   *
   * @param key must not be null.
   * @param tClass must not be null.
   * @return
   */
  <T> T listRightPop(String key, Class<T> tClass);

  /**
   * Removes and returns last elements in list stored at key.
   *
   * @param key must not be null.
   * @param count must not be null.
   * @param tClass must not be null.
   * @return
   */
  <T> List<T> listRightPop(String key, long count, Class<T> tClass);
}
