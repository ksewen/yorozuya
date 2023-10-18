package com.github.ksewen.yorozuya.common.context.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.*;

/**
 * @author ksewen
 * @date 13.10.2023 21:43
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServiceContextTest {

  private final ServiceContext context = ServiceContext.getInstance();

  private final String TEST_VALUE = "test";

  @BeforeAll
  void beforeAll() {
    this.context.put("testContext", this.TEST_VALUE);
    this.context.put("testBlankValue", "");
    this.context.put("testRemove", "remove");
  }

  @Order(1)
  @Test
  void put() {
    final String key = "testPut";
    this.context.put(key, this.TEST_VALUE);
    assertThat(this.context.contains(key));
    assertThat(this.context.get(key)).isEqualTo(this.TEST_VALUE);
    this.context.remove(key);
  }

  @Order(1)
  @Test
  void putAll() {
    final String key1 = "testPutAllItem1";
    final String key2 = "testPutAllItem2";
    final String key3 = "testPutAllItem3";
    Map<String, String> map = new HashMap<>();
    map.put(key1, this.TEST_VALUE + 1);
    map.put(key2, this.TEST_VALUE + 2);
    map.put(key3, this.TEST_VALUE + 3);
    this.context.putAll(map);

    assertThat(this.context.contains(key1)).isTrue();
    assertThat(this.context.get(key1)).isEqualTo(this.TEST_VALUE + 1);
    assertThat(this.context.contains(key2)).isTrue();
    assertThat(this.context.get(key2)).isEqualTo(this.TEST_VALUE + 2);
    assertThat(this.context.contains(key3)).isTrue();
    assertThat(this.context.get(key3)).isEqualTo(this.TEST_VALUE + 3);

    this.context.remove(key1);
    this.context.remove(key2);
    this.context.remove(key3);
  }

  @Order(1)
  @Test
  void get() {
    assertThat(this.context.get("testContext")).isEqualTo(this.TEST_VALUE);
  }

  @Order(1)
  @Test
  void remove() {
    assertThat(this.context.contains("testRemove")).isTrue();
    this.context.remove("testRemove");
    assertThat(this.context.contains("testRemove")).isFalse();
  }

  @Order(1)
  @Test
  void getContext() {
    assertThat(this.context.getContext())
        .isNotNull()
        .matches(c -> c.size() > 0)
        .matches(c -> c.containsKey("testContext"))
        .matches(c -> c.containsKey("testBlankValue"));
  }

  @Order(1)
  @Test
  void contains() {
    assertThat(this.context.contains("testContext")).isTrue();
    assertThat(this.context.contains("testBlankValue")).isTrue();
  }

  @Order(2)
  @Test
  void shutdown() {
    Map<String, String> tagsBeforeClear = this.context.getContext();
    this.context.shutdown();
    Map<String, String> tagsAfterClear = this.context.getContext();
    assertThat(this.context.getContext()).hasSize(0);
    assertThat(tagsAfterClear != tagsBeforeClear).isTrue();
  }
}
