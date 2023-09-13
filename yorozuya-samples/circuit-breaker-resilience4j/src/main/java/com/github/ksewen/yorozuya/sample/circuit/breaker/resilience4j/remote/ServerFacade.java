package com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.remote;

import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.dto.InstanceResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ksewen
 * @date 04.09.2023 12:55
 */
@FeignClient(name = "restClient")
public interface ServerFacade {

  @GetMapping(value = "/rest/server")
  @CircuitBreaker(name = "restClientServer", fallbackMethod = "serverFallback")
  Result<InstanceResponse> server();

  @SuppressWarnings("unused")
  default Result<InstanceResponse> serverFallback(CallNotPermittedException e) {
    return Result.success(InstanceResponse.builder().instance("from cache").build());
  }
}
