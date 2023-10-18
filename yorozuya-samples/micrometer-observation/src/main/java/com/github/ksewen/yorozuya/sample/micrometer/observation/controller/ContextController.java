package com.github.ksewen.yorozuya.sample.micrometer.observation.controller;

import com.github.ksewen.yorozuya.common.context.Context;
import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.micrometer.observation.remote.ServerFacade;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author ksewen
 * @date 18.10.2023 18:06
 */
@RestController
@RequestMapping("/context")
@RequiredArgsConstructor
@Slf4j
public class ContextController {

  private final RestTemplate restTemplate;

  private final ServerFacade serverFacade;

  private final Context context;

  private final JsonHelpers jsonHelpers;

  @Value("${server.url:http://127.0.0.1:8080/rest/server}")
  private String url;

  @GetMapping("/client")
  public Result<Boolean> client() {
    log.info(
        "the current context has key-values: {}",
        this.jsonHelpers.toJsonString(this.context.getContext()));
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.url);
    Result<Boolean> result = this.restTemplate.getForObject(builder.toUriString(), Result.class);
    return Result.success(result.getData());
  }

  @GetMapping("/feign-client")
  public Result<Boolean> feignClient() {
    log.info(
        "the current context has key-values: {}",
        this.jsonHelpers.toJsonString(this.context.getContext()));
    Result<Boolean> result = this.serverFacade.server(null);
    return Result.success(result.getData());
  }
}
