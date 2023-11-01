package com.github.ksewen.yorozuya.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.netflix.appinfo.LeaseInfo;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

/**
 * @author ksewen
 * @date 27.10.2023 20:41
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EurekaQueryDTO {

  private ApplicationsDTO applications;

  @Data
  @Builder
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class ApplicationsDTO {

    @JsonProperty("versions__delta")
    private Long versionDelta;

    @JsonProperty("apps__hashcode")
    private String appsHashCode;

    private List<ApplicationItem> application;
  }

  @Data
  @Builder
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class ApplicationItem {

    private String name;

    List<InstanceItem> instance;
  }

  @Data
  @Builder
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class InstanceItem {

    private String instanceId;

    private String hostName;

    @JsonProperty("app")
    private String appName;

    @JsonProperty("ipAddr")
    private String ipAddress;

    private String status;

    @JsonProperty("overriddenstatus")
    private String overriddenStatus;

    private NodePort port;

    private NodePort securePort;

    private Map<String, String> metadata;

    private String homePageUrl;

    private String statusPageUrl;

    private String healthCheckUrl;

    private String vipAddress;

    private String secureVipAddress;

    private LeaseInfo leaseInfo;
  }

  @Data
  @Builder
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class NodePort {

    @JsonProperty("$")
    private int port;

    @JsonProperty("@enabled")
    private String enable;
  }
}
