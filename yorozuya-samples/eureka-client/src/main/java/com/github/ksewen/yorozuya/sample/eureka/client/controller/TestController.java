package com.github.ksewen.yorozuya.sample.eureka.client.controller;

import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.eureka.client.dto.InstanceResponse;
import com.github.ksewen.yorozuya.sample.eureka.client.remote.ServerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author ksewen
 * @date 31.08.2023 11:21
 */
@RestController
@RequestMapping("/rest")
public class TestController {

  @Autowired private ServerFacade serverFacade;

  @Autowired
  @Qualifier("loadBalancerRestTemplate")
  private RestTemplate restTemplate;

  @Value("${instance.id:1}")
  private String instance;

  @GetMapping("/client")
  public Result<InstanceResponse> client() {
    String url = "http://eureka-client/rest/server";
    Result<InstanceResponse> result =
        this.restTemplate
            .exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result<InstanceResponse>>() {})
            .getBody();
    return Result.success(result.getData());
  }

  @GetMapping("/feign-client")
  public Result<InstanceResponse> feignClient() {
    Result<InstanceResponse> result = this.serverFacade.server();
    return Result.success(result.getData());
  }

  @GetMapping("/server")
  public Result<InstanceResponse> server() {
    return Result.success(InstanceResponse.builder().instance(instance).build());
  }
}