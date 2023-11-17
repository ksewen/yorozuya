package com.github.ksewen.yorozuya.dashboard.configuration;

import com.github.ksewen.yorozuya.dashboard.service.ApplicationRegistry;
import com.github.ksewen.yorozuya.dashboard.service.EurekaServerManager;
import com.github.ksewen.yorozuya.dashboard.service.EurekaService;
import com.github.ksewen.yorozuya.dashboard.service.impl.EurekaServiceImpl;
import com.github.ksewen.yorozuya.dashboard.service.impl.PropertiesEurekaServerManager;
import com.github.ksewen.yorozuya.dashboard.service.impl.RestEurekaApplicationRegistry;
import com.github.ksewen.yorozuya.starter.helper.http.client.RestClientHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author ksewen
 * @date 16.11.2023 11:29
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(DashboardMarkerAutoConfiguration.Marker.class)
public class InstanceRegistryAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(EurekaService.class)
  public EurekaService eurekaService(
      @Autowired RestTemplate restTemplate, @Autowired RestClientHelpers restClientHelpers) {
    return new EurekaServiceImpl(restTemplate, restClientHelpers);
  }

  @Bean
  @ConditionalOnMissingBean(EurekaServerManager.class)
  public EurekaServerManager eurekaServerManager(@Autowired EurekaClientConfigBean properties) {
    return new PropertiesEurekaServerManager(properties);
  }

  @Bean
  @ConditionalOnMissingBean(ApplicationRegistry.class)
  public ApplicationRegistry applicationRegistry(
      @Autowired EurekaServerManager eurekaServerManager, @Autowired EurekaService eurekaService) {
    return new RestEurekaApplicationRegistry(eurekaServerManager, eurekaService);
  }
}
