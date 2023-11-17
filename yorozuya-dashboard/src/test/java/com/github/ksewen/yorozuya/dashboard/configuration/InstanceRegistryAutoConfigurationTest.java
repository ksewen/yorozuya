package com.github.ksewen.yorozuya.dashboard.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.github.ksewen.yorozuya.dashboard.model.Application;
import com.github.ksewen.yorozuya.dashboard.model.Instance;
import com.github.ksewen.yorozuya.dashboard.service.ApplicationRegistry;
import com.github.ksewen.yorozuya.dashboard.service.EurekaServerManager;
import com.github.ksewen.yorozuya.dashboard.service.EurekaService;
import com.github.ksewen.yorozuya.starter.helper.http.client.RestClientHelpers;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * @author ksewen
 * @date 17.11.2023 17:34
 */
class InstanceRegistryAutoConfigurationTest {

  @Test
  void eurekaService() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                DashboardMarkerAutoConfiguration.class,
                InstanceRegistryAutoConfiguration.class,
                MockAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(EurekaService.class);
              assertThat(context)
                  .getBean("eurekaService")
                  .isSameAs(context.getBean(EurekaService.class));
            });
  }

  @Test
  void nonEurekaService() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                DashboardMarkerAutoConfiguration.class,
                MockServiceAutoConfiguration.class,
                InstanceRegistryAutoConfiguration.class,
                MockAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(EurekaService.class);
              assertThat(context)
                  .getBean("mockEurekaService")
                  .isSameAs(context.getBean(EurekaService.class));
              assertThat(context).doesNotHaveBean("eurekaService");
            });
  }

  @Test
  void eurekaServerManager() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                DashboardMarkerAutoConfiguration.class,
                InstanceRegistryAutoConfiguration.class,
                MockAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(EurekaServerManager.class);
              assertThat(context)
                  .getBean("eurekaServerManager")
                  .isSameAs(context.getBean(EurekaServerManager.class));
            });
  }

  @Test
  void nonEurekaServerManager() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                DashboardMarkerAutoConfiguration.class,
                MockServiceAutoConfiguration.class,
                InstanceRegistryAutoConfiguration.class,
                MockAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(EurekaServerManager.class);
              assertThat(context)
                  .getBean("mockEurekaServerManager")
                  .isSameAs(context.getBean(EurekaServerManager.class));
              assertThat(context).doesNotHaveBean("eurekaServerManager");
            });
  }

  @Test
  void applicationRegistry() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                DashboardMarkerAutoConfiguration.class,
                InstanceRegistryAutoConfiguration.class,
                MockAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(ApplicationRegistry.class);
              assertThat(context)
                  .getBean("applicationRegistry")
                  .isSameAs(context.getBean(ApplicationRegistry.class));
            });
  }

  @Test
  void nonApplicationRegistry() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                DashboardMarkerAutoConfiguration.class,
                MockServiceAutoConfiguration.class,
                InstanceRegistryAutoConfiguration.class,
                MockAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(ApplicationRegistry.class);
              assertThat(context)
                  .getBean("mockApplicationRegistry")
                  .isSameAs(context.getBean(ApplicationRegistry.class));
              assertThat(context).doesNotHaveBean("applicationRegistry");
            });
  }

  @Test
  void nonAnyService() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                InstanceRegistryAutoConfiguration.class, MockAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(EurekaService.class);
              assertThat(context).doesNotHaveBean(EurekaServerManager.class);
              assertThat(context).doesNotHaveBean(ApplicationRegistry.class);
            });
  }

  @Configuration
  static class MockAutoConfiguration {

    @Bean
    public RestTemplate mockRestTemplate() {
      return new RestTemplate();
    }

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

    @Bean
    public EurekaClientConfigBean mockEurekaClientConfigBean() {
      return new EurekaClientConfigBean();
    }
  }

  @Configuration
  @AutoConfigureBefore(InstanceRegistryAutoConfiguration.class)
  static class MockServiceAutoConfiguration {
    @Bean
    public EurekaService mockEurekaService() {
      return server -> null;
    }

    @Bean
    public EurekaServerManager mockEurekaServerManager() {
      return () -> null;
    }

    @Bean
    public ApplicationRegistry mockApplicationRegistry() {
      return new ApplicationRegistry() {
        @Override
        public List<Application> applications(
            String zone, @Nullable Collection<String> applicationNames) {
          return null;
        }

        @Nullable
        @Override
        public Application getApplication(String zone, String applicationName) {
          return null;
        }

        @Override
        public List<Instance> instances(String zone, String applicationName) {
          return null;
        }

        @Nullable
        @Override
        public Instance getInstance(String zone, String applicationName, String instanceId) {
          return null;
        }

        @Override
        public void refresh() {}
      };
    }
  }
}
