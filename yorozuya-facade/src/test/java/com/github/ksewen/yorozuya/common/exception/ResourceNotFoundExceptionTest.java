package com.github.ksewen.yorozuya.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import org.junit.jupiter.api.Test;

/**
 * @author ksewen
 * @date 27.10.2023 15:33
 */
class ResourceNotFoundExceptionTest {

  private String MESSAGE = "test";

  @Test
  void getCode() {
    ResourceNotFoundException exception = new ResourceNotFoundException();
    assertThat(exception)
        .isNotNull()
        .matches(e -> DefaultResultCodeEnums.NOT_FOUND == e.getCode(), "wrong code")
        .matches(
            e -> DefaultResultCodeEnums.NOT_FOUND.getCode() == e.getCode().getCode(), "wrong code")
        .matches(
            e -> DefaultResultCodeEnums.NOT_FOUND.getMessage().equals(e.getMessage()),
            "wrong message");
  }

  @Test
  void getCode1() {
    ResourceNotFoundException exception = new ResourceNotFoundException(this.MESSAGE);
    assertThat(exception)
        .isNotNull()
        .matches(e -> DefaultResultCodeEnums.NOT_FOUND == e.getCode(), "wrong code")
        .matches(
            e -> DefaultResultCodeEnums.NOT_FOUND.getCode() == e.getCode().getCode(), "wrong code")
        .matches(e -> this.MESSAGE.equals(e.getMessage()), "wrong message");
  }
}
