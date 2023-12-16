# YOROZUYA-AUTH-SERVER

[English](./README.md) | [简体中文](./README_CN.md)

## Auth Server

### Verwendung

Der Auth-Server spielt eine Role bei der Ausgabe des Tokens, wenn sich die Benutzer anmelden.
Mit der
Annotation [@EnableAuthServer](./src/main/java/com/github/ksewen/yorozuya/auth/server/annotation/EnableAuthServer.java)
kann man diese Funktion aktivieren.

Zum Beispiel:

```java

@SpringBootApplication
@EnableAuthServer
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
```

### Konfiguration

Um das Verhalten während der Generierung des Tokens zu konfigurieren, überprüfe bitte die folgenden Attribute:

```yaml
security:
  token:
    # Zeitüberschreitung des Access-Tokens, die standardmäßige Wert ist 12 Stunden.
    expiration: PT2H
    # Zeitüberschreitung des Refresh-Tokens, die standardmäßige Wert ist 7 Tage.
    refresh-token-expiration: PT24H
    # Name des Emittenten, normalerweise wird die URL des Emittenten festgelegt.
    issuer: https://github.com/ksewen/yorozuya
    access-public-key: # Inhalt des RSA-Public-Key für Access-Token
    access-private-key: # Inhalt des RSA-Private-Key für Access-Token
    refresh-public-key: # Inhalt des RSA-Public-Key für Refresh-Token
    refresh-private-key: # Inhalt des RSA-Private-Key für Refresh-Token
    # Setze dies auf „true“, um die Berechtigungen des Benutzers im Token zu kodieren.
    # Die standardmäßige Wert ist „false“, die Informationen der Berechtigungen wird vom Token nicht enthalten.
    with-authorities: true
  # Setze dies auf „false“, wird die Endpunkte von Actuator von Spring Security nicht geschützt.
  # Die standardmäßige Wert ist „true“.
  protect-management-endpoints: false
  # Die definierten Schnittstellen werden nicht geschützt.
  # Das Format ist „Method: URI“. „ALL“ bedeutet alle HTTP-Methoden.
  permit-urls:
    ALL: /auth/**
    get: /test/non-authentication
```

### Anpassen

#### AuthController

Standardmäßig wird eine Implementierung des Endpunkts für Anmeldung
als [AuthController](./src/main/java/com/github/ksewen/yorozuya/auth/server/controller/AuthController.java) angeboten
und bereits in den Spring Context durch die
Konfiguration [AuthControllerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/AuthControllerAutoConfiguration.java)
registriert.

