package com.github.ksewen.yorozuya.starter.configuration.http.feign;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author ksewen
 * @date 08.09.2023 13:01
 */
// FIXME: exclude in jacoco report
public class OnNoLoadBalancerOrDisable extends AnyNestedCondition {

  public OnNoLoadBalancerOrDisable() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  @ConditionalOnMissingClass(
      value = {
        "org.springframework.cloud.client.loadbalancer.LoadBalancerClient",
        "org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory"
      })
  static class NoLoadBalancer {}

  @ConditionalOnProperty(
      value = "spring.cloud.loadbalancer.enabled",
      havingValue = "false",
      matchIfMissing = true)
  static class Disabled {}
}
