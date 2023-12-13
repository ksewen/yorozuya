# YOROZUYA-SPRING-BOOT-STARTER

[English](./README.md)

该 starter 提供了自动配置，将微服务应用无缝加入集群。

## Environment

使用 [
Environment](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/environment/Environment.java)
接口可以获取比如 hostName, hostIp, environment (例如：production, test, develop...) 和 applicationName 等信息。并且可以包括自定义的
metadata。该 Starter 声明了默认的实现 [
BasicEnvironment](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/environment/impl/BasicEnvironment.java)
并且注册到 Spring 上下文。可以使用 @Autoconfiguration 类配置注入并且替换为自定义的实现。

使用示例:

```java 

@Component
@RequiredArgsConstructor
public class Sample {

    private final Environment environment;

    // 获取 hostname
    String hostName = this.environment.getHostName();

    // 获取 host ip
    String hostIp = this.environment.getHostIp();

    // 获取 enviroment
    String activEnvironment = this.environment.getEnvironment();

    // 获取 application name
    String applicationName = this.environment.getApplicationName();

    // 获取 metadata map
    Map<String, String> metadata = this.environment.getMetadata();
}
```

## Data Source

### Spring Data JPA

“spring-boot-data-jpa-starter” 已经是一个开箱即用的组件。需要连接数据源，请添加对应的 driver，例如：

```xml

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

并且参考下述示例配置：

```yaml
spring:
  datasource:
    url: # JDBC URL of the database
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: # username of the database.
    password: # password of the database
```

查看 [JPA 文档](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/) 以了解更多。

在示例项目 [示例项目](../yorozuya-samples/spring-data-jpa) 中，可以找到一个集成了 testcontainers 进行集成测试的简单用例。
**需要安装 Docker**.

### Spring Data Redis

一个典型的示例，使用如下配置连接到 Redis:

```yaml
spring:
  data:
    redis:
      host: # host of redis
      port: # port of redis
      password: # password of redis
      database: # index of database
```

如果想要使用连接池，首先在 pom.xml 中添加 “org.apache.commons:commons-pool2” 依赖：

```xml

<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>

```

并且参考下述示例配置：

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

如果需要 Gzip 压缩支持，请参考如下配置以开启 Gzip 支持：

```yaml
spring:
  data:
    redis:
      template:
        gzip:
          enable: true
```

查看 [Spring Data Redis 文档](https://docs.spring.io/spring-data/redis/docs/current/reference/html/) 以了解更多。

#### RedisHelper

为了简化常用命令的操作，默认提供了基于 “StringRedisTemplate”
的 [RedisCommonHelper](./src/main/java/com/github/ksewen/yorozuya/starter/helper/redis/impl/RedisCommonHelpers.java)
，并已经将其注册到 Spring 上下文。

查看接口定义 [RedisHelpers](./src/main/java/com/github/ksewen/yorozuya/starter/helper/redis/RedisHelpers.java) 以了解更多。

## Rest Clients

人们通常使用 OpenFeign 来调用集群内部的其他服务。当然，也有特殊情况需要调用集群外的接口。使用这个项目，可以轻松解决这个问题。由于两个常用的组件已经被很好得集成，可以很轻松的通过
HTTP 访问其他资源。

<span id="higher_level_clients">

### Higher level Clients

</span>

#### Openfeign

可以使用注解 @EnableFeignClients 以开启 openfeign 支持：

```java

@SpringBootApplication
@EnableFeignClients
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

需要自定义底层客户端，可以参考以下配置：

```shell
# Enable the okhttp3 client for feign
spring.cloud.openfeign.okhttp.enabled=true

# Enable the okhttp3 client for feign
# If spring.cloud.openfeign.okhttp.enabled=true was used, this property is not effective.
spring.cloud.openfeign.httpclient.hc5.enabled=true
```

Spring Cloud Loadbalancer 默认启用，要停用此功能，可以在 pom.xml 中排除 spring-cloud-starter-loadbalancer 依赖或使用以下属性：

```shell
spring.cloud.loadbalancer.enabled=false
```

详情请参考示例项目。
不使用 Loadbalancer:

- [rest-client-okhttp](../yorozuya-samples/rest-client-okhttp)
- [rest-client-httpclient](../yorozuya-samples/rest-client-httpclient)

使用 Loadbalancer:

- [eureka-client](../yorozuya-samples/eureka-client)

#### RestTemplate

RestTemplate 默认启用，要停用这个功能，可以参考以下配置：

