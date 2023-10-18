package com.github.ksewen.yorozuya.starter.configuration.context;

import com.github.ksewen.yorozuya.common.constant.ContextConstants;
import com.github.ksewen.yorozuya.starter.configuration.context.interceptor.RepetitionStrategyEnum;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ksewen
 * @date 15.10.2023 18:10
 */
@Data
@ConfigurationProperties(prefix = "common.context.service")
public class ServiceContextProperties {

  /** Define the key of the business context key-values. */
  private String headerPrefix = ContextConstants.HEADER_NAME_DEFAULT_PREFIX;

  /** Whether to transmit all key-values prefixed by headerPrefix. */
  private boolean transferWithPrefix = Boolean.TRUE;

  /**
   * The keys defined in Set and the keys prefixed with headerPrefix will be injected into Current
   * Context.
   */
  private Set<String> defaultInjectKeySet = new HashSet<>();

  /**
   * Optimize the control of key-value pairs transmitted with HTTP requests using either a whitelist
   * or a blacklist approach.
   */
  private boolean enableWhiteList = Boolean.TRUE;

  /** Configure whitelist or blacklist. */
  private Set<String> limitSet = new HashSet<>();

  /** Define the processing strategy if http headers already exist with the given key. */
  private RepetitionStrategyEnum repetitionStrategy = RepetitionStrategyEnum.IGNORE;
}
