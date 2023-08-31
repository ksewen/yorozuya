package com.github.ksewen.yorozuya.common.environment.impl;

import com.github.ksewen.yorozuya.common.constant.SystemConstants;
import com.github.ksewen.yorozuya.common.environment.Environment;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ksewen
 * @date 25.08.2023 16:08
 */
@Slf4j
public class BasicEnvironment implements Environment {

  private String hostName;

  private String hostIp;

  private String environment = SystemConstants.PROPERTIES_NOT_SET_VALUE;

  private String applicationName = SystemConstants.PROPERTIES_NOT_SET_VALUE;

  private Map<String, String> metadata = new HashMap<>();

  @Override
  public String getHostName() {
    return this.hostName;
  }

  @Override
  public String getHostIp() {
    return this.hostIp;
  }

  @Override
  public String getEnvironment() {
    return this.environment;
  }

  @Override
  public String getApplicationName() {
    return this.applicationName;
  }

  @Override
  public Map<String, String> getMetadata() {
    return this.metadata;
  }

  private static BasicEnvironment newInstance() {
    BasicEnvironment basicEnvironment = new BasicEnvironment();
    basicEnvironment.initHostInfo();
    return basicEnvironment;
  }

  private void initHostInfo() {
    try {
      InetAddress address = InetAddress.getLocalHost();
      this.hostName = address.getHostName();
      this.hostIp = address.getHostAddress();
    } catch (UnknownHostException e) {
      log.error("Can not init hostName and hostIp", e);
    }
  }

  public static final class SingletonHolder {
    public static final BasicEnvironment instance = BasicEnvironment.newInstance();
  }

  public void initEnvironment(String environment) {
    if (SystemConstants.PROPERTIES_NOT_SET_VALUE.equals(this.environment)) {
      this.environment = environment;
    }
  }

  public void initApplicationName(String applicationName) {
    if (SystemConstants.PROPERTIES_NOT_SET_VALUE.equals(this.applicationName)) {
      this.applicationName = applicationName;
    }
  }

  public static BasicEnvironment getInstance() {
    return SingletonHolder.instance;
  }
}
