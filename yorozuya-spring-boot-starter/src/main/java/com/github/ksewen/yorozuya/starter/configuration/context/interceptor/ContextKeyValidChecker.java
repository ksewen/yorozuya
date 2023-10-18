package com.github.ksewen.yorozuya.starter.configuration.context.interceptor;

import com.github.ksewen.yorozuya.starter.configuration.context.ServiceContextProperties;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 17.10.2023 12:08
 */
@RequiredArgsConstructor
public class ContextKeyValidChecker {

  private final ServiceContextProperties properties;

  protected boolean valid(String key) {
    if (this.properties.isTransferWithPrefix()
        && key.startsWith(this.properties.getHeaderPrefix())) {
      return true;
    }
    if (this.properties.isEnableWhiteList() && this.properties.getLimitSet().contains(key)) {
      return true;
    }
    return !this.properties.isEnableWhiteList() && !this.properties.getLimitSet().contains(key);
  }
}
