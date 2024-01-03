package com.github.ksewen.yorozuya.auth.server.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.github.ksewen.yorozuya.auth.server.annotation.EnableAuthServer;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * @author ksewen
 * @date 10.12.2023 22:02
 */
@EnableAuthServer
@SpringBootTest(
    classes = {
      SecurityAutoConfiguration.class,
      AuthServerAutoConfigurationTest.MockJwtToUserConverterAutoConfiguration.class,
      SecurityAutoConfigurationTest.MockWebEndpointPropertiesAutoConfiguration.class,
      SecurityPropertiesTest.MockHandlerMappingIntrospectorAutoConfiguration.class
    },
    properties = {
      "security.permit-urls.get=/auth/**",
      "security.permit-urls.post=/test/*",
      "security.permit-urls.all=/swagger*/**",
      "security.protect-management-endpoints=false"
    })
class SecurityPropertiesTest {

  @Autowired private SecurityProperties securityProperties;

  @Test
  void getPermitUrls() {
    Map<String, String> result = this.securityProperties.getPermitUrls();
    assertThat(result)
        .isNotEmpty()
        .hasSize(3)
        .matches(s -> s.get("get").equals("/auth/**"))
        .matches(s -> s.get("post").equals("/test/*"))
        .matches(s -> s.get("all").equals("/swagger*/**"));
  }

  @Test
  void isProtectManagementEndpoints() {
    boolean result = this.securityProperties.isProtectManagementEndpoints();
    assertThat(result).isFalse();
  }

  @Configuration
  static class MockHandlerMappingIntrospectorAutoConfiguration {

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
      return mock(HandlerMappingIntrospector.class);
    }
  }
}
