package com.github.ksewen.yorozuya.auth.server.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * @author ksewen
 * @date 26.11.2023 21:11
 */
class ResourceServerMarkerAutoConfigurationTest {

  @Test
  void authServerMarker() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(ResourceServerMarkerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(ResourceServerMarkerAutoConfiguration.Marker.class);
              assertThat(context)
                  .getBean("resourceServerMarker")
                  .isSameAs(context.getBean(ResourceServerMarkerAutoConfiguration.Marker.class));
            });
  }
}
