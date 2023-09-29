package com.github.ksewen.yorozuya.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import org.junit.jupiter.api.Test;

/**
 * @author ksewen
 * @date 26.09.2023 22:38
 */
class SerializationOrDeserializationExceptionTest {
  private String MESSAGE = "test";

  @Test
  void getCode() {
    SerializationOrDeserializationException exception =
        new SerializationOrDeserializationException();
    assertThat(exception)
        .isNotNull()
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR == e.getCode(),
            "wrong code")
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.getCode()
                    == e.getCode().getCode(),
            "wrong code")
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR
                    .getMessage()
                    .equals(e.getMessage()),
            "wrong message");
  }

  @Test
  void getCode1() {
    SerializationOrDeserializationException exception =
        new SerializationOrDeserializationException(this.MESSAGE);
    assertThat(exception)
        .isNotNull()
        .matches(
            e -> DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR == e.getCode(),
            "wrong code")
        .matches(
            e ->
                DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR.getCode()
                    == e.getCode().getCode(),
            "wrong code")
        .matches(e -> this.MESSAGE.equals(e.getMessage()), "wrong message");
  }
}
