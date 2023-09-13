package com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.controller;

import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.dto.InstanceResponse;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.remote.ServerFacade;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author ksewen
 * @date 31.08.2023 11:21
 */
@RestController
@RequestMapping("/rest")
public class TestController {

  @Autowired private ServerFacade serverFacade;

  @Autowired private RestService restService;

  @Value("${instance.id:1}")
  private String instance;

  @GetMapping("/client")
  public Result<InstanceResponse> client() {
    InstanceResponse result = this.restService.server();
    return Result.success(result);
  }

  @GetMapping("/feign-client")
  public Result<InstanceResponse> feignClient() {
    Result<InstanceResponse> result = this.serverFacade.server();
    return result.isSuccess()
        ? Result.success(result.getData())
        : Result.<InstanceResponse>builder()
            .success(result.isSuccess())
            .code(result.getCode())
            .message(result.getMessage())
            .data(result.getData())
            .build();
  }

  @GetMapping("/server")
  public Result<InstanceResponse> server() {
    return Result.success(InstanceResponse.builder().instance(instance).build());
  }
}
