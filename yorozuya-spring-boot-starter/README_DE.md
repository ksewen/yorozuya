# YOROZUYA-SPRING-BOOT-STARTER

[English](./README.md) | [简体中文](./README_CN.md)

Mithilfe dieses Starters kann eine nahtlose Integration von Microservices in die vorhandene Gruppe automatisch
konfiguriert werden.

## Environment

Mithilfe der Schnittstelle [
Environment](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/environment/Environment.java) können
Informationen der Laufzeitumgebung wie z.B. hostName, hostIp, environment (z.B.: production, test, develop...)
und applicationName ausgelesen werden. Die eigenen Metadaten können in die als „metadata“ genannte Map hingefügt werden.

Die standardmäßige
Implementierung [BasicEnvironment](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/environment/impl/BasicEnvironment.java)
wurde bereits in den Spring Kontext registriert.
Mithilfe der @Autoconfiguration Klasse und der Registrierung lässt sie sich durch eine eigene Implementierung ersetzen.

Verwendung:

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

Als eine benutzerfreundliche Komponente wird „spring-boot-data-jpa-starter“ in der Regel betrachtet.
Die Verbindung mit Datenbank lässt sich durch die Komponente des Drivers herstellen. Beispielsweise kann die folgende
Konfiguration benutzt werden:

```xml

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

Zudem müssen die folgenden Attribute hinzugefügt werden:

```yaml
spring:
  datasource:
    url: # JDBC URL of the database
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: # username of the database.
    password: # password of the database
```

Überprüfe die [JPA Dokumentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/), um mehr darüber
zu erfahren.

In diesem [Beispielprojekt](../yorozuya-samples/spring-data-jpa) lässt sich ein einfaches Anwendungsbeispiel für die
Integration von Testcontainers für Integrationstests finden. **Docker ist erforderlich**.

### Spring Data Redis

In einem typischen Szenario lässt sich die Verbindung mit Redis mithilfe der folgenden Attribute herstellen:

```yaml
spring:
  data:
    redis:
      host: # host of redis
      port: # port of redis
      password: # password of redis
      database: # index of database
```

Mit der Komponente „org.apache.commons:commons-pool2“ kann der Verbindungspool bei Bedarf bereitgestellt werden:

```xml

<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>

```

Zudem sind die folgenden Attribute wesentlich:

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

Anhand der folgenden Attribute kann die Gzip-Komprimierung bei Bedarf aktiviert werden:

```yaml
spring:
  data:
    redis:
      template:
        gzip:
          enable: true
```

Überprüfe die [Spring Data Redis Dokumentation](https://docs.spring.io/spring-data/redis/docs/current/reference/html/),
um mehr darüber zu erfahren.

#### RedisHelper

Die auf „StringRedisTemplate“ basierte
Klasse [RedisCommonHelper](./src/main/java/com/github/ksewen/yorozuya/starter/helper/redis/impl/RedisCommonHelpers.java)
wurde als die standardmäßige Implementierung in den Spring Kontext registriert. Dadurch lassen sich die allgemeinen
Operationen vereinfachen.

Überprüfe die
Schnittstelle [RedisHelpers](./src/main/java/com/github/ksewen/yorozuya/starter/helper/redis/RedisHelpers.java), um mehr
darüber zu erfahren.

## Rest Clients

OpenFeign dient in der Regel dazu, Services nämlich HTTP-Schnittstellen, insbesondere innerhalb der Gruppe anzufragen.
Andererseits sind die Services außerhalb der Gruppe unausweichlich. Mithilfe dieses Projekts lassen sich beide
Operationen dank der Integration zweier gemeinsamer Bibliotheken vereinfachen und ermöglichen einen problemlosen Zugriff
auf Ressourcen über HTTP.

<span id="higher_level_clients">

### Higher-Level-Clients

</span>

#### Openfeign

Anhand der Annotation @EnableFeignClients lässt sich „OpenFeign-Client“ aktivieren:

```java

@SpringBootApplication
@EnableFeignClients
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

Der „Lower-Level-Client“ kann durch die folgenden Attribute konfiguriert werden:

```shell
# Enable the apache httpclient 5 for feign
# If spring.cloud.openfeign.okhttp.enabled=true was used, this property is not effective.
spring.cloud.openfeign.httpclient.hc5.enabled=true
```

Standardmäßig ist „spring Cloud Loadbalancer“ aktiviert. Mit dem Entfernen der Komponente
„spring-cloud-starter-loadbalancer“ aus der Datei „pom.xml“ oder der folgenden Attribute kann es deaktiviert werden:

```shell
spring.cloud.loadbalancer.enabled=false
```

