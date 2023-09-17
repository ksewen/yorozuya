package com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.remote;

import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.dto.InstanceResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ksewen
 * @date 04.09.2023 12:55
 */
@FeignClient(name = "restClient")
public interface ServerRemote {

  String FEIGN_CLIENT_SERVICE = "feignClientServer";

  @GetMapping(value = "/server/delay-response")
  @RateLimiter(name = FEIGN_CLIENT_SERVICE, fallbackMethod = "serverFallback")
  @CircuitBreaker(name = FEIGN_CLIENT_SERVICE, fallbackMethod = "serverFallback")
  Result<InstanceResponse> server(@RequestParam Integer second);

  @SuppressWarnings("unused")
  default Result<InstanceResponse> serverFallback(CallNotPermittedException e) {
    return Result.success(InstanceResponse.builder().instance("circuit breaker is open").build());
  }
}
