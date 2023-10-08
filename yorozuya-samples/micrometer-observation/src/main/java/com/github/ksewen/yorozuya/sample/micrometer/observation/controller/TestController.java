package com.github.ksewen.yorozuya.sample.micrometer.observation.controller;

import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.micrometer.observation.remote.ServerFacade;
import com.github.ksewen.yorozuya.starter.annotation.logger.LoggerTrace;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author ksewen
 * @date 31.08.2023 11:21
 */
@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
@Slf4j
public class TestController {

  private final RestTemplate restTemplate;

  private final ServerFacade serverFacade;

  @Value("${server.url:http://127.0.0.1:8080/rest/server}")
  private String url;

  @GetMapping("/client")
  @Observed(
      name = "client",
      contextualName = "record-client-request",
      lowCardinalityKeyValues = {"second", "1"})
  @LoggerTrace("client")
  public Result<Boolean> client(@RequestParam(name = "second", required = false) Integer second) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.url);
    if (second != null) {
      builder.queryParam("second", second);
    }
    Result<Boolean> result = this.restTemplate.getForObject(builder.toUriString(), Result.class);
    return Result.success(result.getData());
  }

  @GetMapping("/feign-client")
  public Result<Boolean> feignClient(
      @RequestParam(name = "second", required = false) Integer second) {
    Result<Boolean> result = this.serverFacade.server(second);
    return Result.success(result.getData());
  }

  @GetMapping("/server")
  public Result<Boolean> server(@RequestParam(name = "second", required = false) Integer second)
      throws InterruptedException {
    if (second != null) {
      Thread.sleep(second * 1000);
    }
    return Result.success(Boolean.TRUE);
  }
}