Um mehr darüber zu erfahren, überprüfe bitte die Projekte:

Ohne Loadbalancer:

- [rest-client](../yorozuya-samples/rest-client)

Mit Loadbalancer:

- [eureka-client](../yorozuya-samples/eureka-client)

#### RestTemplate

RestTemplate ist standardmäßig aktiviert. Anhand der Konfiguration von folgenden Attributen kann es deaktiviert werden:

```shell
# RestTemplateAutoConfiguration offers two beans, one with load balancer and other without.

# Disable the default restTemplate
common.rest.template.default.enabled=false

# Disable the default restTemplate with Loadbalancer
common.rest.template.loadbalancer.enabled=false
```

Apache HttpClient 5 wurde standardmäßig als „Low-Level-Client“ von RestTemplate in den Spring Kontext registriert.

Die standardmäßigen Clients können mit den Attributen konfiguriert werden, überprüfe
bitte [Lower-Level-Clients](#lower_level_clients).
Um durch deinen eigenen Client zu ersetzen, registriere bitte die Bean in Spring Kontext.

Um mehr darüber zu erfahren, überprüfe bite die Projekte:

Ohne Loadbalancer:

- [rest-client](../yorozuya-samples/rest-client)

Mit Loadbalancer:

- [eureka-client](../yorozuya-samples/eureka-client)

#### RestClient

Spring Framework 6.1 M2 führt den RestClient ein, der ein neuer synchroner HTTP-Client ist. Der RestClient bietet eine
moderne, fließende API. Er bietet eine Abstraktion über HTTP-Bibliotheken, die eine bequeme Umwandlung von Java-Objekten
in HTTP-Anfragen und die Erstellung von Objekten aus der HTTP-Antwort ermöglicht.

Der neue Client wird nicht standardmäßig aktiviert. Mit den folgenden Attributen kann es aktiviert werden: 

```shell
# RestClientAutoConfiguration offers two beans, one with load balancer and other without.

# Enable the default restClient
common.rest.client.default.enabled=true

# Enable the default restClient with Loadbalancer
common.rest.client.loadbalancer.enabled=true
```

Apache HttpClient 5 wird standardmäßig als „Low-Level-Client“ von RestClient aktiviert.

Die standardmäßigen Clients können mit den Attributen konfiguriert werden, überprüfe
bitte [Lower-Level-Clients](#lower_level_clients).
Durch die Registrierung von der Bean der eigenen Implementierung in den Spring Kontext lässt sich der Client ersetzen.

Um mehr darüber zu erfahren, überprüfe bitte die Projekte:

Ohne Loadbalancer:

- [rest-client](../yorozuya-samples/rest-client)

Mit Loadbalancer:

- [eureka-client](../yorozuya-samples/eureka-client)

Überprüfe
die [Dokumentation von RestClient](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html), um
mehr über den neuen Client zu erfahren.

<span id="lower_level_clients">

### Lower-Level-Clients

</span>

#### Apache HttpClient 5

Mithilfe der folgenden Attribute kann es deaktiviert werden:

```shell
# the default value is true
common.http.client.hc5.enabled=false
```

Überprüfe [HttpClientProperties](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/http/client/HttpClientProperties.java),
um mehr über die Attribute zu erfahren.

### Load Balancer

Die Informationen über die Verwendung vom „Load Balancer“ während der Anfragen über HTTP lassen sich im entsprechenden
Abschnitt [Higher-Level-Clients](#higher_level_clients) finden.

### Beziehungen der Konfiguration für Zeitüberschreitung zwischen Higher-Level- und Lower-Level-Client

Tatsächlich bietet Spring Cloud Load Balancer keine Konfiguration für Zeitüberschreitung an. Es ist ganz anders
als [ribbon](https://github.com/Netflix/ribbon), der vorher eine beliebte Auswahl war. Deshalb besteht in diesem Projekt
einfachere Beziehungen dazwischen.

#### Openfeign mit Apache HttpClient 5

Aufgrund der Überladung der Konfigurationen des „Low-Level-Client“ durch OpenFeign ist es empfehlenswert, die
Konfiguration der Zeitüberschreitung über folgende entsprechende Attribute von OpenFeign vorzunehmen:

Variablen in der Properties- oder YAML-Datei zu benutzen, wird empfohlen. Zum Beispiel:

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

Überprüfe [ApacheHttp5Client](https://github.com/OpenFeign/feign/blob/master/hc5/src/main/java/feign/hc5/ApacheHttp5Client.java)

#### RestTemplate mit Apache HttpClient 5

Dass solche Konfigurationen nicht wirksam werden, ist dank der Quellcode
in [HttpComponentsClientHttpRequestFactory#setConnectTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/HttpComponentsClientHttpRequestFactory.java)
und [HttpComponentsClientHttpRequestFactory#setReadTimeout(int)](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-web/src/main/java/org/springframework/http/client/HttpComponentsClientHttpRequestFactory.java)
deutlich.

Die wirksame Konfiguration kann durch die folgenden Attribute vorgenommen werden:

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

## Circuit Breaker

In einer Microservices-Architektur sind Aufrufe zwischen verschiedenen Services wesentlich, wodurch die
Unzuverlässigkeit der Services unausweichlich wird. Mithilfe Circuit Breaker können Microservices jedoch auch bei
Serviceausfällen innerhalb der Gruppe weiterhin funktionieren. Zusätzlich lassen sich so kaskadierende Ausfälle
vermeiden und die benötigte Zeit zur Wiederherstellung gewährleisten.

Hystrix wurde aufgrund des Wartungsmodus nicht ausgewählt.

### Resilience4j

Mit diesem Starter können alle Funktionen von Resilience4j verwendet werden. Nach
dem [Benutzerhandbuch](https://resilience4j.readme.io/docs/getting-started) lassen sich alle dessen Funktionen durch
sowohl Annotationen als auch Programmierung benutzen.

Im [Beispielprojekt](../yorozuya-samples/circuit-breaker-resilience4j) können die empfehlenden Anwendungsfälle für die
Integration mit Spring Boot gefunden werden.

Die [Demo](https://github.com/resilience4j/resilience4j-spring-boot3-demo), die
von der [offiziellen Dokumentation](https://resilience4j.readme.io/docs/getting-started-3) angeboten wird, ist
verfügbar.

## Helpers

### JsonHelpers

Überprüfe bitte die
Schnittstelle [JsonHelpers](./src/main/java/com/github/ksewen/yorozuya/starter/helper/json/JsonHelpers.java), um mehr
darüber zu erfahren.

Die auf Jackson basierte Implementierung von JsonHelpers wird standardmäßig in den Spring Kontext registriert. Durch die
eigene Implementierung kann das Ersetzen ganz einfach ermöglicht werden. Überprüfe
bitte [JacksonJsonHelpersAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/jackson/JacksonJsonHelpersAutoConfiguration.java)
bei Bedarf.

### RedisHelpers

Überprüfe bitte die
Schnittstelle [RedisHelpers](./src/main/java/com/github/ksewen/yorozuya/starter/helper/redis/RedisHelpers.java), um mehr
darüber zu erfahren.

Die auf StringRedisTemplate basierte Implementierung von RedisHelpers und JacksonJsonHelpers wird standardmäßig in den
Spring Kontext registriert. Durch die eigene Implementierung kann das Ersetzen ganz einfach ermöglicht werden. Überprüfe
bitte [RedisHelpersAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/starter/configuration/redis/RedisHelpersAutoConfiguration.java)
bei Bedarf.

## Context

Kontextinformationen zwischen Services weiterzureichen ist in der Praxis des Microservices unausweichlich, damit andere
Service solche Informationen für Geschäftslogik oder andere Middleware und Frameworks bearbeiten kann. Eine
standardmäßige Implementierung
von [Kontext](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/context/Context.java),
die [ServiceContext](../yorozuya-common/src/main/java/com/github/ksewen/yorozuya/common/context/impl/ServiceContext.java)
heißt, wird mithilfe ThreadLocal angeboten.

Als Dienstanbieter lassen sich relevante Schlüssel-Wert-Paare, die über HTTP-Header übergeben werden müssen, automatisch
in den aktuellen Kontext einfügen und problemlos über Feign oder RestTemplate an nachfolgende Service weiterreichen.

Mithilfe der folgenden Attribute kann das Verhalten der Funktion in den Griff bekommen werden:

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
bitte die [offizielle Dokumentation](https://micrometer.io/docs/observation), um mehr darüber zu erfahren.

### AOP Protokollierung

Standardmäßig wird Protokollierung der Informationen von Request und Response (Debug) so wie Fehlerprotokollen (Error)
bereitgestellt.

Anhand der folgenden Attribute kann dieses Feature deaktiviert werden:

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

Mithilfe
Annotationen [LoggerTrace](./src/main/java/com/github/ksewen/yorozuya/starter/annotation/logger/LoggerTrace.java)
und [LoggerTrace](./src/main/java/com/github/ksewen/yorozuya/starter/annotation/logger/LoggerNotTrace.java) das
spezifische Verhalten von AOP Logger festzulegen, könnte als eine Alternative dazu betrachtet.
