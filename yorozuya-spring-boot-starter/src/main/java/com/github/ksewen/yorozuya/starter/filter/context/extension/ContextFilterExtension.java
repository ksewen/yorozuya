package com.github.ksewen.yorozuya.starter.filter.context.extension;

import jakarta.servlet.ServletRequest;

/**
 * @author ksewen
 * @date 15.10.2023 17:29
 */
public interface ContextFilterExtension {

  /**
   * inject key-values into current Context.
   *
   * @param servletRequest
   */
  void inject(ServletRequest servletRequest);
}
