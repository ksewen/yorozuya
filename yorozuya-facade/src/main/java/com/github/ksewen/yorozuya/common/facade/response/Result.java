package com.github.ksewen.yorozuya.common.facade.response;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import lombok.Builder;
import lombok.Data;

/**
 * @author ksewen
 * @date 03.09.2023 14:53
 */
@Data
@Builder
public class Result<T> {

  private int code;

  private String message;

  @Builder.Default private boolean success = Boolean.FALSE;

  private T data;

  public static Result success() {
    return Result.builder()
        .code(DefaultResultCodeEnums.SUCCESS.getCode())
        .message(DefaultResultCodeEnums.SUCCESS.getMessage())
        .success(Boolean.TRUE)
        .build();
  }

  public static Result success(String message) {
    return Result.builder()
        .code(DefaultResultCodeEnums.SUCCESS.getCode())
        .message(message)
        .success(Boolean.TRUE)
        .build();
  }

  public static <T> Result<T> success(T data) {
    return Result.<T>builder()
        .code(DefaultResultCodeEnums.SUCCESS.getCode())
        .message(DefaultResultCodeEnums.SUCCESS.getMessage())
        .success(Boolean.TRUE)
        .data(data)
        .build();
  }

  public static <T> Result<T> success(String message, T data) {
    return Result.<T>builder()
        .code(DefaultResultCodeEnums.SUCCESS.getCode())
        .message(message)
        .success(Boolean.TRUE)
        .data(data)
        .build();
  }

  public static Result operationFailed() {
    return Result.builder()
        .code(DefaultResultCodeEnums.OPERATION_FAILED.getCode())
        .message(DefaultResultCodeEnums.OPERATION_FAILED.getMessage())
        .build();
  }

  public static Result operationFailed(String message) {
    return Result.builder()
        .code(DefaultResultCodeEnums.OPERATION_FAILED.getCode())
        .message(message)
        .build();
  }

  public static Result systemError() {
    return Result.builder()
        .code(DefaultResultCodeEnums.SYSTEM_ERROR.getCode())
        .message(DefaultResultCodeEnums.SYSTEM_ERROR.getMessage())
        .build();
  }

  public static Result systemError(String message) {
    return Result.builder()
        .code(DefaultResultCodeEnums.SYSTEM_ERROR.getCode())
        .message(message)
        .build();
  }

  public static Result paramInvalid() {
    return Result.builder()
        .code(DefaultResultCodeEnums.PARAM_INVALID.getCode())
        .message(DefaultResultCodeEnums.PARAM_INVALID.getMessage())
        .build();
  }

  public static Result paramInvalid(String message) {
    return Result.builder()
        .code(DefaultResultCodeEnums.PARAM_INVALID.getCode())
        .message(message)
        .build();
  }
}
