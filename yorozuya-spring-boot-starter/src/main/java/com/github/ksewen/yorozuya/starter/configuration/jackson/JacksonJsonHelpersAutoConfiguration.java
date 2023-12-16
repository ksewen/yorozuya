package com.github.ksewen.yorozuya.starter.configuration.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import com.github.ksewen.yorozuya.starter.helper.json.impl.JacksonJsonHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 26.09.2023 22:53
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ObjectMapper.class)
public class JacksonJsonHelpersAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(JsonHelpers.class)
  public JsonHelpers jacksonJsonHelpers(@Autowired ObjectMapper objectMapper) {
    return new JacksonJsonHelpers(objectMapper);
  }
}
