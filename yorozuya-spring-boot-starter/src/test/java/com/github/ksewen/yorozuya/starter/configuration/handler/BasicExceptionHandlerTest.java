package com.github.ksewen.yorozuya.starter.configuration.handler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import com.github.ksewen.yorozuya.common.exception.CommonException;
import com.github.ksewen.yorozuya.common.exception.InvalidParamException;
import com.github.ksewen.yorozuya.common.facade.response.Result;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolationException;
import java.beans.PropertyEditor;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ksewen
 * @date 04.09.2023 10:57
 */
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BasicExceptionHandlerTest {

  private String MESSAGE = "test message";

  @Autowired private MockMvc mockMvc;

  @BeforeAll
  void init() {
    this.mockMvc =
        MockMvcBuilders.standaloneSetup(new MockController())
            .setControllerAdvice(BasicExceptionHandler.class)
            .build();
  }

  @Test
  void handleBindException() throws Exception {
    this.mockMvc
        .perform(
            get("/mock/bind-exception")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.PARAM_INVALID.getCode()))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.message").value(this.MESSAGE))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void handleConstraintViolationException() throws Exception {
    this.mockMvc
        .perform(
            get("/mock/constraint-violation-exception")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.PARAM_INVALID.getCode()))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.message").value(this.MESSAGE))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void handleInvalidParamException() throws Exception {
    this.mockMvc
        .perform(
            get("/mock/invalid-param-exception")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.PARAM_INVALID.getCode()))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.message").value(this.MESSAGE))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void handleCallNotPermittedException() throws Exception {
    this.mockMvc
        .perform(
            get("/mock/call-not-permitted-exception")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(
            jsonPath("$.code")
                .value(DefaultResultCodeEnums.CIRCUIT_BREAKER_NO_FALLBACK_METHOD.getCode()))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(
            jsonPath("$.message")
                .value(
                    "CircuitBreaker 'mock circuit breaker' is OPEN and does not permit further calls"))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void handleRequestNotPermitted() throws Exception {
    this.mockMvc
        .perform(
            get("/mock/request-not-permitted")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(
            jsonPath("$.code")
                .value(DefaultResultCodeEnums.RATE_LIMITED_NO_FALLBACK_METHOD.getCode()))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(
            jsonPath("$.message")
                .value("RateLimiter 'mock rate limiter' does not permit further calls"))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void handleCommonException() throws Exception {
    this.mockMvc
        .perform(
            get("/mock/common-exception")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.OPERATION_FAILED.getCode()))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.message").value(this.MESSAGE))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void testException() throws Exception {
    this.mockMvc
        .perform(
            get("/mock/exception")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.SYSTEM_ERROR.getCode()))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.message").value(DefaultResultCodeEnums.SYSTEM_ERROR.getMessage()))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @RestController
  @RequestMapping("/mock")
  class MockController {

    @GetMapping("/bind-exception")
    public Result<Boolean> bindException() throws BindException {
      throw new BindException(new MockBindingResult());
    }

    @GetMapping("/constraint-violation-exception")
    public Result<Boolean> constraintViolationException() {
      throw new ConstraintViolationException(MESSAGE, null);
    }

    @GetMapping("/call-not-permitted-exception")
    public Result<Boolean> callNotPermittedException() {
      throw CallNotPermittedException.createCallNotPermittedException(new MockCircuitBreaker());
    }

    @GetMapping("/request-not-permitted")
    public Result<Boolean> requestNotPermitted() {
      throw RequestNotPermitted.createRequestNotPermitted(new MockRateLimiter());
    }

    @GetMapping("/invalid-param-exception")
    public Result<Boolean> invalidParamException() {
      throw new InvalidParamException(MESSAGE);
    }

    @GetMapping("/common-exception")
    public Result<Boolean> commonException() {
      throw new CommonException(DefaultResultCodeEnums.OPERATION_FAILED, MESSAGE);
    }

    @GetMapping("/exception")
    public Result<Boolean> exception() {
      throw new RuntimeException();
    }
  }

  class MockBindingResult implements BindingResult {

    @Override
    public String getObjectName() {
      return null;
    }

    @Override
    public void setNestedPath(String nestedPath) {}

    @Override
    public String getNestedPath() {
      return null;
    }

    @Override
    public void pushNestedPath(String subPath) {}

    @Override
    public void popNestedPath() throws IllegalStateException {}

    @Override
    public void reject(String errorCode) {}

    @Override
    public void reject(String errorCode, String defaultMessage) {}

    @Override
    public void reject(String errorCode, Object[] errorArgs, String defaultMessage) {}

    @Override
    public void rejectValue(String field, String errorCode) {}

    @Override
    public void rejectValue(String field, String errorCode, String defaultMessage) {}

    @Override
    public void rejectValue(
        String field, String errorCode, Object[] errorArgs, String defaultMessage) {}

    @Override
    public void addAllErrors(Errors errors) {}

    @Override
    public boolean hasErrors() {
      return false;
    }

    @Override
    public int getErrorCount() {
      return 0;
    }

    @Override
    public List<ObjectError> getAllErrors() {
      return null;
    }

    @Override
    public boolean hasGlobalErrors() {
      return false;
    }

    @Override
    public int getGlobalErrorCount() {
      return 0;
    }

    @Override
    public List<ObjectError> getGlobalErrors() {
      return null;
    }

    @Override
    public ObjectError getGlobalError() {
      return null;
    }

    @Override
    public boolean hasFieldErrors() {
      return false;
    }

    @Override
    public int getFieldErrorCount() {
      return 0;
    }

    @Override
    public List<FieldError> getFieldErrors() {
      return null;
    }

    @Override
    public FieldError getFieldError() {
      return new FieldError("test", "test field", MESSAGE);
    }

    @Override
    public boolean hasFieldErrors(String field) {
      return false;
    }

    @Override
    public int getFieldErrorCount(String field) {
      return 0;
    }

    @Override
    public List<FieldError> getFieldErrors(String field) {
      return null;
    }

    @Override
    public FieldError getFieldError(String field) {
      return null;
    }

    @Override
    public Object getFieldValue(String field) {
      return null;
    }

    @Override
    public Class<?> getFieldType(String field) {
      return null;
    }

    @Override
    public Object getTarget() {
      return null;
    }

    @Override
    public Map<String, Object> getModel() {
      return null;
    }

    @Override
    public Object getRawFieldValue(String field) {
      return null;
    }

    @Override
    public PropertyEditor findEditor(String field, Class<?> valueType) {
      return null;
    }

    @Override
    public PropertyEditorRegistry getPropertyEditorRegistry() {
      return null;
    }

    @Override
    public String[] resolveMessageCodes(String errorCode) {
      return new String[0];
    }

    @Override
    public String[] resolveMessageCodes(String errorCode, String field) {
      return new String[0];
    }

    @Override
    public void addError(ObjectError error) {}
  }

  class MockCircuitBreaker implements CircuitBreaker {

    @Override
    public boolean tryAcquirePermission() {
      return false;
    }

    @Override
    public void releasePermission() {}

    @Override
    public void acquirePermission() {}

    @Override
    public void onError(long duration, TimeUnit durationUnit, Throwable throwable) {}

    @Override
    public void onSuccess(long duration, TimeUnit durationUnit) {}

    @Override
    public void onResult(long duration, TimeUnit durationUnit, Object result) {}

    @Override
    public void reset() {}

    @Override
    public void transitionToClosedState() {}

    @Override
    public void transitionToOpenState() {}

    @Override
    public void transitionToOpenStateFor(Duration waitDuration) {}

    @Override
    public void transitionToOpenStateUntil(Instant waitUntil) {}

    @Override
    public void transitionToHalfOpenState() {}

    @Override
    public void transitionToDisabledState() {}

    @Override
    public void transitionToMetricsOnlyState() {}

    @Override
    public void transitionToForcedOpenState() {}

    @Override
    public String getName() {
      return "mock circuit breaker";
    }

    @Override
    public State getState() {
      return State.OPEN;
    }

    @Override
    public CircuitBreakerConfig getCircuitBreakerConfig() {
      return new CircuitBreakerConfig.Builder().writableStackTraceEnabled(false).build();
    }

    @Override
    public Metrics getMetrics() {
      return null;
    }

    @Override
    public Map<String, String> getTags() {
      return null;
    }

    @Override
    public EventPublisher getEventPublisher() {
      return null;
    }

    @Override
    public long getCurrentTimestamp() {
      return 0;
    }

    @Override
    public TimeUnit getTimestampUnit() {
      return null;
    }
  }

  class MockRateLimiter implements RateLimiter {
    @Override
    public void changeTimeoutDuration(Duration timeoutDuration) {}

    @Override
    public void changeLimitForPeriod(int limitForPeriod) {}

    @Override
    public boolean acquirePermission(int permits) {
      return false;
    }

    @Override
    public long reservePermission(int permits) {
      return 0;
    }

    @Override
    public void drainPermissions() {}

    @Override
    public String getName() {
      return "mock rate limiter";
    }

    @Override
    public RateLimiterConfig getRateLimiterConfig() {
      return new RateLimiterConfig.Builder().writableStackTraceEnabled(false).build();
    }

    @Override
    public Map<String, String> getTags() {
      return null;
    }

    @Override
    public Metrics getMetrics() {
      return null;
    }

    @Override
    public EventPublisher getEventPublisher() {
      return null;
    }
  }
}
