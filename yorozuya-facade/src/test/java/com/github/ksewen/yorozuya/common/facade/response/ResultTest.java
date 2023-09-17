package com.github.ksewen.yorozuya.common.facade.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

/**
 * @author ksewen
 * @date 03.09.2023 15:38
 */
class ResultTest {

  private String MESSAGE = "test message";

  private String DATA_NAME = "test";

  @Test
  void success() {
    Result result = Result.success();
    assertThat(result)
        .isNotNull()
        .matches(r -> r.isSuccess(), "it's failed")
        .matches(r -> DefaultResultCodeEnums.SUCCESS.getCode() == r.getCode(), "wrong code")
        .matches(
            r -> DefaultResultCodeEnums.SUCCESS.getMessage().equals(r.getMessage()),
            "wrong message")
        .matches(r -> r.getData() == null, "data is not null");
  }

  @Test
  void testSuccess() {
    Result result = Result.success(this.MESSAGE);
    assertThat(result)
        .isNotNull()
        .matches(r -> r.isSuccess(), "it's failed")
        .matches(r -> DefaultResultCodeEnums.SUCCESS.getCode() == r.getCode(), "wrong code")
        .matches(r -> this.MESSAGE.equals(r.getMessage()), "wrong message")
        .matches(r -> r.getData() == null, "data is not null");
  }

  @Test
  void testSuccess1() {
    TestData data = TestData.builder().name(this.DATA_NAME).build();
    Result<TestData> result = Result.success(data);
    assertThat(result)
        .isNotNull()
        .matches(r -> r.isSuccess(), "it's failed")
        .matches(r -> DefaultResultCodeEnums.SUCCESS.getCode() == r.getCode(), "wrong code")
        .matches(
            r -> DefaultResultCodeEnums.SUCCESS.getMessage().equals(r.getMessage()),
            "wrong message")
        .matches(r -> r.getData() != null, "data is null")
        .matches(r -> this.DATA_NAME.equals(r.getData().getName()), "wrong name of the data");
  }

  @Test
  void testSuccess2() {
    TestData data = TestData.builder().name(this.DATA_NAME).build();
    Result<TestData> result = Result.success(this.MESSAGE, data);
    assertThat(result)
        .isNotNull()
        .matches(r -> r.isSuccess(), "it's failed")
        .matches(r -> DefaultResultCodeEnums.SUCCESS.getCode() == r.getCode(), "wrong code")
        .matches(r -> this.MESSAGE.equals(r.getMessage()), "wrong message")
        .matches(r -> r.getData() != null, "data is null")
        .matches(r -> this.DATA_NAME.equals(r.getData().getName()), "wrong name of the data");
  }

  @Test
  void operationFailed() {
    Result result = Result.operationFailed();
    assertThat(result)
        .isNotNull()
        .matches(r -> !r.isSuccess(), "it's failed")
        .matches(
            r -> DefaultResultCodeEnums.OPERATION_FAILED.getCode() == r.getCode(), "wrong code")
        .matches(
            r -> DefaultResultCodeEnums.OPERATION_FAILED.getMessage().equals(r.getMessage()),
            "wrong message")
        .matches(r -> r.getData() == null, "data is not null");
  }

  @Test
  void testOperationFailed() {
    Result result = Result.operationFailed(this.MESSAGE);
    assertThat(result)
        .isNotNull()
        .matches(r -> !r.isSuccess(), "it's success")
        .matches(
            r -> DefaultResultCodeEnums.OPERATION_FAILED.getCode() == r.getCode(), "wrong code")
        .matches(r -> this.MESSAGE.equals(r.getMessage()), "wrong message")
        .matches(r -> r.getData() == null, "data is not null");
  }

  @Test
  void systemError() {
    Result result = Result.systemError();
    assertThat(result)
        .isNotNull()
        .matches(r -> !r.isSuccess(), "it's success")
        .matches(r -> DefaultResultCodeEnums.SYSTEM_ERROR.getCode() == r.getCode(), "wrong code")
        .matches(
            r -> DefaultResultCodeEnums.SYSTEM_ERROR.getMessage().equals(r.getMessage()),
            "wrong message")
        .matches(r -> r.getData() == null, "data is not null");
  }

  @Test
  void testSystemError() {
    Result result = Result.systemError(this.MESSAGE);
    assertThat(result)
        .isNotNull()
        .matches(r -> !r.isSuccess(), "it's success")
        .matches(r -> DefaultResultCodeEnums.SYSTEM_ERROR.getCode() == r.getCode(), "wrong code")
        .matches(r -> this.MESSAGE.equals(r.getMessage()), "wrong message")
        .matches(r -> r.getData() == null, "data is not null");
  }

  @Test
  void paramInvalid() {
    Result result = Result.paramInvalid();
    assertThat(result)
        .isNotNull()
        .matches(r -> !r.isSuccess(), "it's success")
        .matches(r -> DefaultResultCodeEnums.PARAM_INVALID.getCode() == r.getCode(), "wrong code")
        .matches(
            r -> DefaultResultCodeEnums.PARAM_INVALID.getMessage().equals(r.getMessage()),
            "wrong message")
        .matches(r -> r.getData() == null, "data is not null");
  }

  @Test
  void testParamInvalid() {
    Result result = Result.paramInvalid(this.MESSAGE);
    assertThat(result)
        .isNotNull()
        .matches(r -> !r.isSuccess(), "it's success")
        .matches(r -> DefaultResultCodeEnums.PARAM_INVALID.getCode() == r.getCode(), "wrong code")
        .matches(r -> this.MESSAGE.equals(r.getMessage()), "wrong message")
        .matches(r -> r.getData() == null, "data is not null");
  }

  @Test
  void testResult() {
    TestData data = TestData.builder().name(this.DATA_NAME).build();
    Result<TestData> result =
        Result.<TestData>builder()
            .code(100)
            .success(Boolean.TRUE)
            .message(this.MESSAGE)
            .data(data)
            .build();
    assertThat(result)
        .isNotNull()
        .matches(r -> r.isSuccess(), "it's failed")
        .matches(r -> r.getCode() == 100, "wrong code")
        .matches(r -> this.MESSAGE.equals(r.getMessage()), "wrong message")
        .matches(r -> r.getData() != null, "data is null")
        .matches(r -> this.DATA_NAME.equals(r.getData().getName()), "wrong name of the data");
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  static class TestData {

    @Builder.Default private String name = "test";
  }
}
