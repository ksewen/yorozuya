package com.github.ksewen.yorozuya.sample.rest.client.okhttp.remote;

import com.github.ksewen.yorozuya.common.facade.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ksewen
 * @date 04.09.2023 12:55
 */
@FeignClient(name = "restClient")
public interface ServerFacade {

  @GetMapping("/server")
  Result<Boolean> server();
}
