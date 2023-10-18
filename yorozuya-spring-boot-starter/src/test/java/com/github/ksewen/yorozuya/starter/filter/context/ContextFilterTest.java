package com.github.ksewen.yorozuya.starter.filter.context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.github.ksewen.yorozuya.common.constant.ContextConstants;
import com.github.ksewen.yorozuya.common.context.Context;
import com.github.ksewen.yorozuya.starter.filter.context.extension.ContextFilterExtension;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import org.junit.jupiter.api.Test;

/**
 * @author ksewen
 * @date 15.10.2023 22:46
 */
class ContextFilterTest {

  private Context context = mock(Context.class);

  private Set<String> defaultInjectKeySet = mock(Set.class);

  private List<ContextFilterExtension> extensions = mock(List.class);

  private String prefix = ContextConstants.HEADER_NAME_DEFAULT_PREFIX;

  private ContextFilter contextFilter =
      new ContextFilter(this.context, this.defaultInjectKeySet, this.extensions, this.prefix);

  @Test
  void doFilter() throws ServletException, IOException {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    FilterChain mockFilterChain = mock(FilterChain.class);
    Enumeration<String> mockEnumeration = mock(Enumeration.class);

    when(mockRequest.getHeaderNames()).thenReturn(mockEnumeration);
    when(mockEnumeration.hasMoreElements()).thenReturn(Boolean.FALSE);
    when(this.extensions.isEmpty()).thenReturn(Boolean.FALSE);
    this.contextFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

    verify(this.extensions, times(1)).forEach(any());
    verify(this.context, times(1)).shutdown();
    verify(mockFilterChain, times(1))
        .doFilter(
            argThat(r -> Objects.equals(r, mockRequest)),
            argThat(r -> Objects.equals(r, mockResponse)));
  }

  @Test
  void inject() {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);

    when(mockRequest.getHeaderNames())
        .thenReturn(
            Collections.enumeration(
                Arrays.asList(ContextConstants.HEADER_NAME_DEFAULT_PREFIX + "a", "b", "c")));
    when(this.defaultInjectKeySet.contains("c")).thenReturn(Boolean.TRUE);

    this.contextFilter.inject(mockRequest);

    verify(this.defaultInjectKeySet, times(2)).contains(anyString());
    verify(mockRequest, times(2)).getHeader(anyString());
    verify(mockRequest, times(2)).getRequestURI();
    verify(this.context, times(1)).put(ContextConstants.HEADER_NAME_DEFAULT_PREFIX + "a", null);
    verify(this.context, times(1)).put("c", null);
  }
}
