package com.github.ksewen.yorozuya.starter.configuration.context.interceptor;

import com.github.ksewen.yorozuya.common.context.Context;
import com.github.ksewen.yorozuya.starter.configuration.context.ServiceContextProperties;
import com.github.ksewen.yorozuya.starter.configuration.http.client.interceptor.CustomClientHttpRequestInterceptor;
import java.io.IOException;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.CollectionUtils;

/**
 * @author ksewen
 * @date 17.10.2023 12:02
 */
public class ContextClientHttpRequestInterceptor extends ContextKeyValidChecker
    implements CustomClientHttpRequestInterceptor {

  private final Context context;

  private final ServiceContextProperties properties;

  public ContextClientHttpRequestInterceptor(Context context, ServiceContextProperties properties) {
    super(properties);
    this.context = context;
    this.properties = properties;
  }

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    Map<String, String> map = context.getContext();
    if (!CollectionUtils.isEmpty(map)) {
      map.forEach(
          (key, value) -> {
            if (this.valid(key)) {
              this.process(key, value, request.getHeaders());
            }
          });
    }

    ClientHttpResponse response = execution.execute(request, body);
    return response;
  }

  protected void process(String key, String value, HttpHeaders headers) {
    switch (this.properties.getRepetitionStrategy()) {
      case INSERT -> headers.add(key, value);
      case IGNORE -> {
        if (headers.keySet().stream().noneMatch(k -> k.equals(key))) {
          headers.add(key, value);
        }
      }
      case COVER -> {
        if (headers.keySet().stream().anyMatch(k -> k.equals(key))) {
          headers.remove(key);
        }
        headers.add(key, value);
      }
      default -> {}
    }
  }
}
