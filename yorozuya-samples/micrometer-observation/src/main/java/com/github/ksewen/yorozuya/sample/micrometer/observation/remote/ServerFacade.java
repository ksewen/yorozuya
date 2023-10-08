package com.github.ksewen.yorozuya.sample.micrometer.observation.remote;

import com.github.ksewen.yorozuya.common.facade.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ksewen
 * @date 04.09.2023 12:55
 */
@FeignClient(name = "feignClient")
public interface ServerFacade {

  @GetMapping("/server")
  Result<Boolean> server(@RequestParam Integer second);
}