```shell
# RestTemplateAutoConfiguration offers two beans, one with load balancer and other without.

# Disable the default restTemplate
common.rest.template.default.enabled=false

# Disable the default restTemplate with Loadbalancer
common.rest.template.loadbalancer.enabled=false
```

默认的底层客户端是 OkHttpClient 3，可以通过编辑 pom.xml 文件排除依赖的方式，将底层客户端替换为 Apach HttpClient 5。

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

可以通过配置参数，来控制底层客户端的行为，查看 [Lower level Clients](#lower_level_clients)。
如果要替换为自定义的客户端，请注入自定义的 Bean。

详情请参考示例项目。
不使用 Loadbalancer:

- [rest-client-okhttp](../yorozuya-samples/rest-client-okhttp)
- [rest-client-httpclient](../yorozuya-samples/rest-client-httpclient)

使用 Loadbalancer:

- [eureka-client](../yorozuya-samples/eureka-client)

<span id="lower_level_clients">

### Lower level Clients

</span>

<span id="okhttp_3">

#### OkHttp 3

</span>

OkHttpClient 3 默认启用，要停用这个功能，可以参考以下配置：

```shell
# the default value is true
common.ok.http.client.enabled=false
```

查看 [OkHttp3ClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/OkHttp3ClientProperties.java)
以了解更多。

#### Apache HttpClient 5

要启用这个功能，可以参考以下配置：

```shell
# the default value is true
common.http.client.hc5.enabled=true
```

**注意:** 启用 OkHttpClient 3 时 HttpClient 5 不会被注册，请在 pom.xml 中排除依赖或使用配置 [use the property](#okhttp_3)
以停用 OkHttpClient 3。

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

查看 [HttpClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/HttpClientProperties.java)
以了解更多。

### Load Balancer

关于负载均衡器在HTTP调用中的使用，请参考相关内容 [Higher level Clients](#higher_level_clients)

### higher-level client 和 lower-level client 的超时配置

首先，Spring Cloud Load Balancer 不提供超时配置，这使其与过去流行的 [ribbon](https://github.com/Netflix/ribbon) 不同。
所以在这个项目中的配置就更加简单了。

#### Openfeign 集成 OkHttp 3 或 Apache HttpClient 5

由于 Openfeign 会覆盖来自低级客户端的配置，因此最好使用 Openfeign 的属性来指定连接超时属性。

一个较好的实践是在属性文件中使用变量，例如：

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

查看 [OkHttpClient](https://github.com/OpenFeign/feign/blob/master/okhttp/src/main/java/feign/okhttp/OkHttpClient.java)
和 [ApacheHttp5Client](https://github.com/OpenFeign/feign/blob/master/hc5/src/main/java/feign/hc5/ApacheHttp5Client.java)

#### RestTemplate 集成 Okhttp 3

可以通过设置 RestTemplate 的属性
[OkHttp3ClientHttpRequestFactory#setConnectTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/OkHttp3ClientHttpRequestFactory.java)
,
[OkHttp3ClientHttpRequestFactory#setReadTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/OkHttp3ClientHttpRequestFactory.java)
和
[OkHttp3ClientHttpRequestFactory#setWriteTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/OkHttp3ClientHttpRequestFactory.java)
来配置。但是从代码中看出，一个新的 client 实例被创建了。

所以直接使用 OkHttpClient 的配置是更好的方法：

```yaml
common:
  ok:
    http:
      client:
        connect-timeout: 3000
        read-timeout: 5000
        write-timeout: 5000
```

查看 [OkHttp3ClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/OkHttp3ClientProperties.java)
以了解所有配置项。

#### RestTemplate 集成 Apache HttpClient 5

通过阅读 [HttpComponentsClientHttpRequestFactory#setConnectTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/HttpComponentsClientHttpRequestFactory.java)
和 [HttpComponentsClientHttpRequestFactory#setReadTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/HttpComponentsClientHttpRequestFactory.java)
的源码，可以了解到这些配置并不会生效。

只需参考以下配置:

查看 [HttpClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/HttpClientProperties.java)
以了解所有配置项。

```yaml
common:
  http:
    client:
      hc5:
        enabled: true
        connect-timeout: 3000
        socket-timeout: 5000
```

**PS**
在本项目中，可以在声明 [PoolingHttpClientConnectionManager](https://github.com/apache/httpcomponents-client/blob/master/httpclient5/src/main/java/org/apache/hc/client5/http/impl/io/PoolingHttpClientConnectionManager.java)
的同时指定 SocketTimeout 。
[PoolingHttpClientConnectionManager](https://github.com/apache/httpcomponents-client/blob/master/httpclient5/src/main/java/org/apache/hc/client5/http/impl/io/PoolingHttpClientConnectionManager.java)
允许您分别通过 [SocketConfig](https://github.com/apache/httpcomponents-core/blob/master/httpcore5/src/main/java/org/apache/hc/core5/http/io/SocketConfig.java)
和 [ConnectionConfig](https://github.com/apache/httpcomponents-client/blob/master/httpclient5/src/main/java/org/apache/hc/client5/http/config/ConnectionConfig.java)
配置 SocketTimeout。
但是，只配置 ConnectionConfig#setSocketTimeout(Timeout) 只会在连接建立的第一次请求生效。请通过配置
SocketConfig#setSoTimeout(Timeout) 来确保复用连接时也同样有效。

该 issue 将在 5.2.2 和 5.3-alpha2 的更新中被修复。
查看 [Issue in Apache's Jira](https://issues.apache.org/jira/browse/HTTPCLIENT-2299?page=com.atlassian.jira.plugin.system.issuetabpanels%3Aall-tabpanel)

## Circuit Breaker

在微服务架构中，服务到服务的调用无处不在。同时，服务不可靠随时可能发生。使用断路器可以让微服务在关联服务发生故障时继续运行，从而防止级联故障并为故障服务提供恢复时间。

由于 Hystrix 目前处于维护模式，因此该项目选择使用 Resilience4j。

### Resilience4j

该 Starter 使您能够使用 Resilience4j 的所有功能。 根据[用户指南](https://resilience4j.readme.io/docs/getting-started)
，您可以通过注释以及编程的方式使用此框架。

查看 [示例项目](../yorozuya-samples/circuit-breaker-resilience4j), 以了解与 Spring Boot 集成的推荐方式。

也可以参考 [官方文档](https://resilience4j.readme.io/docs/getting-started-3)
中提及的 [demo](https://github.com/resilience4j/resilience4j-spring-boot3-demo) 以了解更多。

## Helpers

### JsonHelpers

查看接口定义 [JsonHelpers](./src/main/java/com/github/ksewen/yorozuya/starter/helper/json/JsonHelpers.java) 以了解更多。

JsonHelpers 的默认实现基于 Jackson，并且默认注册到 Spring 上下文。如果需要，可以轻松地将其替换为自定义的实现。

查看 [JacksonJsonHelpersAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/jackson/JacksonJsonHelpersAutoConfiguration.java)

### RedisHelpers

查看 [RedisHelpers](./src/main/java/com/github/ksewen/yorozuya/starter/helper/redis/RedisHelpers.java) 以了解更多。

RedisHelpers 的默认实现基于 StringRedisTemplate 和 JacksonJsonHelpers，并且默认注册到 Spring 上下文。如果需要，可以轻松地将其替换为自定义的实现。

查看 [RedisHelpersAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/redis/RedisHelpersAutoConfiguration.java)

## Context

在微服务实践中，经常需要跨服务传输上下文信息，用于业务逻辑、中间件或某些框架。本项目默认提供了一个基于 ThreadLocal
的 [Context](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/context/Context.java)
实现 [ServiceContext](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/context/impl/ServiceContext.java)
。作为服务提供方，可以自动将通过 HTTP 标头传入的相关键值对注入到当前上下文中，并且可以轻松地将这些信息通过 Feign 或
RestTemplate 传递到后续节点。

可以参考以下配置，以控制此功能的具体行为:

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

查看 [示例项目](../yorozuya-samples/micrometer-observation/src/main/java/com/github/ksewen/yorozuya/sample/micrometer/observation/controller/ContextController.java)

## Observation and monitor

### Micrometer Observation

Micrometer-observation 默认已经集成。 并且该功能未来将会集成到计划的模块 yorozuya-dashboard 中。

查看 [官方文档](https://micrometer.io/docs/observation) 以了解更多。

### AOP Logging

用于记录请求和响应信息（Debug）以及错误日志（Error）的切面日志记录器默认开启。

参考以下配置，以关闭此功能：

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

同样可以使用注解 [LoggerTrace](./src/main/java/com/github/ksewen/yorozuya/starter/annotation/logger/LoggerTrace.java)
和 [LoggerTrace](./src/main/java/com/github/ksewen/yorozuya/starter/annotation/logger/LoggerNotTrace.java) 控制特定的日志记录行为。
