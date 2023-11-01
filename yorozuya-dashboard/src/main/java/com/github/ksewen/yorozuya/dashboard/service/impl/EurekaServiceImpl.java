package com.github.ksewen.yorozuya.dashboard.service.impl;

import com.github.ksewen.yorozuya.dashboard.dto.EurekaQueryDTO;
import com.github.ksewen.yorozuya.dashboard.service.EurekaService;
import com.github.ksewen.yorozuya.starter.helper.http.client.RestClientHelpers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author ksewen
 * @date 31.10.2023 16:28
 */
@RequiredArgsConstructor
public class EurekaServiceImpl implements EurekaService {

  private final RestTemplate restTemplate;

  private final RestClientHelpers restClientHelpers;

  @Override
  public EurekaQueryDTO applications(String server) {
    final String eurekaQueryPath = "/eureka/apps";
    String uri =
        UriComponentsBuilder.fromUriString(server).path(eurekaQueryPath).build().toUriString();
    HttpHeaders httpHeaders = this.restClientHelpers.buildDefaultHeaders();
    return this.restTemplate
        .exchange(uri, HttpMethod.GET, new HttpEntity<>(httpHeaders), EurekaQueryDTO.class)
        .getBody();
  }
}
