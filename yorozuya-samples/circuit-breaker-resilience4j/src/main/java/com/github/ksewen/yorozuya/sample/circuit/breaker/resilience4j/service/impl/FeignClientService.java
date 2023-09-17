package com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.service.impl;

import com.github.ksewen.yorozuya.common.exception.InvalidParamException;
import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.dto.InstanceResponse;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.remote.InvalidServerRemote;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.remote.ServerRemote;
import com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author ksewen
 * @date 14.09.2023 10:53
 */
@Component(value = "feignClientService")
@RequiredArgsConstructor
public class FeignClientService implements Service {

  private final ServerRemote serverRemote;

  private final InvalidServerRemote invalidServerRemote;

  @Override
  public String success() {
    Result<InstanceResponse> server = this.serverRemote.server(null);
    return server.getData().getInstance();
  }

  @Override
  public String failure() {
    Result<InstanceResponse> server = this.invalidServerRemote.server(null);
    return server.getData().getInstance();
  }

  @Override
  public String timeout() {
    Result<InstanceResponse> server = this.serverRemote.server(60);
    return server.getData().getInstance();
  }

  @Override
  public String ignoreException() {
    this.success();
    throw new InvalidParamException();
  }
}
