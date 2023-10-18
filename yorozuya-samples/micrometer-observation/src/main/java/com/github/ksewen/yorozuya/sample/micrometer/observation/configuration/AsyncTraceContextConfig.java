package com.github.ksewen.yorozuya.sample.micrometer.observation.configuration;

import io.micrometer.context.ContextExecutorService;
import io.micrometer.context.ContextSnapshotFactory;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author ksewen
 * @date 13.10.2023 10:53
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class AsyncTraceContextConfig implements AsyncConfigurer {

  @Qualifier("taskExecutor") // if you have more than one task executor pools
  private final ThreadPoolTaskExecutor taskExecutor;

  @Override
  public Executor getAsyncExecutor() {
    return ContextExecutorService.wrap(
        taskExecutor.getThreadPoolExecutor(),
        () -> ContextSnapshotFactory.builder().build().captureAll());
  }
}
