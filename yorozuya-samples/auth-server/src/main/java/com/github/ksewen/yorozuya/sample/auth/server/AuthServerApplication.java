package com.github.ksewen.yorozuya.sample.auth.server;

import com.github.ksewen.yorozuya.auth.server.annotation.EnableAuthServer;
import com.github.ksewen.yorozuya.auth.server.annotation.EnableResourceServer;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

@SpringBootApplication(exclude = RedisAutoConfiguration.class)
@EnableAuthServer
@EnableResourceServer
@OpenAPIDefinition(info = @Info(title = "auth-server", version = "0.0.1"))
@SecurityScheme(
    name = "jwt-auth",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    in = SecuritySchemeIn.HEADER)
public class AuthServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthServerApplication.class, args);
  }
}
