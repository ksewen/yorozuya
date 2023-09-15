package com.github.ksewen.yorozuya.starter.configuration.handler;

import com.github.ksewen.yorozuya.common.enums.ResultCode;
import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import com.github.ksewen.yorozuya.common.exception.CommonException;
import com.github.ksewen.yorozuya.common.exception.InvalidParamException;
import com.github.ksewen.yorozuya.common.facade.response.Result;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
  public Result handleBindException(BindException exception) {
    BindingResult bindingResult = exception.getBindingResult();
    if (null == bindingResult || null == bindingResult.getFieldError()) {
      return Result.paramInvalid();
    }
    return Result.paramInvalid(bindingResult.getFieldError().getDefaultMessage());
  }

  @ExceptionHandler(value = {ConstraintViolationException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Result handleConstraintViolationException(ConstraintViolationException exception) {
    return Result.paramInvalid(exception.getMessage());
  }

  @ExceptionHandler(value = {CallNotPermittedException.class})
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public Result handleCallNotPermittedException(CallNotPermittedException exception) {
    return Result.builder()
        .code(DefaultResultCodeEnums.CIRCUIT_BREAKER_NO_FALLBACK_METHOD.getCode())
        .message(
            StringUtils.hasLength(exception.getMessage())
                ? exception.getMessage()
                : DefaultResultCodeEnums.CIRCUIT_BREAKER_NO_FALLBACK_METHOD.getMessage())
        .build();
  }

  @ExceptionHandler(value = {RequestNotPermitted.class})
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public Result handleRequestNotPermitted(RequestNotPermitted exception) {
    return Result.builder()
        .code(DefaultResultCodeEnums.RATE_LIMITED_NO_FALLBACK_METHOD.getCode())
        .message(
            StringUtils.hasLength(exception.getMessage())
                ? exception.getMessage()
                : DefaultResultCodeEnums.RATE_LIMITED_NO_FALLBACK_METHOD.getMessage())
        .build();
  }

  @ExceptionHandler(value = InvalidParamException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Result handleInvalidParamException(InvalidParamException exception) {
    ResultCode code = exception.getCode();
    return code != null
        ? Result.builder().code(code.getCode()).message(exception.getMessage()).build()
        : Result.systemError();
  }

  @ExceptionHandler(value = CommonException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public Result handleCommonException(CommonException exception) {
    ResultCode code = exception.getCode();
    return code != null
        ? Result.builder().code(code.getCode()).message(exception.getMessage()).build()
        : Result.systemError();
  }

  @ExceptionHandler(value = Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public Result handleException(Exception exception) {
    log.error("BasicExceptionHandler catch a global exception", exception);
    return Result.systemError();
  }
}
