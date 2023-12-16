# YOROZUYA-AUTH-SERVER

[Deutsch](./README_DE.md) | [简体中文](./README_CN.md)

## Auth Server

### Usage

The Auth Server plays a role in issuing tokens when users log in.
With the
annotation [@EnableAuthServer](./src/main/java/com/github/ksewen/yorozuya/auth/server/annotation/EnableAuthServer.java),
this feature can be enabled.

For example:

```java

@SpringBootApplication
@EnableAuthServer
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
```

### Configuration

To configure the behavior during token generation, please review the following properties:

```yaml
security:
  token:
    # Access token timeout, default value is 12 hours.
    expiration: PT2H
    # Refresh token timeout, default value is 7 days.
    refresh-token-expiration: PT24H
    # Name of the issuer, usually the URL of the issuer is set.
    issuer: https://github.com/ksewen/yorozuya
    access-public-key: # Content of the RSA Public Key for access token
    access-private-key: # Content of the RSA Private Key for access token
    refresh-public-key: # Content of the RSA Public Key for refresh token
    refresh-private-key: # Content of the RSA Private Key for refresh token
    # Set this to "true" to encode the user's permissions in the token.
    # The default value is "false", where permission information is not included in the token.
    with-authorities: true
  # Set this to "false" to leave Spring Security Actuator endpoints unprotected.
  # The default value is "true".
  protect-management-endpoints: false
  # The defined interfaces are not protected.
  # The format is "Method: URI". "ALL" means all HTTP methods.
  permit-urls:
    ALL: /auth/**
    get: /test/non-authentication
```

### Customization

#### AuthController

By default, an implementation of the authentication endpoint
is provided as [AuthController](./src/main/java/com/github/ksewen/yorozuya/auth/server/controller/AuthController.java)
and registered in the Spring context through the
configuration [AuthControllerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/AuthControllerAutoConfiguration.java).

If you intend to directly use this implementation, please ensure to pass an implementation
of [UserDetailsExtraDefinition](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/UserDetailsExtraDefinition.java),
where the userId is declared and assigned, while
implementing
the [UserDetailsService](https://github.com/spring-projects/spring-security/blob/main/core/src/main/java/org/springframework/security/core/userdetails/UserDetailsService.java).
In the default definition
of [UserDetailsExtraService](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/impl/UserDetailsExtraServiceImpl.java),
registered by the
configuration [AuthControllerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/AuthControllerAutoConfiguration.java), [UserDetails](https://github.com/spring-projects/spring-security/blob/main/core/src/main/java/org/springframework/security/core/userdetails/UserDetails.java)
is forcibly cast
to [UserDetailsExtraDefinition](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/UserDetailsExtraDefinition.java).
If the type passed does not match, simply pass "null" as the user's identification.

#### Other Beans

Beans in the list can be easily replaced with your own implementation.

* [JwtToUserConverter](./src/main/java/com/github/ksewen/yorozuya/auth/server/token/impl/JwtToUserConverter.java)
* [PasswordEncoder](https://github.com/spring-projects/spring-security/blob/main/crypto/src/main/java/org/springframework/security/crypto/password/PasswordEncoder.java)
* [DaoAuthenticationProvider](https://github.com/spring-projects/spring-security/blob/main/core/src/main/java/org/springframework/security/authentication/dao/DaoAuthenticationProvider.java)
* [JwtAuthenticationProvider](https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-resource-server/src/main/java/org/springframework/security/oauth2/server/resource/authentication/JwtAuthenticationProvider.java)
* [UserDetailsExtraService<R, T>](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/UserDetailsExtraService.java)
* [KeyPairManager](./src/main/java/com/github/ksewen/yorozuya/auth/server/key/KeyPairManager.java)
* [TokenProvider](./src/main/java/com/github/ksewen/yorozuya/auth/server/token/TokenProvider.java)
* [JwtEncoder](https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-jose/src/main/java/org/springframework/security/oauth2/jwt/JwtEncoder.java)
* [JwtDecoder](https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-jose/src/main/java/org/springframework/security/oauth2/jwt/JwtDecoder.java)

To learn about specific conditions, please review the following list:

* [AuthControllerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/AuthControllerAutoConfiguration.java)
* [AuthServerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/AuthServerAutoConfiguration.java)
* [TokenAuthenticationAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/TokenAuthenticationAutoConfiguration.java)

### Sample Project

In the [sample project](../../yorozuya-samples/auth-server), you will find examples demonstrating how to create an
Auth Server using a database.

## Resource Server

### Usage

The Resource Server plays a role in verifying tokens when users access resources that require authentication with the
token.
It also injects the current user's information into the current context.
With the
annotation [@EnableResourceServer](./src/main/java/com/github/ksewen/yorozuya/auth/server/annotation/EnableResourceServer.java),
this feature can be enabled.

For example:

```java

@SpringBootApplication
@EnableResourceServer
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
```

### Configuration

Please review the following properties:

```yaml
security:
  token:
    access-public-key: # Content of the RSA Public Key for access token
    access-private-key: # Content of the RSA Private Key for access token
    refresh-public-key: # Content of the RSA Public Key for refresh token
    refresh-private-key: # Content of the RSA Private Key for refresh token
  # Set this to "false" to leave Spring Security Actuator endpoints unprotected.
  # The default value is "true".
  protect-management-endpoints: false
  # The defined interfaces are not protected.
  # The format is "Method: URI". "ALL" means all HTTP methods.
  permit-urls:
    ALL: /auth/**
    get: /test/non-authentication
```

### Customization

#### Beans

Beans in the list can be easily replaced with your own implementation.

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

To learn about specific conditions, please review the following list:

* [AuthServerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/AuthServerAutoConfiguration.java)
* [ResourceServerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/ResourceServerAutoConfiguration.java)
* [TokenAuthenticationAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/TokenAuthenticationAutoConfiguration.java)

### Obtain the current user's information

By implementing the
interface [AuthenticationManager](./src/main/java/com/github/ksewen/yorozuya/auth/server/security/AuthenticationManager.java)
you can easily obtain the current user's information. The default
implementation [ContextAuthenticationManager](./src/main/java/com/github/ksewen/yorozuya/auth/server/security/ContextAuthenticationManager.java)
is already registered in the Spring context. Similarly, if you intend to directly use this implementation,
you should pass an implementation
of [UserDetailsExtraDefinition](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/UserDetailsExtraDefinition.java),
where the userId is declared and assigned, while implementing
the [UserDetailsService](https://github.com/spring-projects/spring-security/blob/main/core/src/main/java/org/springframework/security/core/userdetails/UserDetailsService.java).

### Sample Project

In the [sample project](../../yorozuya-samples/auth-server), you will find examples demonstrating how to create a
Resource Server using a database.
