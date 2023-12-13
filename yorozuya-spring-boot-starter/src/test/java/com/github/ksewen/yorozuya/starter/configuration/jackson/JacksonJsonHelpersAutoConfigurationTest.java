package com.github.ksewen.yorozuya.starter.configuration.jackson;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ksewen.yorozuya.starter.configuration.redis.RedisHelpersAutoConfiguration;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import com.github.ksewen.yorozuya.starter.helper.json.impl.JacksonJsonHelpers;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 26.09.2023 22:59
 */
class JacksonJsonHelpersAutoConfigurationTest {

  @Test
  void jacksonJsonHelpers() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                JacksonJsonHelpersAutoConfiguration.class, JacksonAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(JsonHelpers.class);
              assertThat(context)
                  .getBean("jacksonJsonHelpers")
                  .isSameAs(context.getBean(JsonHelpers.class));
            });
  }

  @Test
  void nonJacksonJsonHelpers() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                RedisHelpersAutoConfiguration.class, MockJsonHelpersAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(JacksonJsonHelpers.class);
            });
  }

  @Configuration(proxyBeanMethods = false)
  static class MockJsonHelpersAutoConfiguration {

    @Bean
    public JsonHelpers jsonHelpers() {
      return new MockJsonHelpers();
    }

    class MockJsonHelpers implements JsonHelpers {
      @Override
      public <T> T toObject(String string, Class<T> tClass) {
        return null;
      }

      @Override
      public <T> T toObject(String string, TypeReference<T> valueTypeRef) {
        return null;
      }

      @Override
      public Map toMap(String string) {
        return null;
      }

      @Override
      public String toJsonString(Object object) {
        return null;
      }

      @Override
      public <T> List<T> toObjectList(String string, TypeReference<List<T>> typeReference) {
        return null;
      }

      @Override
      public JsonNode toJsonNode(String string) {
        return null;
      }

      @Override
      public JsonNode toJsonNode(Object object) {
        return null;
      }

      @Override
      public <T> T toObject(String string, Class<T> tClass, ObjectMapper objectMapper) {
        return null;
      }

      @Override
      public <T> T toObject(
          String string, TypeReference<T> valueTypeRef, ObjectMapper objectMapper) {
        return null;
      }

      @Override
      public Map toMap(String string, ObjectMapper objectMapper) {
        return null;
      }

      @Override
      public String toJsonString(Object object, ObjectMapper objectMapper) {
        return null;
      }

      @Override
      public <T> List<T> toObjectList(
          String string, TypeReference<List<T>> typeReference, ObjectMapper objectMapper) {
        return null;
      }

      @Override
      public JsonNode toJsonNode(String string, ObjectMapper objectMapper) {
        return null;
      }

      @Override
      public JsonNode toJsonNode(Object object, ObjectMapper objectMapper) {
        return null;
      }
    }
  }
}
