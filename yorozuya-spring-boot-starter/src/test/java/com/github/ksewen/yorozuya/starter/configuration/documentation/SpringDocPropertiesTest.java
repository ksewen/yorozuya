package com.github.ksewen.yorozuya.starter.configuration.documentation;

import static org.assertj.core.api.Assertions.*;

import com.github.ksewen.yorozuya.common.environment.Environment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.StringUtils;

/**
 * @author ksewen
 * @date 31.08.2023 22:32
 */
@SpringBootTest(
    classes = SpringDocAutoConfiguration.class,
    properties = {
      "springdoc.version=2.0.0",
      "springdoc.description=api description",
      "springdoc.license.name=Apache 2.0"
    })
class SpringDocPropertiesTest {

  @Autowired private SpringDocProperties springDocProperties;

  @SuppressWarnings("unused")
  @MockBean
  private Environment environment;

  @Test
  void getVersion() {
    assertThat(this.springDocProperties.getVersion()).isEqualTo("2.0.0");
  }

  @Test
  void getTitle() {
    assertThat(this.springDocProperties.getTitle())
        .matches(t -> !StringUtils.hasLength(t), "title has text");
  }

  @Test
  void getDescription() {
    assertThat(this.springDocProperties.getDescription()).isEqualTo("api description");
  }

  @Test
  void getLicenseProperties() {
    assertThat(this.springDocProperties.getLicense()).isNotNull();
    assertThat(this.springDocProperties.getLicense().getName()).isEqualTo("Apache 2.0");
    assertThat(this.springDocProperties.getLicense().getUrl())
        .isEqualTo("https://opensource.org/license/mit/");
  }
}
