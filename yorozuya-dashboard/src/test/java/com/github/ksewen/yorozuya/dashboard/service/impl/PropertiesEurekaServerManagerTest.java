package com.github.ksewen.yorozuya.dashboard.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.github.ksewen.yorozuya.dashboard.service.EurekaServerManager;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;

/**
 * @author ksewen
 * @date 31.10.2023 15:55
 */
@SpringBootTest(classes = PropertiesEurekaServerManager.class)
class PropertiesEurekaServerManagerTest {

  @Autowired private EurekaServerManager registerServerService;

  @MockBean private EurekaClientConfigBean properties;

  @Test
  void serviceConfig() {
    final String testZone = "test";
    final String testZoneUrl = "http://127.0.0.2:8001";
    when(this.properties.getServiceUrl())
        .thenReturn(
            Map.ofEntries(
                Map.entry(
                    EurekaClientConfigBean.DEFAULT_ZONE,
                    "http://127.0.0.1:8001,http://127.0.0.1:8002,http://127.0.0.1:8003"),
                Map.entry(testZone, testZoneUrl)));
    when(this.properties.getEurekaServerServiceUrls(EurekaClientConfigBean.DEFAULT_ZONE))
        .thenReturn(
            Arrays.asList(
                "http://127.0.0.1:8001", "http://127.0.0.1:8002", "http://127.0.0.1:8003"));
    when(this.properties.getEurekaServerServiceUrls(testZone)).thenReturn(List.of(testZoneUrl));

    Map<String, String> result = this.registerServerService.serviceConfig();
    assertThat(result)
        .hasSize(2)
        .matches(c -> c.containsKey(EurekaClientConfigBean.DEFAULT_ZONE))
        .matches(c -> "http://127.0.0.1:8001".equals(c.get(EurekaClientConfigBean.DEFAULT_ZONE)))
        .matches(c -> c.containsKey(testZone))
        .matches(c -> testZoneUrl.equals(c.get(testZone)));
  }
}
