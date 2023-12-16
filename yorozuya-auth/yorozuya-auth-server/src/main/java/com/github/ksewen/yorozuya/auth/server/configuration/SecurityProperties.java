package com.github.ksewen.yorozuya.auth.server.configuration;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ksewen
 * @date 10.12.2023 21:46
 */
@Data
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

  private Map<String, String> permitUrls = new HashMap<>();

  private boolean protectManagementEndpoints = Boolean.TRUE;
}
