package com.github.ksewen.yorozuya.starter.helper.redis;

import com.github.ksewen.yorozuya.common.constant.SystemConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ksewen
 * @date 24.09.2023 22:20
 */
@Data
@ConfigurationProperties(prefix = "common.helper.redis")
public class RedisHelperProperties {

  private String keyPrefix = SystemConstants.PROPERTIES_NOT_SET_VALUE;

  private String keySplit = ":";
}
