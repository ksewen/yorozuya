package com.github.ksewen.yorozuya.dashboard.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.github.ksewen.yorozuya.dashboard.dto.EurekaQueryDTO;
import com.github.ksewen.yorozuya.dashboard.service.EurekaServerManager;
import com.github.ksewen.yorozuya.dashboard.service.EurekaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author ksewen
 * @date 31.10.2023 16:55
 */
@SpringBootTest(classes = RestEurekaApplicationRegistry.class)
class RestEurekaApplicationRegistryTest {

  @Autowired private RestEurekaApplicationRegistry restEurekaApplicationRegistry;

  @MockBean private EurekaServerManager eurekaServerManager;

  @MockBean private EurekaService eurekaService;

  private final String SERVER = "http://127.0.0.1:8001";

  @Test
  void getServiceConfig() {
    this.restEurekaApplicationRegistry.getServiceConfig();

    verify(this.eurekaServerManager, times(1)).serviceConfig();
  }

  @Test
  void getSourceRegistry() {
    EurekaQueryDTO.ApplicationsDTO mockApplicationDTO = mock(EurekaQueryDTO.ApplicationsDTO.class);
    when(this.eurekaService.applications(this.SERVER))
        .thenReturn(EurekaQueryDTO.builder().applications(mockApplicationDTO).build());

    EurekaQueryDTO.ApplicationsDTO result =
        this.restEurekaApplicationRegistry.getSourceRegistry(this.SERVER);
    assertThat(result).isNotNull().isEqualTo(mockApplicationDTO);
  }
}
