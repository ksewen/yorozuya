package com.github.ksewen.yorozuya.dashboard.service.impl;

import static org.mockito.Mockito.*;

import com.github.ksewen.yorozuya.dashboard.dto.EurekaQueryDTO;
import com.github.ksewen.yorozuya.starter.helper.http.client.RestClientHelpers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author ksewen
 * @date 31.10.2023 16:29
 */
@SpringBootTest(classes = EurekaServiceImpl.class)
class EurekaServiceImplTest {

  @Autowired private EurekaServiceImpl eurekaService;

  @MockBean private RestTemplate restTemplate;

  @MockBean private RestClientHelpers restClientHelpers;

  @Test
  void applications() {
    final String server = "http://127.0.0.1:8001";
    @SuppressWarnings("unchecked")
    ResponseEntity<EurekaQueryDTO> mockResult = mock(ResponseEntity.class);
    when(this.restTemplate.exchange(
            anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(EurekaQueryDTO.class)))
        .thenReturn(mockResult);

    this.eurekaService.applications(server);
    verify(this.restClientHelpers, times(1)).buildDefaultHeaders();
    verify(this.restTemplate, times(1))
        .exchange(
            eq(server + "/eureka/apps"),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(EurekaQueryDTO.class));
  }
}
