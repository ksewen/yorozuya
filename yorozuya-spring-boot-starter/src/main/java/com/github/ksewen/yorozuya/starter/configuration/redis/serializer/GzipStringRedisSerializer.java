package com.github.ksewen.yorozuya.starter.configuration.redis.serializer;

import com.github.ksewen.yorozuya.common.util.GzipUtils;
import org.springframework.core.serializer.support.SerializationFailedException;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author ksewen
 * @date 23.09.2023 10:31
 */
public class GzipStringRedisSerializer implements RedisSerializer<String> {

  @Override
  public byte[] serialize(String s) throws SerializationException {
    try {
      return GzipUtils.compress(s);
    } catch (Throwable ex) {
      throw new SerializationFailedException("gzip compression failed", ex);
    }
  }

  @Override
  public String deserialize(byte[] bytes) throws SerializationException {
    try {
      byte[] result = GzipUtils.decompress(bytes);
      if (result == null) {
        return null;
      }
      return new String(result);
    } catch (Throwable ex) {
      throw new SerializationFailedException(
          "gzip decompression failed，please confirm that the value stored in Redis is a valid GZip stream",
          ex);
    }
  }

  @Override
  public Class<?> getTargetType() {
    return String.class;
  }
}
