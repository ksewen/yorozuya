package com.github.ksewen.yorozuya.starter.configuration.http.client;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.httpcomponents.hc5.PoolingHttpClientConnectionManagerMetricsBinder;
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
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@EnableConfigurationProperties({HttpClientProperties.class})
@RequiredArgsConstructor
public class HttpClientAutoConfiguration {

  private final HttpClientProperties httpClientProperties;

  @Bean
  @ConditionalOnMissingBean(HttpClient.class)
  public CloseableHttpClient httpClient(
      @Autowired HttpClientConnectionManager connectionManager,
      @Autowired HttpRequestRetryStrategy retryStrategy,
      @Autowired(required = false) ConnectionKeepAliveStrategy keepAliveStrategy) {
    HttpClientBuilder builder =
        HttpClients.custom()
            .setConnectionManager(connectionManager)
            .evictIdleConnections(
                TimeValue.ofMicroseconds(this.httpClientProperties.getMaxIdleTime().toMillis()))
            .evictExpiredConnections()
            .setRetryStrategy(
                retryStrategy != null ? retryStrategy : new DefaultHttpRequestRetryStrategy());

    if (keepAliveStrategy != null) {
      builder.setKeepAliveStrategy(keepAliveStrategy);
    }

    return builder.build();
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
        SocketConfig.custom().setTcpNoDelay(this.httpClientProperties.isTcpNoDelay()).build());

    connectionManager.setDefaultConnectionConfig(
        ConnectionConfig.custom()
            .setConnectTimeout(
                Timeout.ofMicroseconds(this.httpClientProperties.getConnectTimeout().toMillis()))
            .setSocketTimeout(
                Timeout.ofMicroseconds(this.httpClientProperties.getSocketTimeout().toMillis()))
            .setValidateAfterInactivity(
                TimeValue.ofMicroseconds(
                    this.httpClientProperties.getValidateAfterInactivity().toMillis()))
            .setTimeToLive(
                TimeValue.ofMicroseconds(this.httpClientProperties.getTimeToLive().toMillis()))
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
        TimeValue.ofMicroseconds(this.httpClientProperties.getDefaultRetryInterval().toMillis()));
  }
}
