package com.github.ksewen.yorozuya.starter.helper.redis.impl;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Data;

/**
 * @author ksewen
 * @date 27.09.2023 20:24
 */
public class RedisCommonHelpersImplConfigurations {

  protected final String KEY_PREFIX = "TEST-REDIS";

  protected final String KEY_SPLIT = "-";

  protected final String EXPECT_PREFIX = KEY_PREFIX + KEY_SPLIT;

  protected final String KEY = "key";

  protected final List<String> KEYS = Stream.of("a", "b", "c").toList();

  protected final String NAME = "test";

  protected final String JSON = "testJson";

  protected final List<String> JSON_LIST =
      Stream.of("testJson1", "testJson2", "testJson3").toList();

  protected final Duration TIMEOUT = Duration.ofSeconds(10L);

  protected final RedisCommonHelpersImplConfigurations.TestObject VALUE =
      RedisCommonHelpersImplConfigurations.TestObject.builder().id(1L).name("test").build();

  protected final Map<String, Object> KEY_MAP =
      Stream.of(
              new AbstractMap.SimpleEntry<>(
                  "a",
                  RedisCommonHelpersImplConfigurations.TestObject.builder()
                      .id(1L)
                      .name("test1")
                      .build()),
              new AbstractMap.SimpleEntry<>(
                  "b",
                  RedisCommonHelpersImplConfigurations.TestObject.builder()
                      .id(2L)
                      .name("test2")
                      .build()),
              new AbstractMap.SimpleEntry<>(
                  "c",
                  RedisCommonHelpersImplConfigurations.TestObject.builder()
                      .id(3L)
                      .name("test3")
                      .build()))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

  @Data
  @Builder
  protected static class TestObject {
    private Long id;
    private String name;
  }
}
