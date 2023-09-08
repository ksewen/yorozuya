package com.github.ksewen.yorozuya.sample.eureka.client.controller;

import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.eureka.client.dto.InstanceResponse;
import com.github.ksewen.yorozuya.sample.eureka.client.remote.ServerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author ksewen
 * @date 31.08.2023 11:21
 */
@RestController
@RequestMapping("/eureka")
public class TestController {

  @Autowired private ServerFacade serverFacade;

  @Value("${instance.id:1}")
  private String instance;

  @GetMapping("/client")
  public Result<InstanceResponse> feignClient() {
    Result<InstanceResponse> result = this.serverFacade.server();
    return Result.success(result.getData());
  }

  @GetMapping("/server")
  public Result<InstanceResponse> server() {
    return Result.success(InstanceResponse.builder().instance(instance).build());
  }
}
