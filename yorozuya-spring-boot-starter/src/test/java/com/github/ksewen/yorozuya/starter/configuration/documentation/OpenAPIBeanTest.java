package com.github.ksewen.yorozuya.starter.configuration.documentation;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.common.constant.SystemConstants;
import com.github.ksewen.yorozuya.starter.configuration.environment.EnvironmentAutoConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ksewen
 * @date 09.10.2023 00:38
 */
@SpringBootTest(
    classes = {SpringDocAutoConfiguration.class, EnvironmentAutoConfiguration.class},
    properties = {"springdoc.version=3.0.0", "springdoc.license.url=https://www.test.com"})
public class OpenAPIBeanTest {

  @Autowired private OpenAPI openAPI;

  @Test
  void test() {
    assertThat(openAPI)
        .matches(o -> o.getInfo() != null, "info object is null")
        .matches(
            o -> SystemConstants.PROPERTIES_NOT_SET_VALUE.equals(o.getInfo().getTitle()),
            "title is not expected")
        .matches(o -> "3.0.0".equals(o.getInfo().getVersion()), "version is not expected")
        .matches(
            o ->
                ("Documentation for " + SystemConstants.PROPERTIES_NOT_SET_VALUE)
                    .equals(o.getInfo().getDescription()),
            "description is not expected")
        .matches(o -> o.getInfo().getLicense() != null, "license is null")
        .matches(
            o -> "MIT".equals(o.getInfo().getLicense().getName()), "license name is not expected")
        .matches(
            o -> "https://www.test.com".equals(o.getInfo().getLicense().getUrl()),
            "license url is not expected");
  }
}
