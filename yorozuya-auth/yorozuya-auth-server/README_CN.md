# YOROZUYA-AUTH-SERVER

[English](./README.md) | [Deutsch](./README_DE.md)

## Auth Server

### 使用

Auth-Server 在用户登陆的时候，扮演了 Token 颁发者的角色。
使用注解 [@EnableAuthServer](./src/main/java/com/github/ksewen/yorozuya/auth/server/annotation/EnableAuthServer.java)
可以启动这个功能。

示例:

```java

@SpringBootApplication
@EnableAuthServer
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
```

### 配置

要控制 Token 生成的行为，请参考以下配置：

```yaml
security:
  token:
    # Access-Tokens 的过期时间，默认值是12小时。
    expiration: PT2H
    # Refresh-Tokens 的过期时间，默认值是7天。
    refresh-token-expiration: PT24H
    # 颁发者的名称，一般来说会设置为颁发者的 URL。
    issuer: https://github.com/ksewen/yorozuya
    access-public-key: # 为 Access-Token 指定 RSA-Public-Key 的内容
    access-private-key: # 为 Access-Token 指定 RSA-Private-Key 的内容
    refresh-public-key: # 为 Refresh-Token 指定 RSA-Public-Key 的内容
    refresh-private-key: # 为 Refresh-Token 指定 RSA-Private-Key 的内容
    # 设置为“true”，将用户权限编码到 Token。
    # 默认值是“false”，Token 中将不会包含用户权限信息。
    with-authorities: true
  # 设置为“false”，Actuator 的端点将不再受 Spring Security 保护。
  # 默认值是“true”。
  protect-management-endpoints: false
  # 配置的接口将无需鉴权。
  # 格式为：“HTTP方法：URI”，“ALL”表示允许所有 HTTP方法。
  permit-urls:
    ALL: /auth/**
    get: /test/non-authentication
```

### 自定义

#### AuthController

默认提供的登陆端点 [AuthController](./src/main/java/com/github/ksewen/yorozuya/auth/server/controller/AuthController.java)
已经通过配置 [AuthControllerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/AuthControllerAutoConfiguration.java)
注入到 Spring 上下文中。

如果想要直接使用这个默认的实现，请注意在实现 [UserDetailsService](https://github.com/spring-projects/spring-security/blob/main/core/src/main/java/org/springframework/security/core/userdetails/UserDetailsService.java)
的同时，将返回值指定为一个 [UserDetailsExtraDefinition](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/UserDetailsExtraDefinition.java)
的实现，确保在其中声明 userId
并赋值。配置 [AuthControllerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/AuthControllerAutoConfiguration.java)
中注册的 [UserDetailsExtraService](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/impl/UserDetailsExtraServiceImpl.java)
默认实现，会将 [UserDetails](https://github.com/spring-projects/spring-security/blob/main/core/src/main/java/org/springframework/security/core/userdetails/UserDetails.java)
强制转换为 [UserDetailsExtraDefinition](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/UserDetailsExtraDefinition.java)
以获取 userId。如果类型不匹配，将直接返回“null”。

#### 其他 Beans

以下列表提及的 Beans，均可以很容易的替换为自定义的实现：

* [JwtToUserConverter](./src/main/java/com/github/ksewen/yorozuya/auth/server/token/impl/JwtToUserConverter.java)
* [PasswordEncoder](https://github.com/spring-projects/spring-security/blob/main/crypto/src/main/java/org/springframework/security/crypto/password/PasswordEncoder.java)
* [DaoAuthenticationProvider](https://github.com/spring-projects/spring-security/blob/main/core/src/main/java/org/springframework/security/authentication/dao/DaoAuthenticationProvider.java)
* [JwtAuthenticationProvider](https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-resource-server/src/main/java/org/springframework/security/oauth2/server/resource/authentication/JwtAuthenticationProvider.java)
* [UserDetailsExtraService<R, T>](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/UserDetailsExtraService.java)
* [KeyPairManager](./src/main/java/com/github/ksewen/yorozuya/auth/server/key/KeyPairManager.java)
* [TokenProvider](./src/main/java/com/github/ksewen/yorozuya/auth/server/token/TokenProvider.java)
* [JwtEncoder](https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-jose/src/main/java/org/springframework/security/oauth2/jwt/JwtEncoder.java)
* [JwtDecoder](https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-jose/src/main/java/org/springframework/security/oauth2/jwt/JwtDecoder.java)

查看下列配置类，以明确具体的加载条件：

* [AuthControllerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/AuthControllerAutoConfiguration.java)
* [AuthServerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/AuthServerAutoConfiguration.java)
* [TokenAuthenticationAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/TokenAuthenticationAutoConfiguration.java)

### 示例项目

[示例项目](../../yorozuya-samples/auth-server) 展示了如何利用数据库创建一个 Auth-Server。

## Resource Server

### 使用

Resource-Server 在用户携带 Token 访问需要认证的资源时，扮演一个验证者的角色。并将验证通过的用户信息注入当前上下文。
使用注解 [@EnableResourceServer](./src/main/java/com/github/ksewen/yorozuya/auth/server/annotation/EnableResourceServer.java)
可以启动这个功能。

示例:

```java

@SpringBootApplication
@EnableResourceServer
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
```

### 配置

参考以下配置：

```yaml
security:
  token:
    access-public-key: # 为 Access-Token 指定 RSA-Public-Key 的内容
    access-private-key: # 为 Access-Token 指定 RSA-Private-Key 的内容
    refresh-public-key: # 为 Refresh-Token 指定 RSA-Public-Key 的内容
    refresh-private-key: # 为 Refresh-Token 指定 RSA-Private-Key 的内容
  # 设置为“false”，Actuator 的端点将不再受 Spring Security 保护。
  # 默认值是“true”。
  protect-management-endpoints: false
  # 配置的接口将无需鉴权。
  # 格式为：“HTTP方法：URI”，“ALL”表示允许所有 HTTP方法。
  permit-urls:
    ALL: /auth/**
    get: /test/non-authentication
```

### 自定义

#### Beans

以下列表提及的 Beans，均可以很容易的替换为自定义的实现：

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

查看下列配置类，以明确具体的加载条件：

* [AuthServerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/AuthServerAutoConfiguration.java)
* [ResourceServerAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/ResourceServerAutoConfiguration.java)
* [TokenAuthenticationAutoConfiguration](./src/main/java/com/github/ksewen/yorozuya/auth/server/configuration/TokenAuthenticationAutoConfiguration.java)

### 获取当前用户信息

借助接口 [AuthenticationManager](./src/main/java/com/github/ksewen/yorozuya/auth/server/security/AuthenticationManager.java)
的实现可以轻松获取 userId
和用户名。默认的实现 [ContextAuthenticationManager](./src/main/java/com/github/ksewen/yorozuya/auth/server/security/ContextAuthenticationManager.java)
已被注册在 Spring
上下文中。同样的，想直接使用这个功能，请在实现 [UserDetailsService](https://github.com/spring-projects/spring-security/blob/main/core/src/main/java/org/springframework/security/core/userdetails/UserDetailsService.java)
的同时，将返回值指定为一个 [UserDetailsExtraDefinition](./src/main/java/com/github/ksewen/yorozuya/auth/server/service/UserDetailsExtraDefinition.java)
的实现，确保在其中声明 userId 并赋值。

### 示例项目

[示例项目](../../yorozuya-samples/auth-server) 展示了如何利用数据库创建一个 Resource-Server。

