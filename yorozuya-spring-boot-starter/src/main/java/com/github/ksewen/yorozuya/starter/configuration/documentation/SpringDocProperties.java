package com.github.ksewen.yorozuya.starter.configuration.documentation;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author ksewen
 * @date 31.08.2023 22:12
 */
@Data
@ConfigurationProperties(prefix = "springdoc")
public class SpringDocProperties {

  private String title;

  private String version = "1.0.0";

  private String description;

  @NestedConfigurationProperty private final LicenseProperties license = new LicenseProperties();

  @Data
  public static class LicenseProperties {

    private String name = "MIT";

    private String url = "https://opensource.org/license/mit/";
  }
}
