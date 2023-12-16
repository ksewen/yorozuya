# YOROZUYA-SPRING-BOOT-STARTER

[Deutsch](./README_DE.md) | [简体中文](./README_CN.md)

This starter offers automated configurations for seamless integration of a microservice into the
group.

## Environment

With the [
Environment](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/environment/Environment.java)
interface, you can retrieve runtime environment information such as hostName, hostIp, environment (for example:
production, test, develop...) and applicationName. You can also include your custom metadata.  
This starter uses and injects a bean of the default implementation [
BasicEnvironment](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/environment/impl/BasicEnvironment.java)
. To replace it with your own implementation, just use @Autoconfiguration class and inject the bean!

usage:

```java 

@Component
@RequiredArgsConstructor
public class Sample {

    private final Environment environment;

    // get hostname
    String hostName = this.environment.getHostName();

    // get host ip
    String hostIp = this.environment.getHostIp();

    // get enviroment
    String activEnvironment = this.environment.getEnvironment();

    // get application name
    String applicationName = this.environment.getApplicationName();

    // get metadata map
    Map<String, String> metadata = this.environment.getMetadata();
}
```

## Data Source

### Spring Data JPA

"spring-boot-data-jpa-starter" is already a user-friendly component. To connect to a database, please add dependency of
a driver, for example:

```xml

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

and config flowing properties:

```yaml
spring:
  datasource:
    url: # JDBC URL of the database
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: # username of the database.
    password: # password of the database
```

See [JPA documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/) and learn more about it.

In the [sample project](../yorozuya-samples/spring-data-jpa), you can find a simple use case of integrating
testcontainers for integration testing. **Docker is required**.

### Spring Data Redis

In a typical scenario, use the following configuration to connect to Redis:

```yaml
spring:
  data:
    redis:
      host: # host of redis
      port: # port of redis
      password: # password of redis
      database: # index of database
```

If you want to use a connection pool, first add the "org.apache.commons:commons-pool2" dependency in your pom.xml:

```xml

<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>

```

and configure with the following properties:

```yaml
spring:
  data:
    redis:
      lettuce:
        pool:
          max-idle: 16
          max-active: 32
          min-idle: 8
```

If you need Gzip compression support, please use the following configuration to enable it:

```yaml
spring:
  data:
    redis:
      template:
        gzip:
          enable: true
```

See [Spring Data Redis documentation](https://docs.spring.io/spring-data/redis/docs/current/reference/html/) and learn
more about it.

#### RedisHelper

To simplify the operation of common commands, in this starter provides a
default [RedisCommonHelper](./src/main/java/com/github/ksewen/yorozuya/starter/helper/redis/impl/RedisCommonHelpers.java)
based on "StringRedisTemplate" and automatically registers it in the Spring context by default.

See the interface [RedisHelpers](./src/main/java/com/github/ksewen/yorozuya/starter/helper/redis/RedisHelpers.java) to
learn more.

## Rest Clients

Frequently, people utilize OpenFeign to make calls to other servers within a group. Certainly, there are exceptional
situations where you need to invoke an interface outside the group. With this project you can easily solve both of these
issues. Because the two common packages have already been integrated, you can easily access resources via HTTP.

<span id="higher_level_clients">

### Higher level Clients

</span>

#### Openfeign

You can use the annotation @EnableFeignClients to enable the openfeign client:

```java

@SpringBootApplication
@EnableFeignClients
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

To customize the lower level client, you can make use of the following properties:

```shell
# Enable the okhttp3 client for feign
spring.cloud.openfeign.okhttp.enabled=true

# Enable the okhttp3 client for feign
# If spring.cloud.openfeign.okhttp.enabled=true was used, this property is not effective.
spring.cloud.openfeign.httpclient.hc5.enabled=true
```

The Spring Cloud Loadbalancer is enabled by default, to deactivate this function, exclude the
spring-cloud-starter-loadbalancer dependency from pom.xml or use the following properties:

```shell
spring.cloud.loadbalancer.enabled=false
```

