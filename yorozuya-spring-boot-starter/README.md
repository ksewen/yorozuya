# YOROZUYA-SPRING-BOOT-STARTER

This starter offers automated configurations for seamless integration of a microservice into the
group.

## Rest Clients

Frequently, people utilize OpenFeign to make calls to other servers within a group. Certainly, there are exceptional
situations where you need to invoke an interface outside the group. With this project you can easily solve both of these
issues. Because the two common packages have already been integrated, you can easily access resources via HTTP.

### Higher level Clients

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
Apach HttpClient 5.

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

The default clients are configured to set parameters, please see [Lower level Clients](###lower-level-Clients).
To replace with your own client, inject your custom bean please.

For detailed information, please see the projects.  
Without Loadbalancer:

- [rest-client-okhttp](../yorozuya-samples/rest-client-okhttp)
- [rest-client-httpclient](../yorozuya-samples/rest-client-httpclient)

With Loadbalancer:

- [eureka-client](../yorozuya-samples/eureka-client)

### Lower level Clients

#### OkHttp 3

OkHttpClient 3 is enabled by default, to deactivate this, you can make use of following properties:

```shell
# the default value is true, if the value not set, it's true
common.ok.http.client.enabled=false
```

See [OkHttp3ClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/OkHttp3ClientProperties.java)
to learn more about the properties.

#### Apache HttpClient 5

To activate this, you can make use of following properties:

```shell
# the default value is true, if the value not set, it's true
common.http.client.enabled=true
```

**Attention:** HttpClient 5 will definitely not register when OkHttpClient 3 is used, please exclude the
okhttp dependency from pom.xml or [use the property](####apache-httpClient-5) to disable it.

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