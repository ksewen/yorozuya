package com.github.ksewen.yorozuya.starter.configuration.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import com.github.ksewen.yorozuya.common.exception.CommonException;
import com.github.ksewen.yorozuya.common.exception.InvalidParamException;
import com.github.ksewen.yorozuya.common.facade.response.Result;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author ksewen
 * @date 04.09.2023 10:57
 */
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BasicExceptionHandlerTest {

  private final String MESSAGE = "test message";

  private MockMvc mockMvc;

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
            get(UriComponentsBuilder.fromUriString("/mock/bind-exception")
                    .queryParam("fieldError", false)
                    .toUriString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.PARAM_INVALID.getCode()))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.message").value(DefaultResultCodeEnums.PARAM_INVALID.getMessage()))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void handleBindExceptionWithFieldError() throws Exception {
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
  void handleConnectException() throws Exception {
    this.mockMvc
        .perform(
            get(UriComponentsBuilder.fromUriString("/mock/connect-exception")
                    .queryParam("message", false)
                    .toUriString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getCode()))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(
            jsonPath("$.message").value(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getMessage()))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void handleConnectExceptionWithCustomMessage() throws Exception {
    this.mockMvc
        .perform(
            get("/mock/connect-exception")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getCode()))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.message").value(this.MESSAGE))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void handleSocketTimeoutException() throws Exception {
    this.mockMvc
        .perform(
            get(UriComponentsBuilder.fromUriString("/mock/socket-timeout-exception")
                    .queryParam("message", false)
                    .toUriString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getCode()))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(
            jsonPath("$.message").value(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getMessage()))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void handleSocketTimeoutExceptionWithCustomMessage() throws Exception {
    this.mockMvc
        .perform(
            get("/mock/socket-timeout-exception")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getCode()))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.message").value(this.MESSAGE))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void handleResourceAccessException() throws Exception {
    this.mockMvc
        .perform(
            get(UriComponentsBuilder.fromUriString("/mock/resource-access-exception")
                    .queryParam("message", false)
                    .toUriString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getCode()))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(
            jsonPath("$.message").value(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getMessage()))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void handleResourceAccessExceptionWithCustomMessage() throws Exception {
    this.mockMvc
        .perform(
            get("/mock/resource-access-exception")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getCode()))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.message").value(this.MESSAGE))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void handleFeignException() throws Exception {
    this.mockMvc
        .perform(
            get(UriComponentsBuilder.fromUriString("/mock/feign-exception")
                    .queryParam("message", false)
                    .toUriString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getCode()))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(
            jsonPath("$.message").value(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getMessage()))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void handleFeignExceptionWithCustomMessage() throws Exception {
    this.mockMvc
        .perform(
            get("/mock/feign-exception")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getCode()))
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
    public Result<Boolean> bindException(
        @RequestParam(name = "fieldError", defaultValue = "true") boolean fieldError)
        throws BindException {
      BindingResult bindingResult = mock(BindingResult.class);
      if (fieldError) {
        when(bindingResult.getFieldError())
            .thenReturn(new FieldError("test", "test field", MESSAGE));
      } else {
        when(bindingResult.getFieldError()).thenReturn(null);
      }
      throw new BindException(bindingResult);
    }

    @GetMapping("/constraint-violation-exception")
    public Result<Boolean> constraintViolationException() {
      throw new ConstraintViolationException(MESSAGE, null);
    }

    @GetMapping("/connect-exception")
    public Result<Boolean> connectException(
        @RequestParam(name = "message", defaultValue = "true") boolean message) throws Exception {
      if (message) {
        throw new ConnectException(MESSAGE);
      }
      throw new ConnectException();
    }

    @GetMapping("/socket-timeout-exception")
    public Result<Boolean> socketTimeoutException(
        @RequestParam(name = "message", defaultValue = "true") boolean message) throws Exception {
      if (message) {
        throw new SocketTimeoutException(MESSAGE);
      }
      throw new SocketTimeoutException();
    }

    @GetMapping("/resource-access-exception")
    public Result<Boolean> resourceAccessException(
        @RequestParam(name = "message", defaultValue = "true") boolean message) {
      if (message) {
        throw new ResourceAccessException(MESSAGE);
      }
      throw new ResourceAccessException(null);
    }

    @GetMapping("/feign-exception")
    public Result<Boolean> feignException(
        @RequestParam(name = "message", defaultValue = "true") boolean message) {
      FeignException feignException = mock(FeignException.class);
      if (message) {
        when(feignException.getMessage()).thenReturn(MESSAGE);
      }
      throw feignException;
    }

    @GetMapping("/call-not-permitted-exception")
    public Result<Boolean> callNotPermittedException() {
      CircuitBreaker circuitBreaker = mock(CircuitBreaker.class);
      when(circuitBreaker.getName()).thenReturn("mock circuit breaker");
      when(circuitBreaker.getState()).thenReturn(CircuitBreaker.State.OPEN);
      when(circuitBreaker.getCircuitBreakerConfig())
          .thenReturn(new CircuitBreakerConfig.Builder().writableStackTraceEnabled(false).build());
      throw CallNotPermittedException.createCallNotPermittedException(circuitBreaker);
    }

    @GetMapping("/request-not-permitted")
    public Result<Boolean> requestNotPermitted() {
      RateLimiter rateLimiter = mock(RateLimiter.class);
      when(rateLimiter.getName()).thenReturn("mock rate limiter");
      when(rateLimiter.getRateLimiterConfig())
          .thenReturn(new RateLimiterConfig.Builder().writableStackTraceEnabled(false).build());
      throw RequestNotPermitted.createRequestNotPermitted(rateLimiter);
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
}
