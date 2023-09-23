package com.github.ksewen.yorozuya.starter.configuration.redis.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

/**
 * @author ksewen
 * @date 23.09.2023 10:04
 */
public class Jackson2JsonRedisTemplate extends RedisTemplate<String, Object> {

  public Jackson2JsonRedisTemplate(ObjectMapper objectMapper) {
    Assert.notNull(
        objectMapper,
        "ObjectMapper cannot be null, when constructing a Jackson2JsonRedisTemplate instance");
    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
        new Jackson2JsonRedisSerializer(objectMapper, Object.class);
    setKeySerializer(RedisSerializer.string());
    setValueSerializer(jackson2JsonRedisSerializer);
    setHashKeySerializer(RedisSerializer.string());
    setHashValueSerializer(jackson2JsonRedisSerializer);
  }
}
