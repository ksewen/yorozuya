package com.github.ksewen.yorozuya.auth.server.configuration;

import com.github.ksewen.yorozuya.auth.server.token.impl.JwtToUserConverter;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

/**
 * @author ksewen
 * @date 02.12.2023 22:42
 */
@Configuration(proxyBeanMethods = false)
@Conditional(AuthServerAutoConfiguration.AuthOderResourceServerEnabled.class)
@AutoConfigureAfter(AuthServerMarkerAutoConfiguration.class)
public class AuthServerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(JwtToUserConverter.class)
  public JwtToUserConverter jwtToUserConverter() {
    return new JwtToUserConverter();
  }

  @Bean
  @ConditionalOnMissingBean(PasswordEncoder.class)
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @ConditionalOnMissingBean(DaoAuthenticationProvider.class)
  public DaoAuthenticationProvider daoAuthenticationProvider(
      @Autowired PasswordEncoder passwordEncoder,
      @Autowired UserDetailsService userDetailsService) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(userDetailsService);
    return provider;
  }

  @Bean("refreshTokenAuthenticationProvider")
  @ConditionalOnBean(ResourceServerMarkerAutoConfiguration.Marker.class)
  @ConditionalOnMissingBean(name = "refreshTokenAuthenticationProvider")
  public JwtAuthenticationProvider refreshTokenAuthenticationProvider(
      @Autowired @Qualifier("jwtRefreshTokenDecoder") JwtDecoder jwtRefreshTokenDecoder,
      @Autowired JwtToUserConverter jwtToUserConverter) {
    JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtRefreshTokenDecoder);
    provider.setJwtAuthenticationConverter(jwtToUserConverter);
    return provider;
  }

  @Generated
  public static final class AuthOderResourceServerEnabled extends AnyNestedCondition {

    public AuthOderResourceServerEnabled() {
      super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnBean(AuthServerMarkerAutoConfiguration.Marker.class)
    @Generated
    @SuppressWarnings("unused")
    static class AuthServerEnable {}

    @ConditionalOnBean(ResourceServerMarkerAutoConfiguration.Marker.class)
    @Generated
    @SuppressWarnings("unused")
    static class ResourceServerEnable {}
  }
}
