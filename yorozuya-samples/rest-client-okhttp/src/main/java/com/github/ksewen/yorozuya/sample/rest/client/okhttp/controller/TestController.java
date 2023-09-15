package com.github.ksewen.yorozuya.sample.rest.client.okhttp.controller;

import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.rest.client.okhttp.remote.ServerFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author ksewen
 * @date 31.08.2023 11:21
 */
@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
public class TestController {

  private final RestTemplate restTemplate;

  private final ServerFacade serverFacade;

  @Value("${server.url:http://127.0.0.1:8080/rest/server}")
  private String url;

  @GetMapping("/client")
  public Result<Boolean> client() {
    Result<Boolean> result = this.restTemplate.getForObject(this.url, Result.class);
    return Result.success(result.getData());
  }

  @GetMapping("/feign-client")
  public Result<Boolean> feignClient() {
    Result<Boolean> result = this.serverFacade.server();
    return Result.success(result.getData());
  }

  @GetMapping("/server")
  public Result<Boolean> server() {
    return Result.success(Boolean.TRUE);
  }
}