For detailed information, please see the projects.  
Without Loadbalancer:

- [rest-client-okhttp](../yorozuya-samples/rest-client-okhttp)
- [rest-client-httpclient](../yorozuya-samples/rest-client-httpclient)

With Loadbalancer:

- [eureka-client](../yorozuya-samples/eureka-client)

#### RestTemplate

RestTemplate is enabled by default, to deactivate this, you can make use of following properties:

```shell
# RestTemplateAutoConfiguration offers two beans, one with load balancer and other without.

# Disable the default restTemplate
common.rest.template.default.enabled=false

# Disable the default restTemplate with Loadbalancer
common.rest.template.loadbalancer.enabled=false
```

OkHttpClient 3 is enabled for RestTemplate by default, you can edit the dependency in pom.xml to replace with
Apache HttpClient 5.

```xml

<dependencies>
    <dependency>
        <groupId>com.github.ksewen</groupId>
        <artifactId>yorozuya-spring-boot-starter</artifactId>
        <exclusions>
            <exclusion>
                <groupId>com.squareup.okhttp3</groupId>
                <artifactId>okhttp</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.apache.httpcomponents.client5</groupId>
        <artifactId>httpclient5</artifactId>
    </dependency>
</dependencies>
```

The default clients can be configured with the properties, please see [Lower level Clients](#lower_level_clients).
To replace with your own client, inject your custom bean please.

For detailed information, please see the projects.  
Without Loadbalancer:

- [rest-client-okhttp](../yorozuya-samples/rest-client-okhttp)
- [rest-client-httpclient](../yorozuya-samples/rest-client-httpclient)

With Loadbalancer:

- [eureka-client](../yorozuya-samples/eureka-client)

<span id="lower_level_clients">

### Lower level Clients

</span>

<span id="okhttp_3">

#### OkHttp 3

</span>

OkHttpClient 3 is enabled by default, to deactivate this, you can make use of following properties:

```shell
# the default value is true
common.ok.http.client.enabled=false
```

See [OkHttp3ClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/OkHttp3ClientProperties.java)
to learn more about the properties.

#### Apache HttpClient 5

To activate this, you can make use of following properties:

```shell
# the default value is true
common.http.client.hc5.enabled=true
```

**Attention:** HttpClient 5 will definitely not register when OkHttpClient 3 is used, please exclude the
okhttp dependency from pom.xml or [use the property](#okhttp_3) to disable it.

```xml

<dependencies>
    <dependency>
        <groupId>com.github.ksewen</groupId>
        <artifactId>yorozuya-spring-boot-starter</artifactId>
        <exclusions>
            <exclusion>
                <groupId>com.squareup.okhttp3</groupId>
                <artifactId>okhttp</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
</dependencies>
```

See [HttpClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/HttpClientProperties.java)
to learn more about the properties.

### Load Balancer

Regarding the usage of Load Balancer in HTTP calls, please refer to the relevant content
in [Higher level Clients](#higher_level_clients).

### The relationships of timeout configuration between higher-level client and lower-level client

Firstly, Spring Cloud Load Balancer does not provide configuration for timeouts, which sets it apart
from [ribbon](https://github.com/Netflix/ribbon), which used to be a popular choice. so there is an even simpler
relationship between them in this project.

#### Openfeign with OkHttp 3 / Apache HttpClient 5

Because Openfeign overrides configurations from the low-level client, it's preferable to use properties for Openfeign
to specifying connection timeout properties.

The better way is to use variables in the properties file, for example:

```yaml
common:
  ok:
    http:
      client:
        connect-timeout: 3000
        read-timeout: 5000
        write-timeout: 5000

spring:
  cloud:
    openfeign:
      client:
        config:
          default:
            connectTimeout: ${common.ok.http.client.connect-timeout}
            read-timeout: ${common.ok.http.client.read-timeout}
```

See [OkHttpClient](https://github.com/OpenFeign/feign/blob/master/okhttp/src/main/java/feign/okhttp/OkHttpClient.java)
and [ApacheHttp5Client](https://github.com/OpenFeign/feign/blob/master/hc5/src/main/java/feign/hc5/ApacheHttp5Client.java)

#### RestTemplate with Okhttp 3

You can configure such settings through the
[OkHttp3ClientHttpRequestFactory#setConnectTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/OkHttp3ClientHttpRequestFactory.java)
,
[OkHttp3ClientHttpRequestFactory#setReadTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/OkHttp3ClientHttpRequestFactory.java)
and
[OkHttp3ClientHttpRequestFactory#setWriteTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/OkHttp3ClientHttpRequestFactory.java)
of RestTemplate. But from the code, it can be seen that a new client instance is created.

So it's the better way to directly use the configurations of OkHttpClient:

```yaml
common:
  ok:
    http:
      client:
        connect-timeout: 3000
        read-timeout: 5000
        write-timeout: 5000
```

To understand all properties, please
see [OkHttp3ClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/OkHttp3ClientProperties.java)

#### RestTemplate with Apache HttpClient 5

By reading the comments
in [HttpComponentsClientHttpRequestFactory#setConnectTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/HttpComponentsClientHttpRequestFactory.java)
and
[HttpComponentsClientHttpRequestFactory#setReadTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/HttpComponentsClientHttpRequestFactory.java)
, it can be understood that these configurations will not take effect.

Just use the following properties:

To understand all properties, please
see [HttpClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/HttpClientProperties.java).

```yaml
common:
  http:
    client:
      hc5:
        enabled: true
        connect-timeout: 3000
        socket-timeout: 5000
```

**PS** In this project, you can specify the SocketTimeout configuration when declaring the
[PoolingHttpClientConnectionManager](https://github.com/apache/httpcomponents-client/blob/master/httpclient5/src/main/java/org/apache/hc/client5/http/impl/io/PoolingHttpClientConnectionManager.java)
.
The [PoolingHttpClientConnectionManager](https://github.com/apache/httpcomponents-client/blob/master/httpclient5/src/main/java/org/apache/hc/client5/http/impl/io/PoolingHttpClientConnectionManager.java)
allows you to set the SocketTimeout
separately
by [SocketConfig](https://github.com/apache/httpcomponents-core/blob/master/httpcore5/src/main/java/org/apache/hc/core5/http/io/SocketConfig.java)
and [ConnectionConfig](https://github.com/apache/httpcomponents-client/blob/master/httpclient5/src/main/java/org/apache/hc/client5/http/config/ConnectionConfig.java)
. However, if you only configure ConnectionConfig#setSocketTimeout(Timeout), it won't take effect. Please achieve this
by configuring SocketConfig#setSoTimeout(Timeout).

This issue is fixed in version 5.2.2 and 5.3-alpha2.
See [Issue in Apache's Jira](https://issues.apache.org/jira/browse/HTTPCLIENT-2299?page=com.atlassian.jira.plugin.system.issuetabpanels%3Aall-tabpanel)

## Circuit Breaker

In a microservices architecture, service-to-service calls are ubiquitous. Simultaneously, service unreliability can
occur at any moment. Using a circuit breaker allows microservices to continue running when the associated service fails,
preventing cascading failures and giving the failing service time to recover.

Resilience4j has been chosen for this project as Hystrix is currently in maintenance mode.

### Resilience4j

This starter enables you to leverage all the capabilities of Resilience4j. As per
the [user guides](https://resilience4j.readme.io/docs/getting-started), you can utilize this framework not only through
annotations but also programmatically.

In the [sample project](../yorozuya-samples/circuit-breaker-resilience4j), you can find the recommended usage with
Spring Boot.

You can also refer to the [demo](https://github.com/resilience4j/resilience4j-spring-boot3-demo) provided by
the [official documentation](https://resilience4j.readme.io/docs/getting-started-3).

## Helpers

### JsonHelpers

See the interface [JsonHelpers](./src/main/java/com/github/ksewen/yorozuya/starter/helper/json/JsonHelpers.java) and
learn more about it.

The implementation of JsonHelpers, based on Jackson, is by default registered in the Spring Boot context. You can easily
replace it with your own implementation, if you need.
See [JacksonJsonHelpersAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/jackson/JacksonJsonHelpersAutoConfiguration.java)

### RedisHelpers

See the interface [RedisHelpers](./src/main/java/com/github/ksewen/yorozuya/starter/helper/redis/RedisHelpers.java) and
learn more about it.

The implementation of RedisHelpers, based on StringRedisTemplate and JacksonJsonHelpers, is by default registered in the
Spring Boot context. You can easily replace it with your own implementation, if you need.
See [RedisHelpersAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/redis/RedisHelpersAutoConfiguration.java)

## Context

In microservices practice, there is often a need to transmit contextual information across services, for business
logic, for middlewares or for some frameworks. The starter provides
a [Context](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/context/Context.java) implementation
based on ThreadLocal
as [ServiceContext](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/context/impl/ServiceContext.java)
by default. As a service provider, you can automatically inject relevant key-value pairs that are passed in through HTTP
headers into the current context, and you can easily propagate this information to subsequent nodes via Feign or
RestTemplate.

To control such behavior, please use the following configuration:

```yaml
common:
  context:
    service:
      # request -> context
      # Define a prefix for keys manually injected into the context in the code. 
      # Under the default configuration, this set of key-value pairs will be propagated to subsequent nodes via Feign or RestTemplate.
      # If you haven't configured the property, the default value will be "service_context_".
      header-prefix: test_
      # Configure the keys that do not start with "headerPrefix" to be inserted into the current context.
      default-inject-key-set:
        - test

      # context -> request
      # By setting this property to false, keys that start with "headerPrefix" will not be propagated to subsequent nodes via Feign or RestTemplate.
      # The behavior will be determined by the setting of "whiteList" or "blackList."
      transfer-with-prefix: false
      # Configure to operate in either a white list or black list manner.
      # If the value is set to "false," it means that the keys in the "limitSet" will be considered invalid.
      # The default value is "true," which means that the keys in the "limitSet" will be considered valid.
      enable-white-list: false
      # In white-list mode, the configured values will be propagated to subsequent nodes via Feign or RestTemplate.
      # In black-list mode, the configured values will not be propagated to subsequent nodes via Feign or RestTemplate.
      limit-set:
        - content-length
      # This property specifies the behavior when the given key already exists in the HttpHeader of the current request, with the default value being "IGNORE".
      # IGNORE: Do nothing and use the existing value if it already exists.
      # COVER: Replace the existing value with the new value.
      # INSERT: Insert the new value alongside the existing one.
      repetition-strategy: COVER
```

See 
the [sample project](../yorozuya-samples/micrometer-observation/src/main/java/com/github/ksewen/yorozuya/sample/micrometer/observation/controller/ContextController.java)

## Observation and monitor

### Micrometer Observation

Micrometer-observation is integrated in the starter. And this feature will be integrated in the planned module
yorozuya-dashboard in the future.

See [official documentation](https://micrometer.io/docs/observation) to learn more about this component.

### AOP Logging

The AOP implementation for logging request and response information (Debug) as well as error logs (Error) is by default
provided.

To disable this feature by default, use the following property.

```yaml
# set this value as false to disable the AOP logger, com.github.ksewen.yorozuya.starter.aspect.logger.impl.NotRecordByDefaultAroundLogger will be registered
# the default value is true if this property is not set, com.github.ksewen.yorozuya.starter.aspect.logger.impl.RecordByDefaultAroundLogger will be registered
common:
  aspect:
    logger:
      by:
        default:
          enable: false
```

It can also use
annotations [LoggerTrace](./src/main/java/com/github/ksewen/yorozuya/starter/annotation/logger/LoggerTrace.java)
and [LoggerTrace](./src/main/java/com/github/ksewen/yorozuya/starter/annotation/logger/LoggerNotTrace.java) to set up
specific behavior for the AOP Logger.
