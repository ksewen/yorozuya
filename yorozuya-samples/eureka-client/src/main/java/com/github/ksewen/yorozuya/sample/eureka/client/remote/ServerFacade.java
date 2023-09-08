package com.github.ksewen.yorozuya.sample.eureka.client.remote;

import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.eureka.client.dto.InstanceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ksewen
 * @date 04.09.2023 12:55
 */
@FeignClient(name = "eureka-client")
public interface ServerFacade {

  @GetMapping("/eureka/server")
  Result<InstanceResponse> server();
}
