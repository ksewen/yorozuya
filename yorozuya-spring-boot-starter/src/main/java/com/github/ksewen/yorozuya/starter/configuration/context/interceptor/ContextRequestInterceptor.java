package com.github.ksewen.yorozuya.starter.configuration.context.interceptor;

import com.github.ksewen.yorozuya.common.context.Context;
import com.github.ksewen.yorozuya.starter.configuration.context.ServiceContextProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.Map;
import org.springframework.util.CollectionUtils;

/**
 * @author ksewen
 * @date 16.10.2023 11:07
 */
public class ContextRequestInterceptor extends ContextKeyValidChecker
    implements RequestInterceptor {

  private final Context context;

  private final ServiceContextProperties properties;

  public ContextRequestInterceptor(Context context, ServiceContextProperties properties) {
    super(properties);
    this.context = context;
    this.properties = properties;
  }

  @Override
  public void apply(RequestTemplate template) {
    Map<String, String> map = context.getContext();
    if (CollectionUtils.isEmpty(map)) {
      return;
    }
    map.forEach(
        (key, value) -> {
          if (this.valid(key)) {
            this.process(key, value, template);
          }
        });
  }

  protected void process(String key, String value, RequestTemplate template) {
    switch (this.properties.getRepetitionStrategy()) {
      case INSERT -> template.header(key, value);
      case IGNORE -> {
        if (template.headers().keySet().stream().noneMatch(k -> k.equals(key))) {
          template.header(key, value);
        }
      }
      case COVER -> {
        if (template.headers().keySet().stream().anyMatch(k -> k.equals(key))) {
          template.removeHeader(key);
        }
        template.header(key, value);
      }
      default -> {}
    }
  }
}