Wenn du diese Implementierung direkt benutzen möchtest, achte bitte darauf, eine Implementierung
von [UserDetailsExtraDefinition](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/UserDetailsExtraDefinition.java),
in der „userId“ deklariert und zugewiesen wird, zu übergeben, während
du [UserDetailsService](https://github.com/spring-projects/spring-security/blob/main/core/src/main/java/org/springframework/security/core/userdetails/UserDetailsService.java)
implementierst. In der standardmäßigen Definition
von [UserDetailsExtraService](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/impl/UserDetailsExtraServiceImpl.java),
die durch die
Konfiguration [AuthControllerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/AuthControllerAutoConfiguration.java)
registriert,
wird [UserDetails](https://github.com/spring-projects/spring-security/blob/main/core/src/main/java/org/springframework/security/core/userdetails/UserDetails.java)
zwangsweise
als [UserDetailsExtraDefinition](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/UserDetailsExtraDefinition.java)
umgewandelt. Falls der Typ der Übergabe dazu nicht passt, übergibt diese Method einfach „null“ als Identifikation des
Benutzers.

#### Andere Beans

Beans in der Liste kannst du ganz einfach durch eine eigene Implementierung ersetzen.

* [JwtToUserConverter](./src/main/java/com/github/ksewen/yorozuya/auth/server/token/impl/JwtToUserConverter.java)
* [PasswordEncoder](https://github.com/spring-projects/spring-security/blob/main/crypto/src/main/java/org/springframework/security/crypto/password/PasswordEncoder.java)
* [DaoAuthenticationProvider](https://github.com/spring-projects/spring-security/blob/main/core/src/main/java/org/springframework/security/authentication/dao/DaoAuthenticationProvider.java)
* [JwtAuthenticationProvider](https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-resource-server/src/main/java/org/springframework/security/oauth2/server/resource/authentication/JwtAuthenticationProvider.java)
* [UserDetailsExtraService<R, T>](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/UserDetailsExtraService.java)
* [KeyPairManager](./src/main/java/com/github/ksewen/yorozuya/auth/server/key/KeyPairManager.java)
* [TokenProvider](./src/main/java/com/github/ksewen/yorozuya/auth/server/token/TokenProvider.java)
* [JwtEncoder](https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-jose/src/main/java/org/springframework/security/oauth2/jwt/JwtEncoder.java)
* [JwtDecoder](https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-jose/src/main/java/org/springframework/security/oauth2/jwt/JwtDecoder.java)

Um über die bestimmten Konditionen zu erfahren, überprüfe bitte die folgende Liste:

* [AuthControllerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/AuthControllerAutoConfiguration.java)
* [AuthServerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/AuthServerAutoConfiguration.java)
* [TokenAuthenticationAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/TokenAuthenticationAutoConfiguration.java)

### Beispielprojekt

Im [Beispielprojekt](../../yorozuya-samples/auth-server) findest du einige Beispiele, die dir zeigen, wie man einen
Auth-Server mithilfe einer Datenbank erstellen kann.

## Resource Server

### Verwendung

Der Resource-Server spielt eine Role bei der Verifizierung des Tokens, wenn die Benutzer mit dem Token Zugriff auf
Ressourcen erhalten, die eine Authentifizierung erfordern. Und es injiziert die Information des aktuellen Benutzers in
den aktuellen Kontext.
Mit der
Annotation [@EnableResourceServer](./src/main/java/com/github/ksewen/yorozuya/auth/server/annotation/EnableResourceServer.java)
kann man diese Funktion aktivieren.

Zum Beispiel:

```java

@SpringBootApplication
@EnableResourceServer
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
```

### Konfiguration

Überprüfe bitte die folgenden Attribute:

```yaml
security:
  token:
    access-public-key: # Inhalt des RSA-Public-Key für Access-Token
    access-private-key: # Inhalt des RSA-Private-Key für Access-Token
    refresh-public-key: # Inhalt des RSA-Public-Key für Refresh-Token
    refresh-private-key: # Inhalt des RSA-Private-Key für Refresh-Token
  # Setze dies auf „false“, wird die Endpunkte von Actuator von Spring Security nicht geschützt.
  # Die standardmäßige Wert ist „true“.
  protect-management-endpoints: false
  # Die definierten Schnittstellen werden nicht geschützt.
  # Das Format ist „Method: URI“. „ALL“ bedeutet alle HTTP-Methoden.
  permit-urls:
    ALL: /auth/**
    get: /test/non-authentication
```

### Anpassen

#### Beans

Beans in der Liste kannst du ganz einfach durch eine eigene Implementierung ersetzen.

* [JwtToUserConverter](./src/main/java/com/github/ksewen/yorozuya/auth/server/token/impl/JwtToUserConverter.java)
* [PasswordEncoder](https://github.com/spring-projects/spring-security/blob/main/crypto/src/main/java/org/springframework/security/crypto/password/PasswordEncoder.java)
* [DaoAuthenticationProvider](https://github.com/spring-projects/spring-security/blob/main/core/src/main/java/org/springframework/security/authentication/dao/DaoAuthenticationProvider.java)
* [JwtAuthenticationProvider](https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-resource-server/src/main/java/org/springframework/security/oauth2/server/resource/authentication/JwtAuthenticationProvider.java)
* [UserDetailsExtraService<R, T>](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/UserDetailsExtraService.java)
* [KeyPairManager](./src/main/java/com/github/ksewen/yorozuya/auth/server/key/KeyPairManager.java)
* [TokenProvider](./src/main/java/com/github/ksewen/yorozuya/auth/server/token/TokenProvider.java)
* [JwtEncoder](https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-jose/src/main/java/org/springframework/security/oauth2/jwt/JwtEncoder.java)
* [JwtDecoder](https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-jose/src/main/java/org/springframework/security/oauth2/jwt/JwtDecoder.java)
* [AuthenticationEntryPoint](https://github.com/spring-projects/spring-security/blob/main/web/src/main/java/org/springframework/security/web/AuthenticationEntryPoint.java)
* [AccessDeniedHandler](https://github.com/spring-projects/spring-security/blob/main/web/src/main/java/org/springframework/security/web/access/AccessDeniedHandler.java)

Um über die bestimmten Konditionen zu erfahren, überprüfe bitte die folgende Liste:

* [AuthServerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/AuthServerAutoConfiguration.java)
* [ResourceServerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/ResourceServerAutoConfiguration.java)
* [TokenAuthenticationAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/TokenAuthenticationAutoConfiguration.java)

### Erhalte die Information des aktuellen Benutzers

Mit der Implementierung des
Interfaces [AuthenticationManager](./src/main/java/com/github/ksewen/yorozuya/auth/server/security/AuthenticationManager.java)
kann man ganz einfach die Information des aktuellen Benutzer bekommen. Die standardmäßige
Implementierung [ContextAuthenticationManager](./src/main/java/com/github/ksewen/yorozuya/auth/server/security/ContextAuthenticationManager.java)
wird bereits in den Spring Kontext registriert. Ebenso, du solltest eine Implementierung
von [UserDetailsExtraDefinition](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/UserDetailsExtraDefinition.java),
in der „userId“ deklariert und zugewiesen wird, übergeben, während
du [UserDetailsService](https://github.com/spring-projects/spring-security/blob/main/core/src/main/java/org/springframework/security/core/userdetails/UserDetailsService.java)
implementierst, wenn du diese Implementierung direkt verwendest.

### Beispielprojekt

Im [Beispielprojekt](../../yorozuya-samples/auth-server) findest du einige Beispiele, die dir zeigen, wie man einen
Resource-Server mithilfe einer Datenbank erstellen kann.
