package com.github.ksewen.yorozuya.dashboard.model;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

/**
 * @author ksewen
 * @date 26.10.2023 15:03
 */
@Data
@Builder
public class Instance {

  private String id;

  private String hostName;

  private String applicationName;

  private String ipAddress;

  private String status;

  private Map<String, String> metadata;

  private String homePageUrl;

  private String statusPageUrl;

  private String healthCheckUrl;
}
