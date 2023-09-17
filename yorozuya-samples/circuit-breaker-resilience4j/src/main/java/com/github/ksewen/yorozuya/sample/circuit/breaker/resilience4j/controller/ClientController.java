package com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.controller;

import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.dto.InstanceResponse;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ksewen
 * @date 14.09.2023 10:40
 */
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

  @Qualifier("clientService")
  private final Service service;

  @GetMapping("/success")
  public Result<InstanceResponse> success() {
    String success = this.service.success();
    return Result.success(InstanceResponse.builder().instance(success).build());
  }

  @GetMapping("/failure")
  public Result<InstanceResponse> failure() {
    String success = this.service.failure();
    return Result.success(InstanceResponse.builder().instance(success).build());
  }

  @GetMapping("/timeout")
  public Result<InstanceResponse> timeout() {
    String success = this.service.timeout();
    return Result.success(InstanceResponse.builder().instance(success).build());
  }

  @GetMapping("/ignore-exception")
  public Result<InstanceResponse> ignoreException() {
    String success = this.service.ignoreException();
    return Result.success(InstanceResponse.builder().instance(success).build());
  }
}
