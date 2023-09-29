package com.github.ksewen.yorozuya.starter.configuration.redis.template;

import com.github.ksewen.yorozuya.starter.configuration.redis.serializer.GzipStringRedisSerializer;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author ksewen
 * @date 23.09.2023 10:05
 */
public class GzipStringRedisTemplate extends StringRedisTemplate {

  public GzipStringRedisTemplate() {
    GzipStringRedisSerializer gzipStringRedisSerializer = new GzipStringRedisSerializer();
    setValueSerializer(gzipStringRedisSerializer);
    setHashValueSerializer(gzipStringRedisSerializer);
  }
}
