package com.github.ksewen.yorozuya.starter.aspect.logger.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.ksewen.yorozuya.common.environment.Environment;
import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.starter.annotation.logger.LoggerTrace;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author ksewen
 * @date 06.10.2023 18:04
 */
@SpringBootTest(classes = BasicAroundLogger.class)
class BasicAroundLoggerTest {

  @Autowired private BasicAroundLogger basicAroundLogger;

  @MockBean private Environment environment;

  @MockBean private JsonHelpers jsonHelpers;

  @MockBean private JoinPoint joinPoint;

  private final String MOCK_DECLARING_TYPE_NAME = "com.github.ksewen.MockSignature";

  private final String MOCK_NAME = "mock()";

  private final String MOCK_OPERATION_NAME = this.MOCK_DECLARING_TYPE_NAME + "." + this.MOCK_NAME;

  @Test
  void getApplicationName() {
    final String applicationName = "test-aop";
    when(this.environment.getApplicationName()).thenReturn(applicationName);
    String result = this.basicAroundLogger.getApplicationName();
    assertThat(result).isEqualTo(applicationName);
  }

  @Test
  void getOperationDescriptionWithAnnotationAndValue() throws NoSuchMethodException {
    MethodSignature signature = mock(MethodSignature.class);
    when(this.joinPoint.getSignature()).thenReturn(signature);
    when(signature.getMethod()).thenReturn(withAnnotationAndValue());
    String result = this.basicAroundLogger.getOperationDescription(this.joinPoint);
    assertThat(result).isEqualTo("value");
  }

  @Test
  void getOperationDescriptionWithAnnotationAndWithoutValue() throws NoSuchMethodException {
    MethodSignature signature = mock(MethodSignature.class);
    when(this.joinPoint.getSignature()).thenReturn(signature);
    when(signature.getMethod()).thenReturn(withAnnotationAndWithoutValue());
    when(signature.getDeclaringTypeName()).thenReturn(this.MOCK_DECLARING_TYPE_NAME);
    when(signature.getName()).thenReturn(this.MOCK_NAME);
    String result = this.basicAroundLogger.getOperationDescription(this.joinPoint);
    assertThat(result).isEqualTo(this.MOCK_OPERATION_NAME);
  }

  @Test
  void getOperationDescriptionWithoutAnnotation() throws NoSuchMethodException {
    MethodSignature signature = mock(MethodSignature.class);
    when(this.joinPoint.getSignature()).thenReturn(signature);
    when(signature.getMethod()).thenReturn(withoutAnnotation());
    when(signature.getDeclaringTypeName()).thenReturn(this.MOCK_DECLARING_TYPE_NAME);
    when(signature.getName()).thenReturn(this.MOCK_NAME);
    String result = this.basicAroundLogger.getOperationDescription(this.joinPoint);
    assertThat(result).isEqualTo(this.MOCK_OPERATION_NAME);
  }

  @Test
  void parseArgumentsOrResult() {
    final String json = "jsonStr";
    when(this.jsonHelpers.toJsonString(any())).thenReturn(json);
    String result = this.basicAroundLogger.parseArgumentsOrResult(Result.success());
    assertThat(result).isEqualTo(json);
  }

  @LoggerTrace("value")
  Method withAnnotationAndValue() throws NoSuchMethodException {
    return this.getClass().getDeclaredMethod("withAnnotationAndValue");
  }

  @LoggerTrace
  Method withAnnotationAndWithoutValue() throws NoSuchMethodException {
    return this.getClass().getDeclaredMethod("withAnnotationAndWithoutValue");
  }

  Method withoutAnnotation() throws NoSuchMethodException {
    return this.getClass().getDeclaredMethod("withoutAnnotation");
  }
}
