package com.github.ksewen.yorozuya.dashboard.service.impl;

import com.github.ksewen.yorozuya.dashboard.dto.EurekaQueryDTO;
import com.github.ksewen.yorozuya.dashboard.service.EurekaServerManager;
import com.github.ksewen.yorozuya.dashboard.service.EurekaService;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 31.10.2023 16:13
 */
@RequiredArgsConstructor
public class RestEurekaApplicationRegistry extends AbstractEurekaApplicationRegistry {

  private final EurekaServerManager eurekaServerManager;

  private final EurekaService eurekaService;

  @Override
  protected Map<String, String> getServiceConfig() {
    return this.eurekaServerManager.serviceConfig();
  }

  @Override
  protected EurekaQueryDTO.ApplicationsDTO getSourceRegistry(String server) {
    return Optional.ofNullable(this.eurekaService.applications(server))
        .map(EurekaQueryDTO::getApplications)
        .orElse(null);
  }
}
