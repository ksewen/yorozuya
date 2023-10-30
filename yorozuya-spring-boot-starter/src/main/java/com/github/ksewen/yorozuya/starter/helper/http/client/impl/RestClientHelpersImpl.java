package com.github.ksewen.yorozuya.starter.helper.http.client.impl;

import com.github.ksewen.yorozuya.starter.helper.http.client.RestClientHelpers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @author ksewen
 * @date 27.10.2023 20:49
 */
// TODO: Test
public class RestClientHelpersImpl implements RestClientHelpers {

  @Override
  public HttpHeaders buildDefaultHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
    httpHeaders.add("Accept-Charset", "UTF-8");
    return httpHeaders;
  }

  @Override
  public void buildAuthHeaders(HttpHeaders headers) {
    // TODO: Logic
  }
}
