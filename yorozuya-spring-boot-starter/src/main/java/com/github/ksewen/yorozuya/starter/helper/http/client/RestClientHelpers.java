package com.github.ksewen.yorozuya.starter.helper.http.client;

import org.springframework.http.HttpHeaders;

/**
 * @author ksewen
 * @date 27.10.2023 20:48
 */
public interface RestClientHelpers {

  /**
   * build the basic headers when request to another server
   *
   * @return
   */
  HttpHeaders buildDefaultHeaders();

  /**
   * fill the headers for authentication or authorization when request to another server
   *
   * @param headers
   */
  void buildAuthHeaders(HttpHeaders headers);
}
