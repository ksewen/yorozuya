package com.github.ksewen.yorozuya.starter.aspect.logger.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.github.ksewen.yorozuya.common.constant.SystemConstants;
import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import com.github.ksewen.yorozuya.common.environment.impl.BasicEnvironment;
import com.github.ksewen.yorozuya.common.exception.InvalidParamException;
import com.github.ksewen.yorozuya.starter.annotation.logger.LoggerTrace;
import com.github.ksewen.yorozuya.starter.configuration.aspect.AroundLoggerAutoConfiguration;
import com.github.ksewen.yorozuya.starter.helper.json.impl.JacksonJsonHelpers;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.stereotype.Service;
import org.springframework.test.util.AopTestUtils;

/**
 * @author ksewen
 * @date 06.10.2023 17:56
 */
@SpringBootTest(
    classes = {
      AopAutoConfiguration.class,
      AroundLoggerAutoConfiguration.class,
      NotRecordByDefaultAroundLoggerTest.MockService.class
    },
    properties = {
      "common.aspect.logger.by.default.enable=false",
      "logging.level.com.github.ksewen.yorozuya.starter.aspect.logger.impl=DEBUG"
    })
public class NotRecordByDefaultAroundLoggerTest {

  @SpyBean private NotRecordByDefaultAroundLogger aroundAspectLogger;

  @SuppressWarnings("unused")
  @MockBean
  private BasicEnvironment environment;

  @SuppressWarnings("unused")
  @MockBean
  private JacksonJsonHelpers jsonHelpers;

  @Autowired private NotRecordByDefaultAroundLoggerTest.MockService mockService;

  @Test
  void testAspect() {
    assertNotEquals(
        this.mockService.getClass(), NotRecordByDefaultAroundLoggerTest.MockService.class);
    assertTrue(AopUtils.isAopProxy(this.mockService));
    assertTrue(AopUtils.isCglibProxy(this.mockService));
    assertEquals(
        AopProxyUtils.ultimateTargetClass(this.mockService),
        NotRecordByDefaultAroundLoggerTest.MockService.class);
    assertEquals(
        AopTestUtils.getTargetObject(this.mockService).getClass(),
        NotRecordByDefaultAroundLoggerTest.MockService.class);
    assertEquals(
        AopTestUtils.getUltimateTargetObject(this.mockService).getClass(),
        NotRecordByDefaultAroundLoggerTest.MockService.class);
  }

  @Test
  void returnWithArgument() {
    final String argument = "a";
    String result = this.mockService.getWithArgument(argument);
    assertThat(result).isEqualTo(argument);

    verify(this.aroundAspectLogger, times(1)).doBefore(any(JoinPoint.class));
    verify(this.aroundAspectLogger, times(1))
        .doAfterReturningAdvice(any(JoinPoint.class), eq(argument));
    verify(this.aroundAspectLogger, never())
        .doAfterThrowingAdvice(any(JoinPoint.class), any(Throwable.class));
    verify(this.aroundAspectLogger, never()).pointCut();
  }

  @Test
  void returnWithOutArgument() {
    String result = this.mockService.getWithOutArgument();
    assertThat(result).isEqualTo(SystemConstants.PROPERTIES_NOT_SET_VALUE);

    verify(this.aroundAspectLogger, times(1)).doBefore(any(JoinPoint.class));
    verify(this.aroundAspectLogger, times(1))
        .doAfterReturningAdvice(any(JoinPoint.class), eq(SystemConstants.PROPERTIES_NOT_SET_VALUE));
    verify(this.aroundAspectLogger, never())
        .doAfterThrowingAdvice(any(JoinPoint.class), any(Throwable.class));
    verify(this.aroundAspectLogger, never()).pointCut();
  }

  @Test
  void throwable() {
    InvalidParamException exception =
        assertThrows(InvalidParamException.class, () -> this.mockService.getWithThrowable());
    assertThat(exception)
        .isNotNull()
        .matches(e -> DefaultResultCodeEnums.PARAM_INVALID == e.getCode(), "wrong code")
        .matches(
            e -> DefaultResultCodeEnums.PARAM_INVALID.getCode() == e.getCode().getCode(),
            "wrong code")
        .matches(
            e -> DefaultResultCodeEnums.PARAM_INVALID.getMessage().equals(e.getMessage()),
            "wrong message");

    verify(this.aroundAspectLogger, times(1)).doBefore(any(JoinPoint.class));
    verify(this.aroundAspectLogger, times(1))
        .doAfterThrowingAdvice(any(JoinPoint.class), any(Throwable.class));
    verify(this.aroundAspectLogger, never()).doAfterReturningAdvice(any(), any());
    verify(this.aroundAspectLogger, never()).pointCut();
  }

  @Test
  void noAspect() {
    String result = this.mockService.getWithoutAspect();
    assertThat(result).isEqualTo(SystemConstants.PROPERTIES_NOT_SET_VALUE);

    verify(this.aroundAspectLogger, never()).doBefore(any());
    verify(this.aroundAspectLogger, never()).doAfterReturningAdvice(any(), anyString());
    verify(this.aroundAspectLogger, never()).doAfterThrowingAdvice(any(), any());
    verify(this.aroundAspectLogger, never()).pointCut();
  }

  @Service
  static class MockService {

    @LoggerTrace("getWithArgument")
    public String getWithArgument(String str) {
      return str;
    }

    @LoggerTrace()
    public String getWithOutArgument() {
      return SystemConstants.PROPERTIES_NOT_SET_VALUE;
    }

    @LoggerTrace("getWithThrowable")
    public String getWithThrowable() {
      throw new InvalidParamException();
    }

    public String getWithoutAspect() {
      return SystemConstants.PROPERTIES_NOT_SET_VALUE;
    }
  }
}
