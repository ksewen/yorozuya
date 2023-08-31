package com.github.ksewen.yorozuya.starter.configuration.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author ksewen
 * @date 31.08.2023 22:11
 */
@Configuration
@ConditionalOnClass(OpenAPI.class)
@EnableConfigurationProperties({SpringDocProperties.class})
@RequiredArgsConstructor
public class SpringDocAutoConfiguration {

  private final SpringDocProperties springDocProperties;

  @Value("${spring.application.name:UNSET}")
  private String applicationName;

  private final String DESCRIPTION_DEFAULT_PREFIX = "Documentation for ";

  @Bean
  @ConditionalOnMissingBean(OpenAPI.class)
  public OpenAPI openAPI() {
    String title =
        StringUtils.hasLength(this.springDocProperties.getTitle())
            ? this.springDocProperties.getTitle()
            : applicationName;
    String description =
        StringUtils.hasLength(this.springDocProperties.getDescription())
            ? this.springDocProperties.getDescription()
            : this.DESCRIPTION_DEFAULT_PREFIX + title;
    return new OpenAPI()
        .info(
            new Info()
                .title(title)
                .description(description)
                .version(this.springDocProperties.getVersion())
                .license(
                    new License()
                        .name(this.springDocProperties.getLicense().getName())
                        .url(this.springDocProperties.getLicense().getUrl())));
  }
}
