package com.github.ksewen.yorozuya.dashboard.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * @author ksewen
 * @date 18.11.2023 12:32
 */
class DashboardMarkerAutoConfigurationTest {

  @Test
  void test() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(DashboardMarkerAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(DashboardMarkerAutoConfiguration.Marker.class);
              assertThat(context)
                  .getBean("adminServerMarker")
                  .isSameAs(context.getBean(DashboardMarkerAutoConfiguration.Marker.class));
            });
  }
}
