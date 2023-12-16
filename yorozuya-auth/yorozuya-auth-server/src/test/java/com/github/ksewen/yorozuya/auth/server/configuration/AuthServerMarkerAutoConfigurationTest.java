package com.github.ksewen.yorozuya.auth.server.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * @author ksewen
 * @date 26.11.2023 21:11
 */
class AuthServerMarkerAutoConfigurationTest {

  @Test
  void authServerMarker() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(AuthServerMarkerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(AuthServerMarkerAutoConfiguration.Marker.class);
              assertThat(context)
                  .getBean("authServerMarker")
                  .isSameAs(context.getBean(AuthServerMarkerAutoConfiguration.Marker.class));
            });
  }
}
