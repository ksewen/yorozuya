package com.github.ksewen.yorozuya.starter.configuration.http.client;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 29.08.2023 11:03
 */
@Configuration
@ConditionalOnClass(OkHttpClient.class)
@EnableConfigurationProperties(OkHttp3ClientProperties.class)
@ConditionalOnProperty(
    value = "common.ok.http.client.enabled",
    havingValue = "true",
    matchIfMissing = true)
@RequiredArgsConstructor
public class OkHttp3ClientAutoConfiguration {

  private final OkHttp3ClientProperties okHttp3ClientProperties;

  private OkHttpClient okHttpClient;

  @Bean
  @ConditionalOnMissingBean(OkHttpClient.class)
  public OkHttpClient okHttpClient(
      @Autowired ConnectionPool connectionPool, @Autowired Dispatcher dispatcher) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();

    builder.connectTimeout(
        this.okHttp3ClientProperties.getConnectTimeout().toMillis(), TimeUnit.MILLISECONDS);
    builder.readTimeout(
        this.okHttp3ClientProperties.getReadTimeout().toMillis(), TimeUnit.MILLISECONDS);
    builder.writeTimeout(
        this.okHttp3ClientProperties.getWriteTimeout().toMillis(), TimeUnit.MILLISECONDS);
    builder.pingInterval(
        this.okHttp3ClientProperties.getPingInterval().toMillis(), TimeUnit.MILLISECONDS);

    builder.connectionPool(connectionPool);
    builder.dispatcher(dispatcher);
    this.okHttpClient = builder.build();
    return this.okHttpClient;
  }

  @Bean
  @ConditionalOnMissingBean(ConnectionPool.class)
  public ConnectionPool okHttp3ConnectionPool() {
    int maxIdleConnections =
        this.okHttp3ClientProperties.getConnectionPool().getMaxIdleConnections();
    Duration keepAliveDuration =
        this.okHttp3ClientProperties.getConnectionPool().getKeepAliveDuration();
    return new ConnectionPool(
        maxIdleConnections, keepAliveDuration.toMillis(), TimeUnit.MILLISECONDS);
  }

  @Bean
  @ConditionalOnMissingBean(Dispatcher.class)
  public Dispatcher dispatcher() {
    Dispatcher dispatcher =
        new Dispatcher(
            new ThreadPoolExecutor(
                this.okHttp3ClientProperties.getDispatcher().getCorePoolSize(),
                this.okHttp3ClientProperties.getDispatcher().getMaximumPoolSize(),
                this.okHttp3ClientProperties.getDispatcher().getKeepAliveTime().toMillis(),
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(
                    this.okHttp3ClientProperties.getDispatcher().getWorkQueueSize()),
                new ThreadFactoryBuilder()
                    .setNameFormat(
                        this.okHttp3ClientProperties.getDispatcher().getThreadFactoryNameFormat())
                    .build()));
    dispatcher.setMaxRequests(this.okHttp3ClientProperties.getDispatcher().getMaxRequests());
    dispatcher.setMaxRequestsPerHost(
        this.okHttp3ClientProperties.getDispatcher().getMaxRequestsPerHost());
    return dispatcher;
  }

  @PreDestroy
  public void destroy() {
    if (this.okHttpClient != null) {
      this.okHttpClient.dispatcher().executorService().shutdown();
      this.okHttpClient.connectionPool().evictAll();
    }
  }
}
