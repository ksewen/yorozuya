package com.github.ksewen.yorozuya.starter.configuration.http.client;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ksewen
 * @date 28.08.2023 16:23
 */
@Data
@ConfigurationProperties(prefix = "common.http.client")
public class HttpClientProperties {

  /**
   * Determines the timeout until a new connection is fully established.
   *
   * <p>A timeout value of zero is interpreted as an infinite timeout.
   */
  private Duration connectTimeout = Duration.ofSeconds(5);

  /** Determines the default socket timeout value for I/O operations. */
  private Duration socketTimeout = Duration.ofSeconds(30);

  /**
   * maximum time persistent connections can stay idle while kept alive in the connection pool.
   * Connections whose inactivity period exceeds this value will get closed and evicted from the
   * pool.
   */
  private Duration maxIdleTime = Duration.ofSeconds(60);

  /** the maximal value of total connection. */
  private int connectionMaxTotal = 100;

  /** the maximal value per route of total connection. */
  private int connectionDefaultMaxPerRoute = 10;

  /**
   * Defines period of inactivity after which persistent connections must be re-validated prior to
   * being leased to the consumer. Negative values passed to this method disable connection
   * validation.
   */
  private Duration validateAfterInactivity = Duration.ofSeconds(10);

  /** Defines the total span of time connections can be kept alive or execute requests. */
  private Duration timeToLive = Duration.ofSeconds(1000);

  /**
   * Determines the default value of the java.net.SocketOptions.TCP_NODELAY parameter for newly
   * created sockets.
   */
  private boolean tcpNoDelay = true;

  /** Maximum number of allowed retries. */
  private int maxRetries = 3;

  /** Retry interval between subsequent retries. */
  private Duration defaultRetryInterval = Duration.ofSeconds(1);
}
