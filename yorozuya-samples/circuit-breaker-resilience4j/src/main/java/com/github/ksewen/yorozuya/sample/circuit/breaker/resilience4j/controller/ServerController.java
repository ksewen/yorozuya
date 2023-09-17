package com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.controller;

import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.dto.InstanceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ksewen
 * @date 14.09.2023 10:41
 */
@RestController
@RequestMapping("/server")
public class ServerController {

  @Value("${instance.id:1}")
  private String instance;

  @GetMapping("/delay-response")
  public Result<InstanceResponse> delayResponse(
      @RequestParam(name = "second", required = false) Integer second) throws InterruptedException {
    if (second != null) {
      Thread.sleep(second * 1000);
    }
    return Result.success(InstanceResponse.builder().instance(instance).build());
  }
}
