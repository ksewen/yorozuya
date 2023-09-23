package com.github.ksewen.yorozuya.starter.configuration.redis;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.starter.configuration.redis.template.Jackson2JsonRedisTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * @author ksewen
 * @date 22.09.2023 21:05
 */
class Jackson2JsonRedisTemplateAutoConfigurationTest {

  @Test
  void redisJacksonTemplate() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                Jackson2JsonRedisTemplateAutoConfiguration.class,
                RedisAutoConfiguration.class,
                JacksonAutoConfiguration.class))
        .run(
            (context) -> {
              assertThat(context).hasBean("redisTemplate");
              assertThat(context).hasSingleBean(Jackson2JsonRedisTemplate.class);
              assertThat(context)
                  .getBean("redisTemplate")
                  .isSameAs(context.getBean(Jackson2JsonRedisTemplate.class));
            });
  }

  @Test
  void nonRedisJacksonTemplate() {
    new ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                Jackson2JsonRedisTemplateAutoConfiguration.class,
                RedisAutoConfiguration.class,
                JacksonAutoConfiguration.class))
        .withPropertyValues("spring.data.redis.template.jackson.enable=false")
        .run(
            (context) -> {
              assertThat(context).hasBean("redisTemplate");
              assertThat(context).doesNotHaveBean(Jackson2JsonRedisTemplate.class);
            });
  }
}
