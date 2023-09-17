package com.github.ksewen.yorozuya.starter.configuration.documentation;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.common.constant.SystemConstants;
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
        .withConfiguration(AutoConfigurations.of(SpringDocAutoConfiguration.class))
        .withPropertyValues("springdoc.version=3.0.0", "springdoc.license.url=https://www.test.com")
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(OpenAPI.class);
              assertThat(context).getBean("openAPI").isSameAs(context.getBean(OpenAPI.class));

              OpenAPI openAPI = context.getBean(OpenAPI.class);
              assertThat(openAPI)
                  .matches(o -> o.getInfo() != null, "info object is null")
                  .matches(
                      o -> SystemConstants.PROPERTIES_NOT_SET_VALUE.equals(o.getInfo().getTitle()),
                      "title is not expected")
                  .matches(o -> "3.0.0".equals(o.getInfo().getVersion()), "version is not expected")
                  .matches(
                      o -> "Documentation for UNSET".equals(o.getInfo().getDescription()),
                      "description is not expected")
                  .matches(o -> o.getInfo().getLicense() != null, "license is null")
                  .matches(
                      o -> "MIT".equals(o.getInfo().getLicense().getName()),
                      "license name is not expected")
                  .matches(
                      o -> "https://www.test.com".equals(o.getInfo().getLicense().getUrl()),
                      "license url is not expected");
            });
  }
}
