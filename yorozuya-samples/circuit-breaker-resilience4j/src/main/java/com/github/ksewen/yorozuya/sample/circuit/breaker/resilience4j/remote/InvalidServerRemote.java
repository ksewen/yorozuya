package com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.remote;

import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.dto.InstanceResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ksewen
 * @date 15.09.2023 09:07
 */
@FeignClient(name = "invalidRestClient")
public interface InvalidServerRemote {

  @GetMapping(value = "/server/delay-response")
  @CircuitBreaker(name = "feignClientServer", fallbackMethod = "serverFallback")
  Result<InstanceResponse> server(@RequestParam Integer second);

  @SuppressWarnings("unused")
  default Result<InstanceResponse> serverFallback(CallNotPermittedException e) {
    return Result.success(InstanceResponse.builder().instance("circuit breaker is open").build());
  }
}
