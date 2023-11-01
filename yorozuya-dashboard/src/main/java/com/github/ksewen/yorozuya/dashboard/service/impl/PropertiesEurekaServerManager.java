package com.github.ksewen.yorozuya.dashboard.service.impl;

import com.github.ksewen.yorozuya.dashboard.service.EurekaServerManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.util.CollectionUtils;

/**
 * @author ksewen
 * @date 30.10.2023 16:01
 */
@RequiredArgsConstructor
public class PropertiesEurekaServerManager implements EurekaServerManager {

  private final EurekaClientConfigBean properties;

  @Override
  public Map<String, String> serviceConfig() {
    Map<String, String> result = new HashMap<>();
    this.properties
        .getServiceUrl()
        .keySet()
        .forEach(
            k -> {
              List<String> urls = this.properties.getEurekaServerServiceUrls(k);
              if (!CollectionUtils.isEmpty(urls)) {
                result.put(k, urls.get(0));
              }
            });
    return result;
  }
}
