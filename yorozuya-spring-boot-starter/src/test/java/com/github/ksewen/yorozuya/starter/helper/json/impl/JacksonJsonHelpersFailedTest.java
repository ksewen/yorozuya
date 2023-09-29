package com.github.ksewen.yorozuya.starter.helper.json.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import com.github.ksewen.yorozuya.common.exception.SerializationOrDeserializationException;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author ksewen
 * @date 29.09.2023 14:18
 */
@SpringBootTest(classes = JacksonJsonHelpers.class)
public class JacksonJsonHelpersFailedTest {

  @Autowired private JsonHelpers jsonHelpers;

  @MockBean private ObjectMapper objectMapper;

  private final String MESSAGE = "testMessage";

  private final String JSON = "testJson";

  private final String NAME_STRING = "test";

  private final Date NOW = new Date();

  private final JacksonJsonHelpersTest.TestObject VALUE =
      JacksonJsonHelpersTest.TestObject.builder().name(this.NAME_STRING).time(this.NOW).build();

  @Test
  void toObject() throws JsonProcessingException {
    when(this.objectMapper.readValue(anyString(), ArgumentMatchers.<Class<?>>any()))
        .thenThrow(this.mockJsonProcessingException());
    SerializationOrDeserializationException exception =
        assertThrows(
            SerializationOrDeserializationException.class,
            () -> this.jsonHelpers.toObject(this.JSON, JacksonJsonHelpersTest.TestObject.class));
    assertThat(exception)
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.equals(e.getCode()))
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR
                    .getMessage()
                    .equals(e.getMessage()));
  }

  @Test
  void testToObject() throws JsonProcessingException {
    when(this.objectMapper.readValue(anyString(), ArgumentMatchers.<TypeReference<Class<?>>>any()))
        .thenThrow(this.mockJsonProcessingException());
    SerializationOrDeserializationException exception =
        assertThrows(
            SerializationOrDeserializationException.class,
            () ->
                this.jsonHelpers.toObject(
                    this.JSON, new TypeReference<JacksonJsonHelpersTest.TestObject>() {}));
    assertThat(exception)
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.equals(e.getCode()))
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR
                    .getMessage()
                    .equals(e.getMessage()));
  }

  @Test
  void toMap() throws JsonProcessingException {
    when(this.objectMapper.readValue(
            anyString(), ArgumentMatchers.<TypeReference<Map<Object, Object>>>any()))
        .thenThrow(this.mockJsonProcessingException());
    SerializationOrDeserializationException exception =
        assertThrows(
            SerializationOrDeserializationException.class, () -> this.jsonHelpers.toMap(this.JSON));
    assertThat(exception)
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.equals(e.getCode()))
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR
                    .getMessage()
                    .equals(e.getMessage()));
  }

  @Test
  void toJsonString() throws JsonProcessingException {
    when(this.objectMapper.writeValueAsString(any())).thenThrow(this.mockJsonProcessingException());
    SerializationOrDeserializationException exception =
        assertThrows(
            SerializationOrDeserializationException.class,
            () ->
                this.jsonHelpers.toJsonString(
                    JacksonJsonHelpersTest.TestObject.builder()
                        .name(this.NAME_STRING)
                        .time(this.NOW)
                        .build()));
    assertThat(exception)
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.equals(e.getCode()))
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR
                    .getMessage()
                    .equals(e.getMessage()));
  }

  @Test
  void toObjectList() throws JsonProcessingException {
    when(this.objectMapper.readValue(anyString(), ArgumentMatchers.<TypeReference<List<?>>>any()))
        .thenThrow(this.mockJsonProcessingException());
    SerializationOrDeserializationException exception =
        assertThrows(
            SerializationOrDeserializationException.class,
            () ->
                this.jsonHelpers.toObjectList(
                    this.JSON, new TypeReference<List<JacksonJsonHelpersTest.TestObject>>() {}));
    assertThat(exception)
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.equals(e.getCode()))
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR
                    .getMessage()
                    .equals(e.getMessage()));
  }

  @Test
  void toJsonNode() throws JsonProcessingException {
    when(this.objectMapper.readTree(anyString())).thenThrow(this.mockJsonProcessingException());
    SerializationOrDeserializationException exception =
        assertThrows(
            SerializationOrDeserializationException.class,
            () -> this.jsonHelpers.toJsonNode(this.JSON));
    assertThat(exception)
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.equals(e.getCode()))
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR
                    .getMessage()
                    .equals(e.getMessage()));
  }

  @Test
  void testToJsonNode() throws IllegalArgumentException {
    when(this.objectMapper.valueToTree(any()))
        .thenThrow(new IllegalArgumentException(this.MESSAGE));
    SerializationOrDeserializationException exception =
        assertThrows(
            SerializationOrDeserializationException.class,
            () -> this.jsonHelpers.toJsonNode(this.VALUE));
    assertThat(exception)
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.equals(e.getCode()))
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR
                    .getMessage()
                    .equals(e.getMessage()));
  }

  @Test
  void toObject1() throws JsonProcessingException {
    when(this.objectMapper.readValue(anyString(), ArgumentMatchers.<Class<?>>any()))
        .thenThrow(this.mockJsonProcessingException());
    SerializationOrDeserializationException exception =
        assertThrows(
            SerializationOrDeserializationException.class,
            () ->
                this.jsonHelpers.toObject(
                    this.JSON, JacksonJsonHelpersTest.TestObject.class, this.objectMapper));
    assertThat(exception)
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.equals(e.getCode()))
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR
                    .getMessage()
                    .equals(e.getMessage()));
  }

  @Test
  void testToObject1() throws JsonProcessingException {
    when(this.objectMapper.readValue(anyString(), ArgumentMatchers.<TypeReference<Class<?>>>any()))
        .thenThrow(this.mockJsonProcessingException());
    SerializationOrDeserializationException exception =
        assertThrows(
            SerializationOrDeserializationException.class,
            () ->
                this.jsonHelpers.toObject(
                    this.JSON,
                    new TypeReference<JacksonJsonHelpersTest.TestObject>() {},
                    this.objectMapper));
    assertThat(exception)
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.equals(e.getCode()))
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR
                    .getMessage()
                    .equals(e.getMessage()));
  }

  @Test
  void toMap1() throws JsonProcessingException {
    when(this.objectMapper.readValue(
            anyString(), ArgumentMatchers.<TypeReference<Map<Object, Object>>>any()))
        .thenThrow(this.mockJsonProcessingException());
    SerializationOrDeserializationException exception =
        assertThrows(
            SerializationOrDeserializationException.class,
            () -> this.jsonHelpers.toMap(this.JSON, this.objectMapper));
    assertThat(exception)
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.equals(e.getCode()))
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR
                    .getMessage()
                    .equals(e.getMessage()));
  }

  @Test
  void toJsonString1() throws JsonProcessingException {
    when(this.objectMapper.writeValueAsString(any())).thenThrow(this.mockJsonProcessingException());
    SerializationOrDeserializationException exception =
        assertThrows(
            SerializationOrDeserializationException.class,
            () ->
                this.jsonHelpers.toJsonString(
                    JacksonJsonHelpersTest.TestObject.builder()
                        .name(this.NAME_STRING)
                        .time(this.NOW)
                        .build(),
                    this.objectMapper));
    assertThat(exception)
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.equals(e.getCode()))
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR
                    .getMessage()
                    .equals(e.getMessage()));
  }

  @Test
  void toObjectList1() throws JsonProcessingException {
    when(this.objectMapper.readValue(anyString(), ArgumentMatchers.<TypeReference<List<?>>>any()))
        .thenThrow(this.mockJsonProcessingException());
    SerializationOrDeserializationException exception =
        assertThrows(
            SerializationOrDeserializationException.class,
            () ->
                this.jsonHelpers.toObjectList(
                    this.JSON,
                    new TypeReference<List<JacksonJsonHelpersTest.TestObject>>() {},
                    this.objectMapper));
    assertThat(exception)
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.equals(e.getCode()))
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR
                    .getMessage()
                    .equals(e.getMessage()));
  }

  @Test
  void toJsonNode1() throws JsonProcessingException {
    when(this.objectMapper.readTree(anyString())).thenThrow(this.mockJsonProcessingException());
    SerializationOrDeserializationException exception =
        assertThrows(
            SerializationOrDeserializationException.class,
            () -> this.jsonHelpers.toJsonNode(this.JSON, this.objectMapper));
    assertThat(exception)
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.equals(e.getCode()))
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR
                    .getMessage()
                    .equals(e.getMessage()));
  }

  @Test
  void testToJsonNode1() throws IllegalArgumentException {
    when(this.objectMapper.valueToTree(any()))
        .thenThrow(new IllegalArgumentException(this.MESSAGE));
    SerializationOrDeserializationException exception =
        assertThrows(
            SerializationOrDeserializationException.class,
            () -> this.jsonHelpers.toJsonNode(this.VALUE, this.objectMapper));
    assertThat(exception)
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.equals(e.getCode()))
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR
                    .getMessage()
                    .equals(e.getMessage()));
  }

  private JsonProcessingException mockJsonProcessingException() {
    return new MockJsonProcessingException();
  }

  class MockJsonProcessingException extends JsonProcessingException {

    public MockJsonProcessingException() {
      super(MESSAGE);
    }
  }
}
