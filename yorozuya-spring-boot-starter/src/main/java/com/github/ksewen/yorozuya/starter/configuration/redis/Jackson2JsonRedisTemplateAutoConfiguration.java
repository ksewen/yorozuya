package com.github.ksewen.yorozuya.starter.configuration.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ksewen.yorozuya.starter.configuration.redis.template.Jackson2JsonRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;

/**
 * @author ksewen
 * @date 22.09.2023 20:50
 */
@Configuration
@ConditionalOnClass(RedisOperations.class)
public class Jackson2JsonRedisTemplateAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(name = "redisTemplate")
  @ConditionalOnProperty(
      value = "spring.data.redis.template.jackson.enable",
      havingValue = "true",
      matchIfMissing = true)
  public Jackson2JsonRedisTemplate redisTemplate(
      @Autowired RedisConnectionFactory redisConnectionFactory,
      @Autowired ObjectMapper objectMapper) {
    Jackson2JsonRedisTemplate template = new Jackson2JsonRedisTemplate(objectMapper);
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }
}
