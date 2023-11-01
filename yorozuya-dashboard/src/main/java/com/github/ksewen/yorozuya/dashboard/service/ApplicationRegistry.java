package com.github.ksewen.yorozuya.dashboard.service;

import com.github.ksewen.yorozuya.dashboard.model.Application;
import com.github.ksewen.yorozuya.dashboard.model.Instance;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

/**
 * @author ksewen
 * @date 25.10.2023 17:52
 */
public interface ApplicationRegistry {

  /**
   * Get the Applications based on the given {@code applicationNames}. If the {@code
   * applicationNames} is {@literal null}, all data is returned.
   *
   * @param zone must be not {@literal null}
   * @param applicationNames
   * @return
   */
  List<Application> applications(String zone, @Nullable Collection<String> applicationNames);

  /**
   * Get the Application based on the given {@code applicationName}
   *
   * @param zone must be not {@literal null}
   * @param applicationName must be not {@literal null}
   * @return null if it does not exist
   */
  @Nullable
  Application getApplication(String zone, String applicationName);

  /**
   * Get the List has Instances based on the given {@code applicationName}
   *
   * @param zone must be not {@literal null}
   * @param applicationName must be not {@literal null}
   * @return
   */
  List<Instance> instances(String zone, String applicationName);

  /**
   * Get the Instance based on the given {@code applicationName} and {@code instanceId}
   *
   * @param zone must be not {@literal null}
   * @param applicationName must be not {@literal null}
   * @param instanceId must be not {@literal null}
   * @return null if it does not exist
   */
  @Nullable
  Instance getInstance(String zone, String applicationName, String instanceId);

  /**
   * refresh the registry
   *
   * @throws com.github.ksewen.yorozuya.common.exception.ResourceNotFoundException if can not find a
   *     valid service list by given zone
   */
  void refresh();
}
