package com.github.ksewen.yorozuya.dashboard.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.github.ksewen.yorozuya.dashboard.dto.EurekaQueryDTO;
import com.github.ksewen.yorozuya.dashboard.model.Application;
import com.github.ksewen.yorozuya.dashboard.model.Instance;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Answers;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.CollectionUtils;

/**
 * @author ksewen
 * @date 31.10.2023 17:08
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AbstractEurekaApplicationRegistryTest {

  private final AbstractEurekaApplicationRegistry mockAbstractEurekaApplicationRegistry =
      mock(AbstractEurekaApplicationRegistry.class, Answers.CALLS_REAL_METHODS);

  @SuppressWarnings("unchecked")
  private final Map<String, Map<String, Application>> MAP = mock(Map.class);

  {
    ReflectionTestUtils.setField(this.mockAbstractEurekaApplicationRegistry, "REGISTRY", this.MAP);
  }

  private final String TEST_ZONE = "test";

  private final String TEST_SERVER_URL = "http://127.0.0.1:8001";

  private final String APP_1 = "app-1";

  private final String APP_2 = "app-2";

  private final String INSTANCE_1 = this.APP_1 + "-1";

  private final String INSTANCE_2 = this.APP_1 + "-2";

  @Test
  void applications() {
    when(this.MAP.get(this.TEST_ZONE))
        .thenReturn(
            Map.ofEntries(
                Map.entry(
                    this.APP_1,
                    Application.builder()
                        .name(this.APP_1)
                        .instances(
                            Arrays.asList(
                                Instance.builder().id(this.INSTANCE_1).build(),
                                Instance.builder().id(this.INSTANCE_2).build()))
                        .build()),
                Map.entry(
                    this.APP_2,
                    Application.builder()
                        .name(this.APP_2)
                        .instances(
                            Arrays.asList(
                                Instance.builder().id(this.APP_2 + "-1").build(),
                                Instance.builder().id(this.APP_2 + "-2").build(),
                                Instance.builder().id(this.APP_2 + "-3").build()))
                        .build())));
    List<Application> result =
        this.mockAbstractEurekaApplicationRegistry.applications(
            this.TEST_ZONE, List.of(this.APP_2));
    assertThat(result)
        .isNotNull()
        .hasSize(1)
        .matches(
            l -> {
              Application application = l.get(0);
              return application != null
                  && this.APP_2.equals(application.getName())
                  && !CollectionUtils.isEmpty(application.getInstances())
                  && application.getInstances().size() == 3;
            });
  }

  @Test
  void applicationsWithEmptyRegistry() {
    List<Application> result =
        this.mockAbstractEurekaApplicationRegistry.applications(
            this.TEST_ZONE, List.of(this.APP_1));
    assertThat(result).isNotNull().isEmpty();
  }

  @Test
  void applicationsWithEmptyGivenNames() {
    when(this.MAP.get(this.TEST_ZONE))
        .thenReturn(
            Map.ofEntries(
                Map.entry(this.APP_1, Application.builder().name(this.APP_1).build()),
                Map.entry(this.APP_2, Application.builder().name(this.APP_2).build())));
    List<Application> result =
        this.mockAbstractEurekaApplicationRegistry.applications(this.TEST_ZONE, null);
    assertThat(result)
        .isNotNull()
        .hasSize(2)
        .matches(l -> l.stream().anyMatch(i -> this.APP_1.equals(i.getName())))
        .matches(l -> l.stream().anyMatch(i -> this.APP_2.equals(i.getName())));
  }

  @Test
  void applicationsWithNoAliveInstances() {
    final String app3 = "app-3";
    when(this.MAP.get(this.TEST_ZONE))
        .thenReturn(
            Map.ofEntries(
                Map.entry(this.APP_1, Application.builder().name(this.APP_1).build()),
                Map.entry(this.APP_2, Application.builder().name(this.APP_2).build())));
    List<Application> result =
        this.mockAbstractEurekaApplicationRegistry.applications(this.TEST_ZONE, List.of(app3));
    assertThat(result)
        .isNotNull()
        .hasSize(1)
        .matches(
            l -> {
              Application application = l.get(0);
              return application != null
                  && app3.equals(application.getName())
                  && application.getInstances() != null
                  && application.getInstances().size() == 0;
            });
  }

  @Test
  void getApplication() {
    when(this.MAP.get(this.TEST_ZONE))
        .thenReturn(
            Map.ofEntries(
                Map.entry(
                    this.APP_1,
                    Application.builder()
                        .name(this.APP_1)
                        .instances(
                            Arrays.asList(
                                Instance.builder().id(this.INSTANCE_1).build(),
                                Instance.builder().id(this.INSTANCE_2).build()))
                        .build())));

    Application result =
        this.mockAbstractEurekaApplicationRegistry.getApplication(this.TEST_ZONE, this.APP_1);
    assertThat(result)
        .isNotNull()
        .matches(a -> this.APP_1.equals(a.getName()))
        .matches(a -> !CollectionUtils.isEmpty(a.getInstances()))
        .matches(a -> a.getInstances().size() == 2);
  }

  @Test
  void getApplicationWithNoResult() {
    Application result =
        this.mockAbstractEurekaApplicationRegistry.getApplication(this.TEST_ZONE, this.APP_1);
    assertThat(result).isNull();
  }

  @Test
  void instances() {
    when(this.MAP.get(this.TEST_ZONE))
        .thenReturn(
            Map.ofEntries(
                Map.entry(
                    this.APP_1,
                    Application.builder()
                        .name(this.APP_1)
                        .instances(
                            Arrays.asList(
                                Instance.builder().id(this.INSTANCE_1).build(),
                                Instance.builder().id(this.INSTANCE_2).build()))
                        .build())));

    List<Instance> result =
        this.mockAbstractEurekaApplicationRegistry.instances(this.TEST_ZONE, this.APP_1);
    assertThat(result)
        .isNotNull()
        .isNotEmpty()
        .hasSize(2)
        .matches(l -> l.stream().anyMatch(i -> (this.INSTANCE_1).equals(i.getId())))
        .matches(l -> l.stream().anyMatch(i -> (this.INSTANCE_2).equals(i.getId())));
  }

  @Test
  void instancesWithEmptyRegistry() {
    when(this.MAP.get(this.TEST_ZONE))
        .thenReturn(
            Map.ofEntries(Map.entry(this.APP_1, Application.builder().name(this.APP_1).build())));

    List<Instance> result =
        this.mockAbstractEurekaApplicationRegistry.instances(this.TEST_ZONE, this.APP_1);
    assertThat(result).isNotNull().isEmpty();
  }

  @Test
  void instancesWithEmptyInstances() {
    List<Instance> result =
        this.mockAbstractEurekaApplicationRegistry.instances(this.TEST_ZONE, this.APP_1);
    assertThat(result).isNotNull().isEmpty();
  }

  @Test
  void getInstance() {
    when(this.MAP.get(this.TEST_ZONE))
        .thenReturn(
            Map.ofEntries(
                Map.entry(
                    this.APP_1,
                    Application.builder()
                        .name(this.APP_1)
                        .instances(
                            Arrays.asList(
                                Instance.builder().id(this.INSTANCE_1).build(),
                                Instance.builder().id(this.INSTANCE_2).build()))
                        .build())));

    Instance result =
        this.mockAbstractEurekaApplicationRegistry.getInstance(
            this.TEST_ZONE, this.APP_1, this.INSTANCE_2);
    assertThat(result).isNotNull().matches(i -> this.INSTANCE_2.equals(i.getId()));
  }

  @Test
  void getInstanceWithEmptyRegistry() {
    Instance result =
        this.mockAbstractEurekaApplicationRegistry.getInstance(
            this.TEST_ZONE, this.APP_1, this.INSTANCE_1);
    assertThat(result).isNull();
  }

  @Test
  void getInstanceWithEmptyInstance() {
    when(this.MAP.get(this.TEST_ZONE))
        .thenReturn(
            Map.ofEntries(Map.entry(this.APP_1, Application.builder().name(this.APP_1).build())));
    Instance result =
        this.mockAbstractEurekaApplicationRegistry.getInstance(
            this.TEST_ZONE, this.APP_1, this.INSTANCE_1);
    assertThat(result).isNull();
  }

  @Test
  void getInstanceWithNoResult() {
    when(this.MAP.get(this.TEST_ZONE))
        .thenReturn(
            Map.ofEntries(
                Map.entry(
                    this.APP_1,
                    Application.builder()
                        .name(this.APP_1)
                        .instances(
                            Collections.singletonList(
                                Instance.builder().id(this.INSTANCE_1).build()))
                        .build())));

    Instance result =
        this.mockAbstractEurekaApplicationRegistry.getInstance(
            this.TEST_ZONE, this.APP_1, this.INSTANCE_2);
    assertThat(result).isNull();
  }

  @Test
  void refresh() {
    when(this.mockAbstractEurekaApplicationRegistry.getServiceConfig())
        .thenReturn(
            Map.ofEntries(
                Map.entry(this.TEST_ZONE, this.TEST_SERVER_URL),
                Map.entry(
                    EurekaClientConfigBean.DEFAULT_ZONE, EurekaClientConfigBean.DEFAULT_URL)));
    when(this.mockAbstractEurekaApplicationRegistry.getSourceRegistry(this.TEST_SERVER_URL))
        .thenReturn(
            EurekaQueryDTO.ApplicationsDTO.builder()
                .application(
                    Arrays.asList(
                        EurekaQueryDTO.ApplicationItem.builder()
                            .name(this.APP_1)
                            .instance(
                                Arrays.asList(
                                    EurekaQueryDTO.InstanceItem.builder()
                                        .instanceId(this.INSTANCE_1)
                                        .build(),
                                    EurekaQueryDTO.InstanceItem.builder()
                                        .instanceId(this.INSTANCE_2)
                                        .build()))
                            .build(),
                        EurekaQueryDTO.ApplicationItem.builder()
                            .name(this.APP_2)
                            .instance(
                                Arrays.asList(
                                    EurekaQueryDTO.InstanceItem.builder()
                                        .instanceId(this.APP_2 + "-1")
                                        .build(),
                                    EurekaQueryDTO.InstanceItem.builder()
                                        .instanceId(this.APP_2 + "-2")
                                        .build()))
                            .build()))
                .build());
    when(this.mockAbstractEurekaApplicationRegistry.getSourceRegistry(
            EurekaClientConfigBean.DEFAULT_URL))
        .thenReturn(
            EurekaQueryDTO.ApplicationsDTO.builder()
                .application(
                    Collections.singletonList(
                        EurekaQueryDTO.ApplicationItem.builder()
                            .name(this.APP_1)
                            .instance(
                                Arrays.asList(
                                    EurekaQueryDTO.InstanceItem.builder()
                                        .instanceId(this.INSTANCE_1)
                                        .build(),
                                    EurekaQueryDTO.InstanceItem.builder()
                                        .instanceId(this.INSTANCE_2)
                                        .build()))
                            .build()))
                .build());

    this.mockAbstractEurekaApplicationRegistry.refresh();
    verify(this.MAP, times(1))
        .put(eq(this.TEST_ZONE), argThat(m -> !CollectionUtils.isEmpty(m) && m.size() == 2));
    verify(this.MAP, times(1))
        .put(
            eq(EurekaClientConfigBean.DEFAULT_ZONE),
            argThat(m -> !CollectionUtils.isEmpty(m) && m.size() == 1));
  }

  @Test
  void refreshWithFetchError() {
    when(this.mockAbstractEurekaApplicationRegistry.getServiceConfig())
        .thenReturn(Map.ofEntries(Map.entry(this.TEST_ZONE, this.TEST_SERVER_URL)));
    when(this.mockAbstractEurekaApplicationRegistry.getSourceRegistry(this.TEST_SERVER_URL))
        .thenThrow(new RuntimeException());

    this.mockAbstractEurekaApplicationRegistry.refresh();
    verify(this.MAP, never()).put(anyString(), anyMap());
  }

  @Test
  void refreshWithEmptyRegistry() {
    when(this.mockAbstractEurekaApplicationRegistry.getServiceConfig())
        .thenReturn(Map.ofEntries(Map.entry(this.TEST_ZONE, this.TEST_SERVER_URL)));

    this.mockAbstractEurekaApplicationRegistry.refresh();
    verify(this.MAP, never()).put(anyString(), anyMap());
  }
}
