package com.github.ksewen.yorozuya.common.enums.impl;

import com.github.ksewen.yorozuya.common.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author ksewen
 * @date 03.09.2023 14:47
 */
@Getter
@ToString
@AllArgsConstructor
// TODO: reorganizing common error codes.
public enum DefaultResultCodeEnums implements ResultCode {
  SUCCESS(20000, "success"),

  PARAM_INVALID(40000, "parameter invalid"),
  ALREADY_EXIST(40001, "the record ist already exist"),
  NOT_FOUND(44000, "can not find the record"),

  UNAUTHORIZED(41000, "invalid username or password"),
  TOKEN_EXPIRED(41001, "expired token"),
  ACCESS_DENIED(43000, "permission denied"),

  SYSTEM_ERROR(50000, "system error"),
  OPERATION_FAILED(50001, "operation failed"),
  CIRCUIT_BREAKER_NO_FALLBACK_METHOD(
      50002, "circuit breaker is opened but not fallback method available"),
  RATE_LIMITED_NO_FALLBACK_METHOD(
      50003, "rate limiter does not permit further calls but not fallback method available"),
  REMOTE_CALL_FAILURE(50004, "exception occurred during remote service call"),

  SERIALIZATION_OR_DESERIALIZATION_ERROR(51001, "serialization or deserialization failed"),
  COPY_PROPERTIES_ERROR(51002, "copy properties failed");

  private int code;

  private String message;

  @Override
  public int getCode() {
    return this.code;
  }

  @Override
  public String getMessage() {
    return this.message;
  }
}
