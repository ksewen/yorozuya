package com.github.ksewen.yorozuya.dashboard.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * @author ksewen
 * @date 18.11.2023 12:34
 */
class DisableRepositoryMarkerAutoConfigurationTest {

  @Test
  void test() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(DisableRepositoryMarkerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context)
                  .hasSingleBean(DisableRepositoryMarkerAutoConfiguration.Marker.class);
              assertThat(context)
                  .getBean("disableRepositoryMarker")
                  .isSameAs(context.getBean(DisableRepositoryMarkerAutoConfiguration.Marker.class));
            });
  }
}
