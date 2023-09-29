package com.github.ksewen.yorozuya.starter.configuration.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.github.ksewen.yorozuya.starter.configuration.redis.template.GzipStringRedisTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * @author ksewen
 * @date 23.09.2023 10:43
 */
class GzipStringRedisTemplateAutoConfigurationTest {

  @Test
  void gzipStringRedisTemplate() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                RedisAutoConfiguration.class,
                JacksonAutoConfiguration.class,
                GzipStringRedisTemplateAutoConfiguration.class))
        .withPropertyValues("spring.data.redis.template.gzip.enable=true")
        .run(
            (context) -> {
              assertThat(context).hasBean("stringRedisTemplate");
              assertThat(context).hasSingleBean(GzipStringRedisTemplate.class);
              assertThat(context)
                  .getBean("stringRedisTemplate")
                  .isSameAs(context.getBean(GzipStringRedisTemplate.class));
            });
  }

  @Test
  void nonGzipStringRedisTemplate() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                RedisAutoConfiguration.class,
                JacksonAutoConfiguration.class,
                GzipStringRedisTemplateAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasBean("stringRedisTemplate");
              assertThat(context).doesNotHaveBean(GzipStringRedisTemplate.class);
            });
  }
}
