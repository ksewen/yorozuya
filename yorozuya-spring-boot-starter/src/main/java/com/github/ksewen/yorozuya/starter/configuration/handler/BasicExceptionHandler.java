package com.github.ksewen.yorozuya.starter.configuration.handler;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import com.github.ksewen.yorozuya.common.exception.CommonException;
import com.github.ksewen.yorozuya.common.exception.InvalidParamException;
import com.github.ksewen.yorozuya.common.facade.response.Result;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

/**
 * @author ksewen
 * @date 04.09.2023 10:51
 */
@RestControllerAdvice
@Slf4j
public class BasicExceptionHandler {

  @ExceptionHandler(value = {BindException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Result<?> handleBindException(BindException exception) {
    BindingResult bindingResult = exception.getBindingResult();
    if (bindingResult.getFieldError() == null) {
      return Result.paramInvalid();
    }
    return Result.paramInvalid(bindingResult.getFieldError().getDefaultMessage());
  }

  @ExceptionHandler(value = {ConstraintViolationException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Result<?> handleConstraintViolationException(ConstraintViolationException exception) {
    return Result.paramInvalid(exception.getMessage());
  }

  @ExceptionHandler(value = ConnectException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public Result<?> handleConnectException(ConnectException exception) {
    return Result.builder()
        .code(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getCode())
        .message(
            StringUtils.hasLength(exception.getMessage())
                ? exception.getMessage()
                : DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getMessage())
        .build();
  }

  @ExceptionHandler(value = SocketTimeoutException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public Result<?> handleSocketTimeoutException(SocketTimeoutException exception) {
    return Result.builder()
        .code(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getCode())
        .message(
            StringUtils.hasLength(exception.getMessage())
                ? exception.getMessage()
                : DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getMessage())
        .build();
  }

  @ExceptionHandler(value = ResourceAccessException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public Result<?> handleResourceAccessException(ResourceAccessException exception) {
    return Result.builder()
        .code(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getCode())
        .message(
            StringUtils.hasLength(exception.getMessage())
                ? exception.getMessage()
                : DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getMessage())
        .build();
  }

  @ExceptionHandler(value = FeignException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public Result<?> handleFeignException(FeignException exception) {
    return Result.builder()
        .code(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getCode())
        .message(
            StringUtils.hasLength(exception.getMessage())
                ? exception.getMessage()
                : DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getMessage())
        .build();
  }

  @ExceptionHandler(value = {CallNotPermittedException.class})
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public Result<?> handleCallNotPermittedException(CallNotPermittedException exception) {
    return Result.builder()
        .code(DefaultResultCodeEnums.CIRCUIT_BREAKER_NO_FALLBACK_METHOD.getCode())
        .message(exception.getMessage())
        .build();
  }

  @ExceptionHandler(value = {RequestNotPermitted.class})
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public Result<?> handleRequestNotPermitted(RequestNotPermitted exception) {
    return Result.builder()
        .code(DefaultResultCodeEnums.RATE_LIMITED_NO_FALLBACK_METHOD.getCode())
        .message(exception.getMessage())
        .build();
  }

  @ExceptionHandler(value = InvalidParamException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Result<?> handleInvalidParamException(InvalidParamException exception) {
    return Result.builder()
        .code(exception.getCode().getCode())
        .message(exception.getMessage())
        .build();
  }

  @ExceptionHandler(value = CommonException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public Result<?> handleCommonException(CommonException exception) {
    return Result.builder()
        .code(exception.getCode().getCode())
        .message(exception.getMessage())
        .build();
  }

  @ExceptionHandler(value = Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public Result<?> handleException(Exception exception) {
    log.error("BasicExceptionHandler catch a global exception", exception);
    return Result.systemError();
  }
}
