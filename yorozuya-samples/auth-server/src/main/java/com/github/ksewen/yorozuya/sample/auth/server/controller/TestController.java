package com.github.ksewen.yorozuya.sample.auth.server.controller;

import com.github.ksewen.yorozuya.common.facade.response.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ksewen
 * @date 01.12.2023 19:50
 */
@RestController
@RequestMapping("/test")
public class TestController {

  @GetMapping("/need-authentication")
  public Result<Boolean> needAuthentication() {
    return Result.success(Boolean.TRUE);
  }

  @GetMapping("/non-authentication")
  public Result<Boolean> nonAuthentication() {
    return Result.success(Boolean.TRUE);
  }
}
