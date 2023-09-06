package com.github.ksewen.yorozuya.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import org.junit.jupiter.api.Test;

/**
 * @author ksewen
 * @date 04.09.2023 10:33
 */
class CommonExceptionTest {

  private String MESSAGE = "test";

  @Test
  void getCode() {
    CommonException exception = new CommonException();
    assertThat(exception)
        .isNotNull()
        .matches(e -> DefaultResultCodeEnums.SYSTEM_ERROR == e.getCode(), "wrong code")
        .matches(
            e -> DefaultResultCodeEnums.SYSTEM_ERROR.getCode() == e.getCode().getCode(),
            "wrong code")
        .matches(
            e -> DefaultResultCodeEnums.SYSTEM_ERROR.getMessage().equals(e.getMessage()),
            "wrong message");
  }

  @Test
  void testGetCode() {
    CommonException exception = new CommonException(DefaultResultCodeEnums.ALREADY_EXIST);
    assertThat(exception)
        .isNotNull()
        .matches(e -> DefaultResultCodeEnums.ALREADY_EXIST == e.getCode(), "wrong code")
        .matches(
            e -> DefaultResultCodeEnums.ALREADY_EXIST.getCode() == e.getCode().getCode(),
            "wrong code")
        .matches(
            e -> DefaultResultCodeEnums.ALREADY_EXIST.getMessage().equals(e.getMessage()),
            "wrong message");
  }

  @Test
  void testGetCode1() {
    CommonException exception = new CommonException(this.MESSAGE);
    assertThat(exception)
        .isNotNull()
        .matches(e -> DefaultResultCodeEnums.SYSTEM_ERROR == e.getCode(), "wrong code")
        .matches(
            e -> DefaultResultCodeEnums.SYSTEM_ERROR.getCode() == e.getCode().getCode(),
            "wrong code")
        .matches(e -> this.MESSAGE.equals(e.getMessage()), "wrong message");
  }

  @Test
  void testGetCode2() {
    CommonException exception =
        new CommonException(DefaultResultCodeEnums.PARAM_INVALID, this.MESSAGE);
    assertThat(exception)
        .isNotNull()
        .matches(e -> DefaultResultCodeEnums.PARAM_INVALID == e.getCode(), "wrong code")
        .matches(
            e -> DefaultResultCodeEnums.PARAM_INVALID.getCode() == e.getCode().getCode(),
            "wrong code")
        .matches(e -> this.MESSAGE.equals(e.getMessage()), "wrong message");
  }
}
