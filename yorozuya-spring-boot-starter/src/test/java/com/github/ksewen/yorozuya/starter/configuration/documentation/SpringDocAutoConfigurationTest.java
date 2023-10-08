package com.github.ksewen.yorozuya.starter.configuration.documentation;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.starter.configuration.environment.EnvironmentAutoConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * @author ksewen
 * @date 31.08.2023 22:45
 */
class SpringDocAutoConfigurationTest {

  @Test
  void context() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                SpringDocAutoConfiguration.class, EnvironmentAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(OpenAPI.class);
              assertThat(context).getBean("openAPI").isSameAs(context.getBean(OpenAPI.class));
            });
  }
}
