# YOROZUYA-SPRING-BOOT-STARTER

[English](./README.md) | [简体中文](./README_CN.md)

Dieser Starter bietet automatisierte Konfigurationen an, um Microservices in die vorhandene Gruppe nahtlose zu
integrieren.

## Environment

Mit der Schnittstelle [
Environment](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/environment/Environment.java) kann man
Informationen der Laufzeitumgebung wie z.B. hostName, hostIp, environment (z.B.: production, test, develop...)
und applicationName. Du kannst auch deine eigenen Metadaten hinfügen.  
Diesem Starter gibt eine standardmäßige
Implementierung [BasicEnvironment](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/environment/impl/BasicEnvironment.java),
die schon in den Spring Kontext registriert wurde.  
Um es durch deine eigene Implementierung zu ersetzen, nur @Autoconfiguration Klasse benutzen und die Bean registrieren.

Verweudung:

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

„spring-boot-data-jpa-starter“ ist bereits eine benutzerfreundliche Komponente. Um eine Verbindung mit einer Datenbank
herzustellen, füge eine Abhängigkeit des Drivers hinzu, zum Beispiel:

```xml

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

und konfiguriere mit den folgenden Attributen:

```yaml
spring:
  datasource:
    url: # JDBC URL of the database
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: # username of the database.
    password: # password of the database
```

Überprüfe [JPA Dokumentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/) und erfahre mehr
darüber.

In diesem [Beispielprojekt](../yorozuya-samples/spring-data-jpa) kann man ein einfaches Anwendungsbeispiel für die
Integration von Testcontainers für Integrationstests finden. **Docker ist erforderlich**.

### Spring Data Redis

In einem typischen Szenario kann man mit den folgenden Attributen konfigurieren, um eine Verbindung mit Redis
herzustellen:

```yaml
spring:
  data:
    redis:
      host: # host of redis
      port: # port of redis
      password: # password of redis
      database: # index of database
```

Fall du einen Verbindungspool verwenden möchtest, füge zuerst die Abhängigkeit „org.apache.commons:commons-pool2“ in die
Datei „pom.xml“ hinzu:

```xml

<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>

```

und mit den folgenden Attributen konfigurieren:

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

Wenn du Gzip-Komprimierung benötigst, konfiguriere bitte mit den folgenden Attributen, um diese Funktion zu aktivieren:

```yaml
spring:
  data:
    redis:
      template:
        gzip:
          enable: true
```

Überprüfe [Spring Data Redis Dokumentation](https://docs.spring.io/spring-data/redis/docs/current/reference/html/) und
erfahre mehr darüber.

#### RedisHelper

Um die Operationen für gemeinsame Ausweisung zu vereinfachen, dieser Starter
gibt [RedisCommonHelper](./src/main/java/com/github/ksewen/yorozuya/starter/helper/redis/impl/RedisCommonHelpers.java)
standardmäßig auf „StringRedisTemplate“ basiert, das standardmäßig automatisch in den Spring Kontext registriert wurde.

Überprüfe die
Schnittstelle [RedisHelpers](./src/main/java/com/github/ksewen/yorozuya/starter/helper/redis/RedisHelpers.java) und
erfahre mehr darüber.

## Rest Clients

Meistens benutzt man OpenFeign, um andere Service innerhalb einer Gruppe anzufragen. Aber es gibt natürlich Ausnahme,
dass man Schnittstellen außerhalb einer Gruppe anfragen muss. Mit diesem Projekt kann man die Beiden ganz einfach lösen.
Man kann problemlos über HTTP auf Ressourcen zugreifen, weil zwei gemeinsame Packung bereits integriert wurden.

<span id="higher_level_clients">

### Higher-Level-Clients

</span>

#### Openfeign

Mit der Annotation @EnableFeignClients kann man „OpenFeign-Client“ zu aktivieren:

```java

@SpringBootApplication
@EnableFeignClients
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

Um den „Lower-Level-Client“ anzupassen, kannst du mit den folgenden Attributen verwenden:

```shell
# Enable the okhttp3 client for feign
spring.cloud.openfeign.okhttp.enabled=true

# Enable the okhttp3 client for feign
# If spring.cloud.openfeign.okhttp.enabled=true was used, this property is not effective.
spring.cloud.openfeign.httpclient.hc5.enabled=true
```

