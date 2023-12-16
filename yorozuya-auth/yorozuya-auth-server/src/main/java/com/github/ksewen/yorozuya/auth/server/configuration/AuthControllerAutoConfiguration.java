package com.github.ksewen.yorozuya.auth.server.configuration;

import com.github.ksewen.yorozuya.auth.server.controller.AuthController;
import com.github.ksewen.yorozuya.auth.server.service.UserDetailsExtraService;
import com.github.ksewen.yorozuya.auth.server.service.impl.UserDetailsExtraServiceImpl;
import com.github.ksewen.yorozuya.auth.server.token.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author ksewen
 * @date 13.12.2023 17:30
 */
@Configuration
@ConditionalOnBean(AuthServerMarkerAutoConfiguration.Marker.class)
@ConditionalOnProperty(
    value = "security.login.endpoint.enable",
    havingValue = "true",
    matchIfMissing = true)
public class AuthControllerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(name = "userDetailsExtraService")
  public UserDetailsExtraService<Long, UserDetails> userDetailsExtraService() {
    return new UserDetailsExtraServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean(name = "authController")
  public AuthController authController(
      @Autowired DaoAuthenticationProvider authenticationProvider,
      @Autowired UserDetailsService userDetailsService,
      @Autowired UserDetailsExtraService<Long, UserDetails> userDetailsExtraService,
      @Autowired TokenProvider tokenProvider) {
    return new AuthController(
        authenticationProvider, userDetailsService, userDetailsExtraService, tokenProvider);
  }
}
