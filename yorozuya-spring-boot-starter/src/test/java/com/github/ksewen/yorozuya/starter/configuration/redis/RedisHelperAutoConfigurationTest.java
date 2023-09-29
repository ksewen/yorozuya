package com.github.ksewen.yorozuya.starter.configuration.redis;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import com.github.ksewen.yorozuya.starter.helper.json.impl.JacksonJsonHelpers;
import com.github.ksewen.yorozuya.starter.helper.redis.RedisHelpers;
import com.github.ksewen.yorozuya.starter.helper.redis.impl.RedisCommonHelpers;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author ksewen
 * @date 25.09.2023 22:16
 */
class RedisHelperAutoConfigurationTest {

  @Test
  void redisCommonHelpers() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                RedisHelpersAutoConfiguration.class,
                RedisAutoConfiguration.class,
                RedisHelperAutoConfigurationTest.MockJsonHelpersAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(RedisHelpers.class);
              assertThat(context)
                  .getBean("redisCommonHelpers")
                  .isSameAs(context.getBean(RedisHelpers.class));
            });
  }

  @Test
  void nonRedisCommonHelpers() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                RedisHelpersAutoConfiguration.class,
                RedisHelperAutoConfigurationTest.MockJsonHelpersAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(RedisHelpers.class);
            });
  }

  @Test
  void nonRedisCommonHelpers1() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                MockRedisHelpersAutoConfiguration.class,
                RedisHelpersAutoConfiguration.class,
                RedisHelperAutoConfigurationTest.MockJsonHelpersAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(RedisCommonHelpers.class);
            });
  }

  @Configuration
  @ConditionalOnClass(ObjectMapper.class)
  static class MockJsonHelpersAutoConfiguration {

    @Bean
    public JsonHelpers jsonHelpers() {
      return new JacksonJsonHelpers(new ObjectMapper());
    }
  }

  @Configuration
  @ConditionalOnClass(StringRedisTemplate.class)
  static class MockRedisHelpersAutoConfiguration {
    @Bean
    public RedisHelpers redisHelpers() {
      return new MockRedisHelpers();
    }
  }

  static class MockRedisHelpers implements RedisHelpers {

    @Override
    public boolean hasKey(String key) {
      return false;
    }

    @Override
    public boolean expire(String key, Duration timeout) {
      return false;
    }

    @Override
    public boolean expireAt(String key, Date date) {
      return false;
    }

    @Override
    public long getExpire(String key) {
      return 0;
    }

    @Override
    public boolean delete(String key) {
      return false;
    }

    @Override
    public long delete(Collection<String> keys) {
      return 0;
    }

    @Override
    public long deleteByPrefix(String prefix) {
      return 0;
    }

    @Override
    public void set(String key, Object value) {}

    @Override
    public void set(String key, Object value, Duration timeout) {}

    @Override
    public boolean setIfAbsent(String key, Object value) {
      return false;
    }

    @Override
    public boolean setIfAbsent(String key, Object value, Duration timeout) {
      return false;
    }

    @Override
    public boolean setIfPresent(String key, Object value) {
      return false;
    }

    @Override
    public boolean setIfPresent(String key, Object value, Duration timeout) {
      return false;
    }

    @Override
    public void multiSet(Map<String, Object> map) {}

    @Override
    public <T> T get(String key, Class<T> tClass) {
      return null;
    }

    @Override
    public <T> T get(String key, TypeReference<T> valueTypeRef) {
      return null;
    }

    @Override
    public <T> T getAndDelete(String key, Class<T> tClass) {
      return null;
    }

    @Override
    public <T> T getAndDelete(String key, TypeReference<T> valueTypeRef) {
      return null;
    }

    @Override
    public <T> T getAndExpire(String key, Duration timeout, Class<T> tClass) {
      return null;
    }

    @Override
    public <T> T getAndExpire(String key, Duration timeout, TypeReference<T> valueTypeRef) {
      return null;
    }

    @Override
    public <T> T getAndSet(String key, Object value, Class<T> tClass) {
      return null;
    }

    @Override
    public <T> T getAndSet(String key, Object value, TypeReference<T> valueTypeRef) {
      return null;
    }

    @Override
    public <T> List<T> multiGet(Collection<String> keys, Class<T> tClass) {
      return null;
    }

    @Override
    public <T> List<T> multiGet(Collection<String> keys, TypeReference<T> valueTypeRef) {
      return null;
    }

    @Override
    public long increment(String key, long delta) {
      return 0;
    }

    @Override
    public long decrement(String key, long delta) {
      return 0;
    }

    @Override
    public int append(String key, String value) {
      return 0;
    }

    @Override
    public long size(String key) {
      return 0;
    }

    @Override
    public void hashSet(String key, String hashKey, Object value) {}

    @Override
    public void hashSetAll(String key, Map<String, Object> map) {}

    @Override
    public boolean hashHasKey(String key, String hashKey) {
      return false;
    }

    @Override
    public long hashDelete(String key, String... hashKeys) {
      return 0;
    }

    @Override
    public <T> T hashGet(String key, String hashKey, Class<T> tClass) {
      return null;
    }

    @Override
    public <T> List<T> hashMultiGet(String key, Collection<String> hashKeys, Class<T> tClass) {
      return null;
    }

    @Override
    public long hashSize(String key) {
      return 0;
    }

    @Override
    public <T> List<T> listRange(String key, long start, long end, Class<T> tClass) {
      return null;
    }

    @Override
    public void listTrim(String key, long start, long end) {}

    @Override
    public long listSize(String key) {
      return 0;
    }

    @Override
    public <T> T listIndex(String key, long index, Class<T> tClass) {
      return null;
    }

    @Override
    public long listLeftPush(String key, Object value) {
      return 0;
    }

    @Override
    public long listLeftPushAll(String key, Collection<Object> values) {
      return 0;
    }

    @Override
    public long listRightPush(String key, Object value) {
      return 0;
    }

    @Override
    public long listRightPushAll(String key, Collection<Object> values) {
      return 0;
    }

    @Override
    public void listSet(String key, long index, Object value) {}

    @Override
    public <T> T listLeftPop(String key, Class<T> tClass) {
      return null;
    }

    @Override
    public <T> List<T> listLeftPop(String key, long count, Class<T> tClass) {
      return null;
    }

    @Override
    public <T> T listRightPop(String key, Class<T> tClass) {
      return null;
    }

    @Override
    public <T> List<T> listRightPop(String key, long count, Class<T> tClass) {
      return null;
    }
  }
}
