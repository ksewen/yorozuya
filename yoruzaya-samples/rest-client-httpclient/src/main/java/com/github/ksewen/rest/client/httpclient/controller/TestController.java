package com.github.ksewen.rest.client.httpclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author ksewen
 * @date 31.08.2023 11:21
 */
@RestController
@RequestMapping("/rest")
public class TestController {

  @Autowired private RestTemplate restTemplate;

  @Value("${server.url:http://127.0.0.1:8080/rest/server}")
  private String url;

  @GetMapping("/client")
  public Boolean client() {
    Boolean result = this.restTemplate.getForObject(this.url, Boolean.class);
    return result;
  }

  @GetMapping("/server")
  public Boolean server() {
    return Boolean.TRUE;
  }
}
