package com.github.ksewen.yorozuya.starter.configuration.http.client;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.httpcomponents.hc5.PoolingHttpClientConnectionManagerMetricsBinder;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * If you need to specifically use @see <a
 * href="https://mvnrepository.com/artifact/org.apache.httpcomponents.client5/httpclient5">httpclient5</a>,
 * you should explicitly import the required dependencies and exclude the {@link
 * com.github.ksewen.yorozuya.starter.configuration.http.client.OkHttp3ClientAutoConfiguration}.
 *
 * @author ksewen
 * @date 28.08.2023 16:16
 */
@Configuration
@ConditionalOnClass(HttpClient.class)
@ConditionalOnMissingBean(OkHttp3ClientAutoConfiguration.class)
@EnableConfigurationProperties(HttpClientProperties.class)
@ConditionalOnProperty(value = "common.http.client.hc5.enabled")
@AutoConfigureAfter(OkHttp3ClientAutoConfiguration.class)
@RequiredArgsConstructor
public class HttpClientAutoConfiguration {

  private final HttpClientProperties httpClientProperties;

  private CloseableHttpClient httpClient;

  @Bean
  @ConditionalOnMissingBean(HttpClient.class)
  public CloseableHttpClient httpClient(
      @Autowired HttpClientConnectionManager connectionManager,
      @Autowired HttpRequestRetryStrategy retryStrategy,
      @Autowired(required = false) ConnectionKeepAliveStrategy keepAliveStrategy) {
    HttpClientBuilder builder =
        HttpClients.custom()
            .setConnectionManager(connectionManager)
            .evictIdleConnections(TimeValue.of(this.httpClientProperties.getMaxIdleTime()))
            .evictExpiredConnections()
            .setRetryStrategy(
                retryStrategy != null ? retryStrategy : new DefaultHttpRequestRetryStrategy());

    if (keepAliveStrategy != null) {
      builder.setKeepAliveStrategy(keepAliveStrategy);
    }

    this.httpClient = builder.build();

    return this.httpClient;
  }

  @Bean
  @ConditionalOnMissingBean(HttpClientConnectionManager.class)
  public HttpClientConnectionManager connectionManager(
      @Autowired(required = false) MeterRegistry registry) {
    PoolingHttpClientConnectionManager connectionManager =
        PoolingHttpClientConnectionManagerBuilder.create()
            .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
            .setConnPoolPolicy(PoolReusePolicy.LIFO)
            .build();

    connectionManager.setDefaultSocketConfig(
        SocketConfig.custom()
            .setTcpNoDelay(this.httpClientProperties.isTcpNoDelay())
            .setSoTimeout(Timeout.of(this.httpClientProperties.getSocketTimeout()))
            .build());

    connectionManager.setDefaultConnectionConfig(
        ConnectionConfig.custom()
            .setConnectTimeout(Timeout.of(this.httpClientProperties.getConnectTimeout()))
            .setSocketTimeout(Timeout.of(this.httpClientProperties.getSocketTimeout()))
            .setValidateAfterInactivity(
                TimeValue.of(this.httpClientProperties.getValidateAfterInactivity()))
            .setTimeToLive(TimeValue.of(this.httpClientProperties.getTimeToLive()))
            .build());

    connectionManager.setMaxTotal(this.httpClientProperties.getConnectionMaxTotal());
    connectionManager.setDefaultMaxPerRoute(
        this.httpClientProperties.getConnectionDefaultMaxPerRoute());

    if (registry != null) {
      new PoolingHttpClientConnectionManagerMetricsBinder(
              connectionManager, "common-http-client-pool")
          .bindTo(registry);
    }
    return connectionManager;
  }

  @Bean
  @ConditionalOnMissingBean(HttpRequestRetryStrategy.class)
  public HttpRequestRetryStrategy retryStrategy() {
    return new DefaultHttpRequestRetryStrategy(
        this.httpClientProperties.getMaxRetries(),
        TimeValue.of(this.httpClientProperties.getDefaultRetryInterval()));
  }

  @PreDestroy
  public void destroy() {
    if (httpClient != null) {
      httpClient.close(CloseMode.GRACEFUL);
    }
  }
}
