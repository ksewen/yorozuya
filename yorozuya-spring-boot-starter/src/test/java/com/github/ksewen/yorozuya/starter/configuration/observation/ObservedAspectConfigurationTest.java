package com.github.ksewen.yorozuya.starter.configuration.observation;

import static org.assertj.core.api.Assertions.assertThat;

import io.micrometer.observation.aop.ObservedAspect;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * @author ksewen
 * @date 08.10.2023 23:08
 */
class ObservedAspectConfigurationTest {

  @Test
  void context() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ObservationAutoConfiguration.class, ObservedAspectConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(ObservedAspect.class);
              assertThat(context)
                  .getBean("observedAspect")
                  .isSameAs(context.getBean(ObservedAspect.class));
            });
  }
}
