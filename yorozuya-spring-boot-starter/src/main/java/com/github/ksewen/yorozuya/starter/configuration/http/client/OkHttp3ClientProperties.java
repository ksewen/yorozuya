package com.github.ksewen.yorozuya.starter.configuration.http.client;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author ksewen
 * @date 29.08.2023 11:05
 */
@Data
@ConfigurationProperties(prefix = "common.ok.http.client")
public class OkHttp3ClientProperties {

  /** Determines the timeout until a new connection is fully established. */
  private Duration connectTimeout = Duration.ofSeconds(5);

  /** Determines the read timeout value for I/O operations.. */
  private Duration readTimeout = Duration.ofSeconds(30);

  /** Determines the write timeout value for I/O operations.. */
  private Duration writeTimeout = Duration.ofSeconds(30);

  /**
   * Sets the interval between HTTP/2 and web socket pings initiated by this client. Use this to
   * automatically send ping frames until either the connection fails or it is closed. This keeps
   * the connection alive and may detect connectivity failures.
   *
   * <p>The default value of 0 disables client-initiated pings.
   */
  private Duration pingInterval = Duration.ofSeconds(30);

  @NestedConfigurationProperty
  private final ConnectionPoolProperties connectionPool = new ConnectionPoolProperties();

  @NestedConfigurationProperty
  private final DispatcherProperties dispatcher = new DispatcherProperties();

  @Data
  public static class ConnectionPoolProperties {

    /** The maximum number of idle connections. */
    private int maxIdleConnections = 100;

    /** The time for eviction of idle connections. */
    private Duration keepAliveDuration = Duration.ofMinutes(5);
  }

  @Data
  public static class DispatcherProperties {
    /**
     * The maximum number of requests to execute concurrently. Above this requests queue in memory,
     * waiting for the running calls to complete.
     *
     * <p>If more than maxRequests requests are in flight when this is invoked, those requests will
     * remain in flight.
     */
    private int maxRequests = 100;

    /**
     * The maximum number of requests for each host to execute concurrently. This limits requests by
     * the URL's host name. Note that concurrent requests to a single IP address may still exceed
     * this limit: multiple hostnames may share an IP address or be routed through the same HTTP
     * proxy.
     *
     * <p>If more than maxRequestsPerHost requests are in flight when this is invoked, those
     * requests will remain in flight.
     *
     * <p>WebSocket connections to hosts do not count against this limit.
     */
    private int maxRequestsPerHost = 10;

    /** the number of threads to keep in the pool, even if they are idle. */
    private int corePoolSize = 4;

    /** the maximum number of threads to allow in the pool. */
    private int maximumPoolSize = 8;

    /**
     * when the number of threads is greater than the core, this is the maximum time that excess
     * idle threads will wait for new tasks before terminating.
     */
    private Duration keepAliveTime = Duration.ofSeconds(30);

    /** the size of workQueue. */
    private int workQueueSize = 200;

    /** the name format of the thread in the pool. */
    private String threadFactoryNameFormat = "okhttp3-thread-%d";
  }
}
