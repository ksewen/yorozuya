package com.github.ksewen.yorozuya.starter.configuration.http.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ksewen
 * @date 30.08.2023 16:57
 */
@SpringBootTest(classes = {OkHttp3ClientAutoConfiguration.class})
class OkHttp3ClientAutoConfigurationTest {

  @Autowired private OkHttpClient okHttpClient;

  @Test
  void httpClient() {
    assertThat(this.okHttpClient).isNotNull();
  }
}
