package com.github.ksewen.yorozuya.starter.helper.json.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ksewen.yorozuya.common.exception.SerializationOrDeserializationException;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ksewen
 * @date 26.09.2023 22:39
 */
@RequiredArgsConstructor
@Slf4j
public class JacksonJsonHelpers implements JsonHelpers {

  private final ObjectMapper DEFAULT_OBJECT_MAPPER;

  @Override
  public <T> T toObject(String string, Class<T> tClass) {
    try {
      return this.DEFAULT_OBJECT_MAPPER.readValue(string, tClass);
    } catch (JsonProcessingException e) {
      log.error("deserialization to object failed, text: {}", string, e);
      throw new SerializationOrDeserializationException();
    }
  }

  @Override
  public <T> T toObject(String string, TypeReference<T> valueTypeRef) {
    try {
      return this.DEFAULT_OBJECT_MAPPER.readValue(string, valueTypeRef);
    } catch (JsonProcessingException e) {
      log.error("deserialization to object failed, text: {}", string, e);
      throw new SerializationOrDeserializationException();
    }
  }

  @Override
  public Map<Object, Object> toMap(String string) {
    try {
      return this.DEFAULT_OBJECT_MAPPER.readValue(string, new TypeReference<>() {});
    } catch (JsonProcessingException e) {
      log.error("deserialization to object failed, text: {}", string, e);
      throw new SerializationOrDeserializationException();
    }
  }

  @Override
  public String toJsonString(Object object) {
    try {
      return this.DEFAULT_OBJECT_MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("serialization to json string failed, object type: {}", object.getClass(), e);
      throw new SerializationOrDeserializationException();
    }
  }

  @Override
  public <T> List<T> toObjectList(String string, TypeReference<List<T>> typeReference) {
    try {
      return this.DEFAULT_OBJECT_MAPPER.readValue(string, typeReference);
    } catch (Exception e) {
      log.error("deserialization to object list failed, text: {}", string, e);
      throw new SerializationOrDeserializationException();
    }
  }

  @Override
  public JsonNode toJsonNode(String string) {
    try {
      return this.DEFAULT_OBJECT_MAPPER.readTree(string);
    } catch (JsonProcessingException e) {
      log.error("deserialization to json node failed, text: {}", string, e);
      throw new SerializationOrDeserializationException();
    }
  }

  @Override
  public JsonNode toJsonNode(Object object) {
    try {
      return this.DEFAULT_OBJECT_MAPPER.valueToTree(object);
    } catch (IllegalArgumentException e) {
      log.error("parse to json node failed, object type: {}", object.getClass(), e);
      throw new SerializationOrDeserializationException();
    }
  }

  @Override
  public <T> T toObject(String string, Class<T> tClass, ObjectMapper objectMapper) {
    try {
      return objectMapper.readValue(string, tClass);
    } catch (JsonProcessingException e) {
      log.error("deserialization to object failed, text: {}", string, e);
      throw new SerializationOrDeserializationException();
    }
  }

  @Override
  public <T> T toObject(String string, TypeReference<T> valueTypeRef, ObjectMapper objectMapper) {
    try {
      return objectMapper.readValue(string, valueTypeRef);
    } catch (JsonProcessingException e) {
      log.error("deserialization to object failed, text: {}", string, e);
      throw new SerializationOrDeserializationException();
    }
  }

  @Override
  public Map<Object, Object> toMap(String string, ObjectMapper objectMapper) {
    try {
      return objectMapper.readValue(string, new TypeReference<>() {});
    } catch (JsonProcessingException e) {
      log.error("deserialization to object failed, text: {}", string, e);
      throw new SerializationOrDeserializationException();
    }
  }

  @Override
  public String toJsonString(Object object, ObjectMapper objectMapper) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("serialization to json string failed, object type: {}", object.getClass(), e);
      throw new SerializationOrDeserializationException();
    }
  }

  @Override
  public <T> List<T> toObjectList(
      String string, TypeReference<List<T>> typeReference, ObjectMapper objectMapper) {
    try {
      return objectMapper.readValue(string, typeReference);
    } catch (Exception e) {
      log.error("deserialization to object list failed, text: {}", string, e);
      throw new SerializationOrDeserializationException();
    }
  }

  @Override
  public JsonNode toJsonNode(String string, ObjectMapper objectMapper) {
    try {
      return objectMapper.readTree(string);
    } catch (JsonProcessingException e) {
      log.error("deserialization to json node failed, text: {}", string, e);
      throw new SerializationOrDeserializationException();
    }
  }

  @Override
  public JsonNode toJsonNode(Object object, ObjectMapper objectMapper) {
    try {
      return objectMapper.valueToTree(object);
    } catch (IllegalArgumentException e) {
      log.error("parse to json node failed, object type: {}", object.getClass(), e);
      throw new SerializationOrDeserializationException();
    }
  }
}
