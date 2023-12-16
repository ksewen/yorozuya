package com.github.ksewen.yorozuya.auth.server.configuration;

import com.github.ksewen.yorozuya.auth.server.security.AuthenticationManager;
import com.github.ksewen.yorozuya.auth.server.security.ContextAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author ksewen
 * @date 12.12.2023 22:58
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(ResourceServerMarkerAutoConfiguration.Marker.class)
@AutoConfigureAfter(ResourceServerMarkerAutoConfiguration.class)
public class ResourceServerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(AuthenticationManager.class)
  public AuthenticationManager authenticationManager(
      @Autowired UserDetailsService userDetailsService) {
    return new ContextAuthenticationManager(userDetailsService);
  }
}
