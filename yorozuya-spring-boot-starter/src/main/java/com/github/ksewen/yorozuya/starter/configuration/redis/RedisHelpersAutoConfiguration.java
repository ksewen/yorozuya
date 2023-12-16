package com.github.ksewen.yorozuya.starter.configuration.redis;

import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import com.github.ksewen.yorozuya.starter.helper.redis.RedisHelperProperties;
import com.github.ksewen.yorozuya.starter.helper.redis.RedisHelpers;
import com.github.ksewen.yorozuya.starter.helper.redis.impl.RedisCommonHelpers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author ksewen
 * @date 24.09.2023 22:23
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisHelperProperties.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@RequiredArgsConstructor
public class RedisHelpersAutoConfiguration {

  private final RedisHelperProperties properties;

  @Bean
  @ConditionalOnBean(StringRedisTemplate.class)
  @ConditionalOnMissingBean(RedisHelpers.class)
  public RedisHelpers redisCommonHelpers(
      @Autowired StringRedisTemplate redisTemplate, @Autowired JsonHelpers jsonHelpers) {
    return new RedisCommonHelpers(redisTemplate, jsonHelpers, properties);
  }
}
