package com.github.ksewen.yorozuya.starter.filter.context;

import com.github.ksewen.yorozuya.common.context.Context;
import com.github.ksewen.yorozuya.starter.filter.context.extension.ContextFilterExtension;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author ksewen
 * @date 15.10.2023 17:25
 */
@Slf4j
@RequiredArgsConstructor
public class ContextFilter implements Filter {

  private final Context context;

  private final Set<String> defaultInjectKeySet;

  private final List<ContextFilterExtension> extensions;

  private final String prefix;

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    try {
      inject(servletRequest);
      if (!CollectionUtils.isEmpty(extensions)) {
        extensions.forEach(ext -> ext.inject(servletRequest));
      }
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {
      this.context.shutdown();
    }
  }

  protected void inject(ServletRequest servletRequest) {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      boolean valid =
          StringUtils.hasLength(headerName)
              && (headerName.startsWith(prefix) || this.defaultInjectKeySet.contains(headerName));
      if (valid) {
        String value = request.getHeader(headerName);
        log.debug(
            "found service context: {}, value: {}, invoke:{}",
            headerName,
            value,
            request.getRequestURI());
        this.context.put(headerName, value);
      }
    }
  }
}
