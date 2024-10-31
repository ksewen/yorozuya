# YOROZUYA-SPRING-BOOT-STARTER

[Deutsch](./README_DE.md) | [简体中文](./README_CN.md)

A seamless integration of microservices into the existing group can be with the help of this starter automatically
configured.

## Environment

Using the [Environment](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/environment/Environment.java)
interface, runtime environment information such as hostName, hostIp, environment (e.g., production, test, development),
and applicationName can be read. Custom metadata can be added to the map "metadata".

The default
implementation, [BasicEnvironment](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/environment/impl/BasicEnvironment.java),
has already been registered in the Spring context. With the help of the @Autoconfiguration class and registration, it
can be replaced by a custom implementation.

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

As a user-friendly component, ‘spring-boot-data-jpa-starter’ is generally considered. The connection to the database can
be established through the driver component. For example, the following configuration can be used:

```xml

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

in addition, the following attributes must be added:

```yaml
spring:
  datasource:
    url: # JDBC URL of the database
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: # username of the database.
    password: # password of the database
```

See [JPA documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/) and learn more about it.

In the [sample project](../yorozuya-samples/spring-data-jpa), a simple use case for the integration of Testcontainers
for integration tests can be found. **Docker is required**.

### Spring Data Redis

In a typical scenario, the connection to Redis can be established using the following attributes:

```yaml
spring:
  data:
    redis:
      host: # host of redis
      port: # port of redis
      password: # password of redis
      database: # index of database
```

With the component "org.apache.commons:commons-pool2", the connection pool can be provided as needed:

```xml

<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>

```

Additionally, the following attributes are essential:

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

The Gzip compression can be activated as needed using the following attributes:

```yaml
spring:
  data:
    redis:
      template:
        gzip:
          enable: true
```

Check the [Spring Data Redis documentation](https://docs.spring.io/spring-data/redis/docs/current/reference/html/) and
learn
more about it.

#### RedisHelper

The
class [RedisCommonHelper](./src/main/java/com/github/ksewen/yorozuya/starter/helper/redis/impl/RedisCommonHelpers.java),
based on "StringRedisTemplate", has been registered as the default implementation in the Spring context. This simplifies
common operations.

Check the [RedisHelpers](./src/main/java/com/github/ksewen/yorozuya/starter/helper/redis/RedisHelpers.java) interface to
learn more about it.

## Rest Clients

OpenFeign is typically used to request services, namely HTTP interfaces, especially within the group. On the other hand,
services outside the group are inevitable. This project simplifies both operations thanks to the integration of two
common libraries and enables seamless access to resources via HTTP

<span id="higher_level_clients">

### Higher level Clients

</span>

#### Openfeign

The OpenFeign Client can be activated using the annotation @EnableFeignClients:

```java

@SpringBootApplication
@EnableFeignClients
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

The lower level client can be configured using the following attributes:

```shell
# Enable the apache httpclient 5 for feign
# If spring.cloud.openfeign.okhttp.enabled=true was used, this property is not effective.
spring.cloud.openfeign.httpclient.hc5.enabled=true
```

The Spring Cloud Loadbalancer is enabled by default. It can be disabled by removing the component
"spring-cloud-starter-loadbalancer" from the "pom.xml" file or by using the following attributes:

```shell
spring.cloud.loadbalancer.enabled=false
```

To learn more about it, please check the projects:

Without Loadbalancer:

- [rest-client](../yorozuya-samples/rest-client)

With Loadbalancer:

- [eureka-client](../yorozuya-samples/eureka-client)

#### RestTemplate

RestTemplate is enabled by default. It can be disabled by configuring the following attributes:

```shell
# RestTemplateAutoConfiguration offers two beans, one with load balancer and other without.

# Disable the default restTemplate
common.rest.template.default.enabled=false

# Disable the default restTemplate with Loadbalancer
common.rest.template.loadbalancer.enabled=false
```

Apache HttpClient 5 has been registered by default as the "Low-Level Client" of RestTemplate in the Spring context.

