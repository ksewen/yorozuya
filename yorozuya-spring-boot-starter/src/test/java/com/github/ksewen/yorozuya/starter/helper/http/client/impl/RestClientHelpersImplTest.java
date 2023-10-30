package com.github.ksewen.yorozuya.starter.helper.http.client.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.starter.helper.http.client.RestClientHelpers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @author ksewen
 * @date 30.10.2023 22:21
 */
@SpringBootTest(classes = RestClientHelpersImpl.class)
class RestClientHelpersImplTest {

  @Autowired private RestClientHelpers restClientHelpers;

  @Test
  void buildDefaultHeaders() {
    HttpHeaders httpHeaders = this.restClientHelpers.buildDefaultHeaders();
    assertThat(httpHeaders.isEmpty()).isFalse();
    assertThat(httpHeaders.getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    assertThat(httpHeaders.getAccept()).matches(a -> MediaType.APPLICATION_JSON.equals(a.get(0)));
    assertThat(httpHeaders.get("Accept-Charset")).matches(ac -> "UTF-8".equals(ac.get(0)));
  }
}
