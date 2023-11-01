package com.github.ksewen.yorozuya.dashboard.service.impl;

import com.github.ksewen.yorozuya.dashboard.dto.EurekaQueryDTO;
import com.github.ksewen.yorozuya.dashboard.model.Application;
import com.github.ksewen.yorozuya.dashboard.model.Instance;
import com.github.ksewen.yorozuya.dashboard.service.ApplicationRegistry;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.CollectionUtils;

/**
 * @author ksewen
 * @date 30.10.2023 11:53
 */
@Slf4j
public abstract class AbstractEurekaApplicationRegistry implements ApplicationRegistry {

  private final Map<String, Map<String, Application>> REGISTRY = new ConcurrentHashMap<>();

  @Override
  public List<Application> applications(
      String zone, @Nullable Collection<String> applicationNames) {
    Map<String, Application> applicationsByZone = this.REGISTRY.get(zone);
    if (CollectionUtils.isEmpty(applicationsByZone)) {
      return Collections.emptyList();
    }
    if (CollectionUtils.isEmpty(applicationNames)) {
      return applicationsByZone.values().stream().toList();
    }
    return applicationNames.stream()
        .map(
            a ->
                Optional.ofNullable(applicationsByZone.get(a))
                    .orElse(
                        Application.builder().name(a).instances(Collections.emptyList()).build()))
        .toList();
  }

  @Nullable
  @Override
  public Application getApplication(String zone, String applicationName) {
    Map<String, Application> applicationsByZone = this.REGISTRY.get(zone);
    if (CollectionUtils.isEmpty(applicationsByZone)) {
      return null;
    }
    return applicationsByZone.get(applicationName);
  }

  @Override
  public List<Instance> instances(String zone, String applicationName) {
    Map<String, Application> applicationsByZone = this.REGISTRY.get(zone);
    if (CollectionUtils.isEmpty(applicationsByZone)) {
      return Collections.emptyList();
    }
    return Optional.ofNullable(applicationsByZone.get(applicationName))
        .map(Application::getInstances)
        .orElse(Collections.emptyList());
  }

  @Nullable
  @Override
  public Instance getInstance(String zone, String applicationName, String instanceId) {
    Map<String, Application> applicationsByZone = this.REGISTRY.get(zone);
    if (CollectionUtils.isEmpty(applicationsByZone)) {
      return null;
    }
    return Optional.ofNullable(applicationsByZone.get(applicationName))
        .flatMap(
            a ->
                Optional.ofNullable(a.getInstances())
                    .flatMap(l -> l.stream().filter(i -> instanceId.equals(i.getId())).findFirst()))
        .orElse(null);
  }

  @Override
  public void refresh() {
    log.debug("start to init application registry in memory");

    Map<String, String> servers = getServiceConfig();
    servers.forEach(
        (key, value) -> {
          EurekaQueryDTO.ApplicationsDTO sourceRegistry;
          try {
            sourceRegistry = getSourceRegistry(value);
          } catch (Exception e) {
            log.error("exception cause when request to eureka server: {}!", value, e);
            return;
          }

          if (sourceRegistry == null || CollectionUtils.isEmpty(sourceRegistry.getApplication())) {
            log.info("can not found application from server: {}", value);
            return;
          }
          final Map<String, Application> applicationMap = new ConcurrentHashMap<>();
          sourceRegistry
              .getApplication()
              .forEach(
                  app ->
                      applicationMap.put(
                          app.getName(),
                          Application.builder()
                              .name(app.getName())
                              .instances(
                                  Optional.ofNullable(app.getInstance())
                                      .map(
                                          l ->
                                              l.stream()
                                                  .map(
                                                      i ->
                                                          Instance.builder()
                                                              .id(i.getInstanceId())
                                                              .hostName(i.getHostName())
                                                              .applicationName(i.getAppName())
                                                              .ipAddress(i.getIpAddress())
                                                              .status(i.getStatus())
                                                              .metadata(i.getMetadata())
                                                              .homePageUrl(i.getHomePageUrl())
                                                              .statusPageUrl(i.getStatusPageUrl())
                                                              .healthCheckUrl(i.getHealthCheckUrl())
                                                              .build())
                                                  .toList())
                                      .orElse(Collections.emptyList()))
                              .build()));

          this.REGISTRY.put(key, Collections.unmodifiableMap(applicationMap));
        });
    log.debug("init application registry in memory complete");
  }

  protected abstract Map<String, String> getServiceConfig();

  protected abstract EurekaQueryDTO.ApplicationsDTO getSourceRegistry(String server);
}
