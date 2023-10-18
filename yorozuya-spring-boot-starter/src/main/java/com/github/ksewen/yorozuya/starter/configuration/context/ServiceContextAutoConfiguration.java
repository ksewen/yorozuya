package com.github.ksewen.yorozuya.starter.configuration.context;

import com.github.ksewen.yorozuya.common.context.Context;
import com.github.ksewen.yorozuya.common.context.impl.ServiceContext;
import com.github.ksewen.yorozuya.starter.configuration.context.interceptor.ContextClientHttpRequestInterceptor;
import com.github.ksewen.yorozuya.starter.configuration.context.interceptor.ContextRequestInterceptor;
import com.github.ksewen.yorozuya.starter.filter.context.ContextFilter;
import com.github.ksewen.yorozuya.starter.filter.context.extension.ContextFilterExtension;
import com.github.ksewen.yorozuya.starter.helper.context.ContextHelpers;
import com.github.ksewen.yorozuya.starter.helper.context.impl.ServiceContextHelpers;
import feign.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/**
 * @author ksewen
 * @date 13.10.2023 22:23
 */
@Configuration
@EnableConfigurationProperties(ServiceContextProperties.class)
@RequiredArgsConstructor
public class ServiceContextAutoConfiguration {

  private final ServiceContextProperties properties;

  private final String FILTER_NAME = "contextFilter";

  @Bean
  @Primary
  public Context serviceContext() {
    return ServiceContext.getInstance();
  }

  @Bean
  @ConditionalOnBean(Context.class)
  @ConditionalOnMissingBean(ContextHelpers.class)
  public ContextHelpers serviceContextHelpers(@Autowired Context context) {
    return new ServiceContextHelpers(context, this.properties.getHeaderPrefix());
  }

  @Bean
  @ConditionalOnBean(Context.class)
  public FilterRegistrationBean<ContextFilter> filterRegistrationBean(
      @Autowired Context context, @Autowired ObjectProvider<ContextFilterExtension> extensions) {
    FilterRegistrationBean<ContextFilter> registration =
        new FilterRegistrationBean<>(
            new ContextFilter(
                context,
                this.properties.getDefaultInjectKeySet(),
                extensions.orderedStream().toList(),
                this.properties.getHeaderPrefix()));
    registration.setName(this.FILTER_NAME);
    return registration;
  }

  @Bean
  @ConditionalOnClass(Client.class)
  public ContextRequestInterceptor contextRequestInterceptor(@Autowired Context context) {
    return new ContextRequestInterceptor(context, this.properties);
  }

  @Bean
  @ConditionalOnClass(RestTemplate.class)
  public ContextClientHttpRequestInterceptor contextClientHttpRequestInterceptor(
      @Autowired Context context) {
    return new ContextClientHttpRequestInterceptor(context, this.properties);
  }
}
