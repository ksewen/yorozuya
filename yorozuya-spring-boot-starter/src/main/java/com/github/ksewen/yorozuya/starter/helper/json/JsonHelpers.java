package com.github.ksewen.yorozuya.starter.helper.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

/**
 * @author ksewen
 * @date 24.09.2023 13:40
 */
public interface JsonHelpers {

  <T> T toObject(String string, Class<T> tClass);

  <T> T toObject(String string, TypeReference<T> valueTypeRef);

  Map toMap(String string);

  String toJsonString(Object object);

  <T> List<T> toObjectList(String string, TypeReference<List<T>> typeReference);

  JsonNode toJsonNode(String string);

  JsonNode toJsonNode(Object object);

  <T> T toObject(String string, Class<T> tClass, ObjectMapper objectMapper);

  <T> T toObject(String string, TypeReference<T> valueTypeRef, ObjectMapper objectMapper);

  Map toMap(String string, ObjectMapper objectMapper);

  String toJsonString(Object object, ObjectMapper objectMapper);

  <T> List<T> toObjectList(
      String string, TypeReference<List<T>> typeReference, ObjectMapper objectMapper);

  JsonNode toJsonNode(String string, ObjectMapper objectMapper);

  JsonNode toJsonNode(Object object, ObjectMapper objectMapper);
}
