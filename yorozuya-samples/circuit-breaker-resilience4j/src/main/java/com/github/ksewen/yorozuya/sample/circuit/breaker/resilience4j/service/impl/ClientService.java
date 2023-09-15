package com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.service.impl;

import com.github.ksewen.yorozuya.common.exception.InvalidParamException;
import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.dto.InstanceResponse;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.service.Service;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author ksewen
 * @date 14.09.2023 10:52
 */
@Component(value = "clientService")
@RequiredArgsConstructor
public class ClientService implements Service {

  @Qualifier("loadBalancerRestTemplate")
  private final RestTemplate loadBalancerRestTemplate;

  private final RestTemplate restTemplate;

  @Value("${server.url:http://127.0.0.1:8080/server/delay-response}")
  private String url;

  private static final String CLIENT_SERVICE = "clientService";

  @Override
  @RateLimiter(name = CLIENT_SERVICE)
  @CircuitBreaker(name = CLIENT_SERVICE, fallbackMethod = "serverFallback")
  public String success() {
    Result<InstanceResponse> result =
        this.restTemplate
            .exchange(
                UriComponentsBuilder.fromHttpUrl(url).toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result<InstanceResponse>>() {})
            .getBody();
    return result.getData().getInstance();
  }

  @Override
  @RateLimiter(name = CLIENT_SERVICE)
  @CircuitBreaker(name = "clientServiceWithLoadBalancer", fallbackMethod = "serverFallback")
  public String failure() {
    String url = "http://eureka-client/rest/server";
    Result<InstanceResponse> result =
        this.loadBalancerRestTemplate
            .exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result<InstanceResponse>>() {})
            .getBody();
    return result.getData().getInstance();
  }

  @Override
  @CircuitBreaker(name = CLIENT_SERVICE, fallbackMethod = "serverFallback")
  @RateLimiter(name = CLIENT_SERVICE, fallbackMethod = "serverFallback")
  public String timeout() {
    Result<InstanceResponse> result =
        this.restTemplate
            .exchange(
                UriComponentsBuilder.fromHttpUrl(url).queryParam("second", 60).toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result<InstanceResponse>>() {})
            .getBody();
    return result.getData().getInstance();
  }

  @Override
  @RateLimiter(name = CLIENT_SERVICE)
  @CircuitBreaker(name = CLIENT_SERVICE, fallbackMethod = "serverFallback")
  public String ignoreException() {
    this.success();
    throw new InvalidParamException();
  }

  @SuppressWarnings("unused")
  String serverFallback(CallNotPermittedException e) {
    return "circuit breaker is open";
  }

  @SuppressWarnings("unused")
  String serverFallback(RequestNotPermitted e) {
    return "rate limiter does not permit further calls";
  }
}
