package com.github.ksewen.yorozuya.starter.aspect.logger.impl;

import com.github.ksewen.yorozuya.common.environment.Environment;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * @author ksewen
 * @date 05.10.2023 18:06
 */
@Aspect
@Slf4j
public class NotRecordByDefaultAroundLogger extends BasicAroundLogger {

  public NotRecordByDefaultAroundLogger(Environment environment, JsonHelpers jsonHelpers) {
    super(environment, jsonHelpers);
  }

  @Pointcut("@annotation(com.github.ksewen.yorozuya.starter.annotation.logger.LoggerTrace))")
  void pointCut() {}

  @Before("pointCut()")
  void doBefore(JoinPoint joinPoint) {
    super.before(joinPoint);
  }

  @AfterThrowing(value = "pointCut()", throwing = "throwable")
  void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable throwable) {
    super.afterThrowingAdvice(joinPoint, throwable);
  }

  @AfterReturning(value = "pointCut()", returning = "result")
  void doAfterReturningAdvice(JoinPoint joinPoint, Object result) {
    super.afterReturningAdvice(joinPoint, result);
  }
}