„Spring Cloud Loadbalancer“ ist standardmäßig aktiviert. Um das zu deaktivieren, schließe bitte die Abhängigkeit
„spring-cloud-starter-loadbalancer“ aus der Datei „pom.xml“ aus oder konfiguriere mit den folgenden Attributen:

```shell
spring.cloud.loadbalancer.enabled=false
```

Um mehr darüber zu erfahren, überprüfe bite die Projekte:
Ohne Loadbalancer:

- [rest-client-okhttp](../yorozuya-samples/rest-client-okhttp)
- [rest-client-httpclient](../yorozuya-samples/rest-client-httpclient)

Mit Loadbalancer:

- [eureka-client](../yorozuya-samples/eureka-client)

#### RestTemplate

Standardmäßig ist RestTemplate aktiviert. um das zu deaktivieren, konfiguriere bitte mit den folgenden Attributen:

```shell
# RestTemplateAutoConfiguration offers two beans, one with load balancer and other without.

# Disable the default restTemplate
common.rest.template.default.enabled=false

# Disable the default restTemplate with Loadbalancer
common.rest.template.loadbalancer.enabled=false
```

OkHttpClient 3 wird standardmäßig als „Low-Level-Client“ von RestTemplate aktiviert. Du kannst die Datei „pom.xml“
überarbeiten, um das auszuschließen und durch Apache HttpClient 5 zu ersetzen.

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

