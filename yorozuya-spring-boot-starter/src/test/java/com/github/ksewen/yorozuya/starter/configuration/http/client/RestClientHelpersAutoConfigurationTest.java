package com.github.ksewen.yorozuya.starter.configuration.http.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.starter.helper.http.client.RestClientHelpers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * @author ksewen
 * @date 30.10.2023 22:30
 */
class RestClientHelpersAutoConfigurationTest {

  @Test
  void restClientHelpers() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(RestClientHelpersAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(RestClientHelpers.class);
              assertThat(context)
                  .getBean("restClientHelpers")
                  .isSameAs(context.getBean(RestClientHelpers.class));
            });
  }

  @Test
  void noRestClientHelpers() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                MockRestClientHelpersAutoConfiguration.class,
                RestClientHelpersAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(RestClientHelpers.class);
              assertThat(context)
                  .getBean("mockRestClientHelpers")
                  .isSameAs(context.getBean(RestClientHelpers.class));

              assertThat(context).doesNotHaveBean("restClientHelpers");
            });
  }

  @Configuration
  @AutoConfigureBefore(RestClientHelpersAutoConfiguration.class)
  static class MockRestClientHelpersAutoConfiguration {

    @Bean
    public RestClientHelpers mockRestClientHelpers() {
      return new RestClientHelpers() {
        @Override
        public HttpHeaders buildDefaultHeaders() {
          return null;
        }

        @Override
        public void buildAuthHeaders(HttpHeaders headers) {}
      };
    }
  }
}
