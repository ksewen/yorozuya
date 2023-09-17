package com.github.ksewen.yorozuya.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import org.junit.jupiter.api.Test;

/**
 * @author ksewen
 * @date 04.09.2023 10:41
 */
class InvalidParamExceptionTest {

  private String MESSAGE = "test";

  @Test
  void getCode() {
    InvalidParamException exception = new InvalidParamException();
    assertThat(exception)
        .isNotNull()
        .matches(e -> DefaultResultCodeEnums.PARAM_INVALID == e.getCode(), "wrong code")
        .matches(
            e -> DefaultResultCodeEnums.PARAM_INVALID.getCode() == e.getCode().getCode(),
            "wrong code")
        .matches(
            e -> DefaultResultCodeEnums.PARAM_INVALID.getMessage().equals(e.getMessage()),
            "wrong message");
  }

  @Test
  void getCode1() {
    InvalidParamException exception = new InvalidParamException(this.MESSAGE);
    assertThat(exception)
        .isNotNull()
        .matches(e -> DefaultResultCodeEnums.PARAM_INVALID == e.getCode(), "wrong code")
        .matches(
            e -> DefaultResultCodeEnums.PARAM_INVALID.getCode() == e.getCode().getCode(),
            "wrong code")
        .matches(e -> this.MESSAGE.equals(e.getMessage()), "wrong message");
  }
}
