package com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.service;

import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.dto.InstanceResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author ksewen
 * @date 11.09.2023 21:56
 */
@Service
public class RestService {

  @Autowired
  @Qualifier("loadBalancerRestTemplate")
  private RestTemplate restTemplate;

  @CircuitBreaker(name = "restTemplateClient", fallbackMethod = "serverFallback")
  public InstanceResponse server() {
    String url = "http://eureka-client/rest/server";
    Result<InstanceResponse> result =
        this.restTemplate
            .exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result<InstanceResponse>>() {})
            .getBody();
    return result.getData();
  }

  @SuppressWarnings("unused")
  InstanceResponse serverFallback(CallNotPermittedException e) {
    return InstanceResponse.builder().instance("from cache").build();
  }
}
