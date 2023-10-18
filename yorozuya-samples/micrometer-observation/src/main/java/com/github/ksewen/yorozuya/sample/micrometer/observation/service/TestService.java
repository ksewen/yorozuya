package com.github.ksewen.yorozuya.sample.micrometer.observation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author ksewen
 * @date 13.10.2023 10:35
 */
@Service
@Slf4j
public class TestService {

  @Async
  public void log() {
    log.info("this is a log in async service.");
  }
}