The default clients can be configured with the attributes, please check the [Lower level Clients](#lower_level_clients).
To replace them with your own client, please register the bean in the Spring context.

To learn more about it, please check the projects:

Without Loadbalancer:

- [rest-client](../yorozuya-samples/rest-client)

With Loadbalancer:

- [eureka-client](../yorozuya-samples/eureka-client)

#### RestClient

Spring Framework 6.1 M2 introduces the RestClient, a new synchronous HTTP client. The RestClient offers a modern, fluent
API. It offers an abstraction over HTTP libraries that allows for convenient conversion from Java object to HTTP
request, and creation of objects from the HTTP response.

The new Client is not enabled by default. It can be activated using the following attributes:

```shell
# RestClientAutoConfiguration offers two beans, one with load balancer and other without.

# Enable the default restClient
common.rest.client.default.enabled=true

# Enable the default restClient with Loadbalancer
common.rest.client.loadbalancer.enabled=true
```

Apache HttpClient 5 is enabled for RestClient by default.

The default clients can be configured with the properties, please check the [Lower level Clients](#lower_level_clients).
To replace them with your own client, please register the bean in the Spring context.

To learn more about it, please check the projects:

Without Loadbalancer:

- [rest-client](../yorozuya-samples/rest-client)

With Loadbalancer:

- [eureka-client](../yorozuya-samples/eureka-client)

Check the [Documentation of RestClient](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html)
to learn more about this new Client.

<span id="lower_level_clients">

### Lower level Clients

</span>

#### Apache HttpClient 5

It can be disabled by configuring the following attributes:

```shell
# the default value is true
common.http.client.hc5.enabled=false
```

Check
the [HttpClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/HttpClientProperties.java)
to learn more about the properties.

### Load Balancer

Information about the use of the Load Balancer during HTTP requests can be found in the relevant content
on [Higher level Clients](#higher_level_clients).

### The relationships of timeout configuration between higher-level client and lower-level client

Spring Cloud Load Balancer does not provide configuration for timeouts. It is quite different
from [ribbon](https://github.com/Netflix/ribbon), which was previously a popular choice. Therefore, this project
establishes simpler relationships between them.

#### Openfeign with Apache HttpClient 5

Because Openfeign overrides configurations from the low-level client, it's preferable to use properties for Openfeign
to specifying connection timeout properties.

The better way is to use variables in the properties file, for example:

```yaml
common:
  http:
    client:
      hc5:
        enabled: true
        connect-timeout: 3000
        socket-timeout: 5000

spring:
  cloud:
    openfeign:
      client:
        config:
          default:
            connectTimeout: ${common.http.client.hc5.connect-timeout}
            read-timeout: ${common.http.client.hc5.socket-timeout}
```

Check
the [ApacheHttp5Client](https://github.com/OpenFeign/feign/blob/master/hc5/src/main/java/feign/hc5/ApacheHttp5Client.java)

#### RestTemplate with Apache HttpClient 5

By reading the comments
in [HttpComponentsClientHttpRequestFactory#setConnectTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/HttpComponentsClientHttpRequestFactory.java)
and
[HttpComponentsClientHttpRequestFactory#setReadTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/HttpComponentsClientHttpRequestFactory.java)
, it can be understood that these configurations will not take effect.

The effective configuration can be made using the following properties:

To learn more about all properties, please check
the [HttpClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/HttpClientProperties.java).

```yaml
common:
  http:
    client:
      hc5:
        enabled: true
        connect-timeout: 3000
        socket-timeout: 5000
```

## Circuit Breaker

In a microservices architecture, service-to-service calls are ubiquitous. Simultaneously, service unreliability can
occur at any moment. Using a circuit breaker allows microservices to continue running when the associated service fails,
preventing cascading failures and giving the failing service time to recover.

Hystrix was not selected due to being in maintenance mode.

### Resilience4j

This starter enables you to leverage all the capabilities of Resilience4j. As per
the [user guides](https://resilience4j.readme.io/docs/getting-started), you can utilize this framework not only through
annotations but also programmatically.

In the [sample project](../yorozuya-samples/circuit-breaker-resilience4j), the recommended usage with Spring Boot can be
founded.

The [demo](https://github.com/resilience4j/resilience4j-spring-boot3-demo) provided by
the [official documentation](https://resilience4j.readme.io/docs/getting-started-3) is available.

## Helpers

### JsonHelpers

Check the interface [JsonHelpers](./src/main/java/com/github/ksewen/yorozuya/starter/helper/json/JsonHelpers.java) and
learn more about it.

The implementation of JsonHelpers, based on Jackson, is by default registered in the Spring Boot context. You can easily
replace it with your own implementation as needed.

Check
the [JacksonJsonHelpersAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/jackson/JacksonJsonHelpersAutoConfiguration.java)

### RedisHelpers

See the interface [RedisHelpers](./src/main/java/com/github/ksewen/yorozuya/starter/helper/redis/RedisHelpers.java) and
learn more about it.

The implementation of RedisHelpers, based on StringRedisTemplate and JacksonJsonHelpers, is by default registered in the
Spring Boot context. You can easily replace it with your own implementation as needed.

Check
the [RedisHelpersAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/redis/RedisHelpersAutoConfiguration.java)

## Context

In microservices practice, there is often a need to transmit contextual information across services, for business
logic, for middlewares or for some frameworks. The starter provides
a [Context](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/context/Context.java) implementation
based on ThreadLocal
as [ServiceContext](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/context/impl/ServiceContext.java)
by default. As a service provider, you can automatically inject relevant key-value pairs that are passed in through HTTP
headers into the current context, and you can easily propagate this information to subsequent nodes via Feign or
RestTemplate.

The behavior of the function can be managed using the following attributes:

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

Check
the [sample project](../yorozuya-samples/micrometer-observation/src/main/java/com/github/ksewen/yorozuya/sample/micrometer/observation/controller/ContextController.java)

## Observation and monitor

### Micrometer Observation

Micrometer-observation is integrated in the starter. And this feature will be integrated in the planned module
yorozuya-dashboard in the future.

Check the [official documentation](https://micrometer.io/docs/observation) to learn more about this component.

### AOP Logging

The AOP implementation for logging request and response information (Debug) as well as error logs (Error) is by default
provided.

This feature can be disabled using the following attributes:

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

Using the
annotations [LoggerTrace](./src/main/java/com/github/ksewen/yorozuya/starter/annotation/logger/LoggerTrace.java)
and [LoggerTrace](./src/main/java/com/github/ksewen/yorozuya/starter/annotation/logger/LoggerNotTrace.java) to define
the specific behavior of the AOP Logger could be considered as an alternative.
