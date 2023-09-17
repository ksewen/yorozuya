package com.github.ksewen.yorozuya.common.environment;

import java.util.Map;

/**
 * @author ksewen
 * @date 25.08.2023 15:24
 */
public interface Environment {

  String getHostName();

  String getHostIp();

  String getEnvironment();

  String getApplicationName();

  Map<String, String> getMetadata();
}