Die standardmäßigen Clients können mit den Attributen konfiguriert werden, überprüfe
bitte [Lower-Level-Clients](#lower_level_clients).
Um durch deinen eigenen Client zu ersetzen, registriere bitte die Bean in Spring Kontext.

Um mehr darüber zu erfahren, überprüfe bite die Projekte:
Ohne Loadbalancer:

- [rest-client-okhttp](../yorozuya-samples/rest-client-okhttp)
- [rest-client-httpclient](../yorozuya-samples/rest-client-httpclient)

Mit Loadbalancer:

- [eureka-client](../yorozuya-samples/eureka-client)

<span id="lower_level_clients">

### Lower-Level-Clients

</span>

<span id="okhttp_3">

#### OkHttp 3

</span>

OkHttpClient 3 ist standardmäßig aktiviert. Um das zu deaktivieren, konfiguriere bitte mit den folgenden Attributen:

```shell
# the default value is true
common.ok.http.client.enabled=false
```

Überprüfe [OkHttp3ClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/OkHttp3ClientProperties.java)
und erfahre mehr über die Attribute.

#### Apache HttpClient 5

Um das zu aktivieren, konfiguriere bitte mit den folgenden Attributen:

```shell
# the default value is true
common.http.client.hc5.enabled=true
```

**Aufmerksamkeit:** HttpClient 5 will nicht registriert werden, falls OkHttpClient 3 ist aktiviert. Schließe bitte die
Abhängigkeit „okhttp“ aus der Datei „pom.xml“ aus oder [mit den attributen](#okhttp_3), um es zu deaktivieren.

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

Überprüfe [HttpClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/HttpClientProperties.java)
und erfahre mehr über die Attribute.

### Load Balancer

Die Informationen über die Verwendung vom „Load Balancer“ während vom Anfragen über HTTP findest du im entsprechenden
Inhalt [Higher-Level-Clients](#higher_level_clients).

### Beziehungen der Konfiguration für Zeitüberschreitung zwischen Higher-Level- und Lower-Level-Client

Tatsächlich bietet Spring Cloud Load Balancer keine Konfiguration für Zeitüberschreitung an. Es ist ganz anders
als [ribbon](https://github.com/Netflix/ribbon), der vorher eine beliebte Auswahl war. Deshalb besteht in diesem Projekt
einfachere Beziehungen dazu.

#### Openfeign mit OkHttp 3 / Apache HttpClient 5

Weil die Konfigurationen von Openfeign von dem „Low-Level-Client“ überladen werden, ist es besser, mit den Attributen
von Openfeign zu konfigurieren, um die Zeitüberschreitung der Verbindung festzulegen.

Es wird empfohlen, Variablen in der Properties- oder YAML-Datei zu benutzen, zum Beispiel:

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

Überprüfe [OkHttpClient](https://github.com/OpenFeign/feign/blob/master/okhttp/src/main/java/feign/okhttp/OkHttpClient.java)
und [ApacheHttp5Client](https://github.com/OpenFeign/feign/blob/master/hc5/src/main/java/feign/hc5/ApacheHttp5Client.java)

#### RestTemplate mit Okhttp 3

Um RestTemplate zu konfigurieren, du kannst
mit [OkHttp3ClientHttpRequestFactory#setConnectTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/OkHttp3ClientHttpRequestFactory.java)
,
[OkHttp3ClientHttpRequestFactory#setReadTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/OkHttp3ClientHttpRequestFactory.java)
und
[OkHttp3ClientHttpRequestFactory#setWriteTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/OkHttp3ClientHttpRequestFactory.java).
Aber aus dem Code kann man hervorgehen, dass eine neue Instanz des Clients erzeugt wird.

Es ist also die bessere Möglichkeit, die Konfigurationen für OkHttpClient direkt zu verwenden:

```yaml
common:
  ok:
    http:
      client:
        connect-timeout: 3000
        read-timeout: 5000
        write-timeout: 5000
```

Um über alle Attribute zu erfahren, überprüfe
bitte [OkHttp3ClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/OkHttp3ClientProperties.java).

#### RestTemplate mit Apache HttpClient 5

Durch das Lesen der Kommentierung
in [HttpComponentsClientHttpRequestFactory#setConnectTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/HttpComponentsClientHttpRequestFactory.java)
und [HttpComponentsClientHttpRequestFactory#setReadTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/HttpComponentsClientHttpRequestFactory.java)
kann man darüber erfahren, dass solche Konfigurationen nicht wirksam werden.

Nur konfiguriere bitte mit den folgenden Attributen:

Um über alle Attribute zu erfahren, überprüfe
bitte [HttpClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/HttpClientProperties.java)

```yaml
common:
  http:
    client:
      hc5:
        enabled: true
        connect-timeout: 3000
        socket-timeout: 5000
```

**PS** In diesem Projekt, du kannst während der Erzeugung
von [PoolingHttpClientConnectionManager](https://github.com/apache/httpcomponents-client/blob/master/httpclient5/src/main/java/org/apache/hc/client5/http/impl/io/PoolingHttpClientConnectionManager.java)
die Konfiguration SocketTimeout angeben.

Die
Klasse [PoolingHttpClientConnectionManager](https://github.com/apache/httpcomponents-client/blob/master/httpclient5/src/main/java/org/apache/hc/client5/http/impl/io/PoolingHttpClientConnectionManager.java)
ermöglicht es,
durch [SocketConfig](https://github.com/apache/httpcomponents-core/blob/master/httpcore5/src/main/java/org/apache/hc/core5/http/io/SocketConfig.java)
und [ConnectionConfig](https://github.com/apache/httpcomponents-client/blob/master/httpclient5/src/main/java/org/apache/hc/client5/http/config/ConnectionConfig.java)
SocketTimeout festzulegen. Wenn nur ConnectionConfig#setSocketTimeout(Timeout) konfiguriert wurde, wurde es nicht
wirksam. Erreiche bitte dies durch die Konfiguration von SocketConfig#setSoTimeout(Timeout).

Dieses Problem wird in den Versionen 5.2.2 und 5.3-alpha2 behoben.
Siehe
bitte [Issue in Apache's Jira](https://issues.apache.org/jira/browse/HTTPCLIENT-2299?page=com.atlassian.jira.plugin.system.issuetabpanels%3Aall-tabpanel)
an.

## Circuit Breaker

In einer Architektur des Microservices gibt es überall Aufrufe zwischen Service und Service. Gleichzeitig kann es
jederzeit zu einer Unzuverlässigkeit des Dienstes kommen. Mit Circuit Breaker dürfen Microservices weiterhin laufen,
während des assoziativen Service ausfällt. Gleichzeitig werden kaskadierende Ausfälle verhindert und Fehlerdiensten wird
Zeit zur Wiederherstellung gegeben.

Resilience4j wurde ausgewählt für dieses Projekt, da sich Hystrix derzeit im Wartungsmodus befindet.

### Resilience4j

Mit diesem Starter können Sie alle Funktionen von Resilience4j nutzen. Nach
dem [Benutzerhandbuch](https://resilience4j.readme.io/docs/getting-started) kann man alle dessen Funktionen durch nicht
nur Annotationen, sondern auch Programmierung benutzen.

Im [Beispielprojekt](../yorozuya-samples/circuit-breaker-resilience4j) findest du die empfehlenden Anwendungsfälle für
die Integration mit Spring Boot.

Du kannst auch die [Demo](https://github.com/resilience4j/resilience4j-spring-boot3-demo), die
von [Offizielle Dokumentation](https://resilience4j.readme.io/docs/getting-started-3) angeboten wird, überprüfen.

## Helpers

### JsonHelpers

Überprüfe bitte die
Schnittstelle [JsonHelpers](./src/main/java/com/github/ksewen/yorozuya/starter/helper/json/JsonHelpers.java) und erfahre
mehr darüber.

Die Implementierung von JsonHelpers basiert auf Jackson wird standardmäßig in den Spring Kontext registriert. Man kann
sie ganz einfach durch eine eigene Implementierung ersetzen. Überprüfe
bitte [JacksonJsonHelpersAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/jackson/JacksonJsonHelpersAutoConfiguration.java),
falls es notwendig ist.

### RedisHelpers

Überprüfe bitte die
Schnittstelle [RedisHelpers](./src/main/java/com/github/ksewen/yorozuya/starter/helper/redis/RedisHelpers.java) und
erfahre
mehr darüber.

Die Implementierung von RedisHelpers basiert auf StringRedisTemplate und JacksonJsonHelpers wird standardmäßig in den
Spring Kontext registriert. sie ganz einfach durch eine eigene Implementierung ersetzen. Überprüfe
bitte [RedisHelpersAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/redis/RedisHelpersAutoConfiguration.java),
falls es notwendig ist.

## Context

In der Praxis des Microservices hat man oft Kontextinformationen zwischen Services weiterzureichen, damit andere Service
sie für Geschäftslogik oder andere Middleware und Frameworks sie verwenden kann. Dieser Starter bietet eine
standardmäßige Implementierung
von [Kontext](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/context/Context.java) basiert auf
ThreadLocal an, das
heißt [ServiceContext](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/context/impl/ServiceContext.java).
Als Dienstanbieter kann man relevante Schlüssel-Wert-Paare, die über HTTP-Header übergeben werden, automatisch in den
aktuellen Kontext einfügen und diese Informationen problemlos über Feign oder RestTemplate an nachfolgende Service
weiterreichen.

Um das Verhalten der Funktion zu kontrollieren, konfiguriere bitte mit den folgenden Attributen:

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

Überprüfe
bitte [Beispielprojekt](../yorozuya-samples/micrometer-observation/src/main/java/com/github/ksewen/yorozuya/sample/micrometer/observation/controller/ContextController.java)

## Beobachtung and Überwachung

### Micrometer Observation

Micrometer-observation ist in dem Starter bereit integriert. Und dieses Feature will in Zukunft auch mit dem planenden
Modul yorozuya-dashboard integrieren.

Überprüfe
bitte [Offizielle Dokumentation](https://micrometer.io/docs/observation) und erfahre mehr darüber.

### AOP Protokollierung

Standardmäßig wird Protokollierung der Informationen von Request und Response (Debug) so wie Fehlerprotokollen (Error)
bereitgestellt.

Um dieses Feature zu deaktivieren, konfiguriere bitte mit den folgenden Attributen.

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

Es gibt auch eine andere Möglichkeit, mit
Annotationen [LoggerTrace](./src/main/java/com/github/ksewen/yorozuya/starter/annotation/logger/LoggerTrace.java)
und [LoggerTrace](./src/main/java/com/github/ksewen/yorozuya/starter/annotation/logger/LoggerNotTrace.java), um das
spezifische Verhalten von AOP Logger festzulegen.
