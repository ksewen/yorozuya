package com.github.ksewen.yorozuya.starter.aspect.logger.impl;

import com.github.ksewen.yorozuya.common.environment.Environment;
import com.github.ksewen.yorozuya.starter.annotation.logger.LoggerTrace;
import com.github.ksewen.yorozuya.starter.aspect.logger.AroundLogger;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

/**
 * @author ksewen
 * @date 05.10.2023 17:16
 */
@Slf4j
@RequiredArgsConstructor
public class BasicAroundLogger implements AroundLogger {

  private final Environment environment;

  private final JsonHelpers jsonHelpers;

  protected void before(JoinPoint joinPoint) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Operation [{}] in service [{}] starts: with arguments: {}",
          this.getOperationDescription(joinPoint),
          this.getApplicationName(),
          this.parseArgumentsOrResult(joinPoint.getArgs()));
    }
  }

  protected void afterThrowingAdvice(JoinPoint joinPoint, Throwable throwable) {
    log.error(
        "Operation [{}] in service [{}] throws exception of type: {} and message: {}",
        this.getOperationDescription(joinPoint),
        this.getApplicationName(),
        throwable.getClass(),
        throwable.getMessage(),
        throwable);
  }

  protected void afterReturningAdvice(JoinPoint joinPoint, Object result) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Operation [{}] in service [{}] finish: with result: {}",
          this.getOperationDescription(joinPoint),
          this.getApplicationName(),
          this.parseArgumentsOrResult(result));
    }
  }

  @Override
  public String getApplicationName() {
    return this.environment.getApplicationName();
  }

  @Override
  public String getOperationDescription(JoinPoint joinPoint) {
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    LoggerTrace loggerTrace = method.getAnnotation(LoggerTrace.class);

    if (loggerTrace != null && StringUtils.hasLength(loggerTrace.value())) {
      return loggerTrace.value();
    }

    return new StringBuilder(joinPoint.getSignature().getDeclaringTypeName())
        .append(".")
        .append(joinPoint.getSignature().getName())
        .toString();
  }

  @Override
  public String parseArgumentsOrResult(Object object) {
    return this.jsonHelpers.toJsonString(object);
  }
}
