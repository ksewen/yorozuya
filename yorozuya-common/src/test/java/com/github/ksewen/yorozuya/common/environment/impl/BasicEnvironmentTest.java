package com.github.ksewen.yorozuya.common.environment.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.common.constant.SystemConstants;
import org.junit.jupiter.api.Test;

/**
 * @author ksewen
 * @date 25.08.2023 16:53
 */
class BasicEnvironmentTest {

  private BasicEnvironment environment = BasicEnvironment.getInstance();

  @Test
  void getHostName() {
    assertThat(this.environment.getHostName()).isNotEmpty();
  }

  @Test
  void getHostIp() {
    assertThat(this.environment.getHostIp()).isNotEmpty();
  }

  @Test
  void getEnvironment() {
    String env = "test";
    assertThat(this.environment.getEnvironment())
        .isEqualTo(SystemConstants.PROPERTIES_NOT_SET_VALUE);
    this.environment.initEnvironment(env);
    assertThat(this.environment.getEnvironment()).isEqualTo(env);
    this.environment.initEnvironment(env + "1");
    assertThat(this.environment.getEnvironment()).isEqualTo(env);
  }

  @Test
  void getApplicationName() {
    String app = "yorozuya";
    assertThat(this.environment.getApplicationName())
        .isEqualTo(SystemConstants.PROPERTIES_NOT_SET_VALUE);
    this.environment.initApplicationName(app);
    assertThat(this.environment.getApplicationName()).isEqualTo(app);
    this.environment.initApplicationName(app + "1");
    assertThat(this.environment.getApplicationName()).isEqualTo(app);
  }

  @Test
  void getMetadata() {
    String key = "test-key";
    String value = "test-value";
    assertThat(this.environment.getMetadata()).isNotNull();
  }
}
