package com.github.ksewen.yorozuya.starter.configuration.redis;

import com.github.ksewen.yorozuya.starter.configuration.redis.template.GzipStringRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;

/**
 * @author ksewen
 * @date 23.09.2023 10:38
 */
@Configuration
@ConditionalOnClass(RedisOperations.class)
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class GzipStringRedisTemplateAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(value = "spring.data.redis.template.gzip.enable", havingValue = "true")
  public GzipStringRedisTemplate stringRedisTemplate(
      @Autowired RedisConnectionFactory redisConnectionFactory) {
    GzipStringRedisTemplate template = new GzipStringRedisTemplate();
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }
}
