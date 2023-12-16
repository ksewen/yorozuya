package com.github.ksewen.yorozuya.auth.server.configuration;

import com.github.ksewen.yorozuya.auth.server.token.impl.JwtToUserConverter;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author ksewen
 * @date 01.12.2023 20:21
 */
@Configuration
@Conditional(AuthServerAutoConfiguration.AuthOderResourceServerEnabled.class)
@EnableConfigurationProperties(SecurityProperties.class)
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityAutoConfiguration {

  private final JwtToUserConverter jwtToUserConverter;

  private final SecurityProperties securityProperties;

  private final WebEndpointProperties webEndpointProperties;
  private final List<String> SWAGGER_PERMIT_LIST =
      Arrays.asList(
          "/v3/api-docs/**",
          "/v3/api-docs.yaml",
          "/swagger-ui.html",
          "/swagger-ui*/*swagger-initializer.js",
          "/swagger-ui*/**",
          "/webjars/**");

  private final String ALL = "ALL";

  @Bean
  @ConditionalOnBean(AuthServerMarkerAutoConfiguration.Marker.class)
  @ConditionalOnMissingBean(
      value = {ResourceServerMarkerAutoConfiguration.Marker.class, SecurityFilterChain.class})
  public SecurityFilterChain authSecurityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
        .authorizeHttpRequests(this.authorizeHttpRequestsCustomizer())
        .exceptionHandling(
            (exceptions) ->
                exceptions
                    .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));
    return http.build();
  }

  @Bean
  @ConditionalOnBean(ResourceServerMarkerAutoConfiguration.Marker.class)
  @ConditionalOnMissingBean(SecurityFilterChain.class)
  public SecurityFilterChain resourceSecurityFilterChain(
      HttpSecurity http,
      @Autowired AuthenticationEntryPoint authenticationEntryPoint,
      @Autowired AccessDeniedHandler accessDeniedHandler)
      throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(this.authorizeHttpRequestsCustomizer())
        .oauth2ResourceServer(
            (oauth2) ->
                oauth2.jwt((jwt) -> jwt.jwtAuthenticationConverter(this.jwtToUserConverter)))
        .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
        .exceptionHandling(
            (exceptions) ->
                exceptions
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler));
    return http.build();
  }

  private Customizer<
          AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>
      authorizeHttpRequestsCustomizer() {
    return (authorize) -> {
      if (!this.securityProperties.isProtectManagementEndpoints()) {
        authorize.requestMatchers(this.webEndpointProperties.getBasePath()).permitAll();
      }
      this.securityProperties
          .getPermitUrls()
          .forEach(
              (key, value) -> {
                String method = key.toUpperCase();
                if (this.ALL.equals(method)) {
                  authorize.requestMatchers(value).permitAll();
                } else {
                  authorize.requestMatchers(HttpMethod.valueOf(method), value).permitAll();
                }
              });
      this.SWAGGER_PERMIT_LIST.forEach(
          u -> authorize.requestMatchers(HttpMethod.GET, u).permitAll());
      authorize.anyRequest().authenticated();
    };
  }
}
