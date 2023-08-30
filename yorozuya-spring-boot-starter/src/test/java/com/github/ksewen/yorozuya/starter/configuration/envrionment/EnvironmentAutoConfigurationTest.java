package com.github.ksewen.yorozuya.starter.configuration.envrionment;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.common.environment.Environment;
import com.github.ksewen.yorozuya.starter.configuration.environment.EnvironmentAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ksewen
 * @date 25.08.2023 22:26
 */
@SpringBootTest(
    classes = {EnvironmentAutoConfiguration.class},
    properties = {"spring.profiles.active=test", "spring.application.name=yorozuya"})
class EnvironmentAutoConfigurationTest {

  @Autowired private Environment environment;

  @Test
  void getEnvironment() {
    assertThat(this.environment.getEnvironment()).isEqualTo("test");
  }

  @Test
  void getApplicationName() {
    assertThat(this.environment.getApplicationName()).isEqualTo("yorozuya");
  }
}
