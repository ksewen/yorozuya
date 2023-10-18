package com.github.ksewen.yorozuya.starter.configuration.context.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

import com.github.ksewen.yorozuya.common.context.Context;
import com.github.ksewen.yorozuya.starter.configuration.context.ServiceContextProperties;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;

/**
 * @author ksewen
 * @date 17.10.2023 12:29
 */
@SpringBootTest(classes = ContextClientHttpRequestInterceptor.class)
class ContextClientHttpRequestInterceptorTest {

  @Autowired private ContextClientHttpRequestInterceptor contextClientHttpRequestInterceptor;

  @MockBean private Context context;

  @MockBean private ServiceContextProperties properties;

  private final String KEY_1 = "key1";

  private final String VALUE_1 = "value1";

  @Test
  void intercept() throws IOException {
    HttpRequest mockRequest = mock(HttpRequest.class);
    ClientHttpRequestExecution mockExecution = mock(ClientHttpRequestExecution.class);
    HttpHeaders mockHeaders = mock(HttpHeaders.class);
    Map<String, String> map =
        Stream.of(new AbstractMap.SimpleEntry<>(this.KEY_1, this.VALUE_1))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    when(mockRequest.getHeaders()).thenReturn(mockHeaders);
    when(this.properties.getRepetitionStrategy()).thenReturn(RepetitionStrategyEnum.IGNORE);
    when(this.properties.isEnableWhiteList()).thenReturn(Boolean.FALSE);
    when(this.properties.getLimitSet()).thenReturn(new HashSet<>());
    when(this.context.getContext()).thenReturn(map);

    this.contextClientHttpRequestInterceptor.intercept(mockRequest, new byte[] {}, mockExecution);

    verify(this.context, times(1)).getContext();
    verify(mockRequest, times(1)).getHeaders();
    verify(mockHeaders, times(1)).add(this.KEY_1, this.VALUE_1);
    verify(mockHeaders, never()).remove(anyString());
  }

  @Test
  void interceptWithEmptyContext() throws IOException {
    HttpRequest mockRequest = mock(HttpRequest.class);
    ClientHttpRequestExecution mockExecution = mock(ClientHttpRequestExecution.class);
    HttpHeaders mockHeaders = mock(HttpHeaders.class);

    when(mockRequest.getHeaders()).thenReturn(mockHeaders);
    when(this.context.getContext()).thenReturn(new HashMap<>());

    this.contextClientHttpRequestInterceptor.intercept(mockRequest, new byte[] {}, mockExecution);

    verify(this.context, times(1)).getContext();
    verify(mockRequest, never()).getHeaders();
    verify(mockHeaders, never()).add(anyString(), anyString());
  }

  @Test
  void processWithStrategyInsert() {
    HttpHeaders mockHeaders = mock(HttpHeaders.class);

    when(this.properties.getRepetitionStrategy()).thenReturn(RepetitionStrategyEnum.INSERT);

    this.contextClientHttpRequestInterceptor.process(this.KEY_1, this.VALUE_1, mockHeaders);

    verify(mockHeaders, never()).keySet();
    verify(mockHeaders, times(1)).add(this.KEY_1, this.VALUE_1);
  }

  @Test
  void processWithStrategyIgnoreAndExist() {
    HttpHeaders mockHeaders = mock(HttpHeaders.class);

    when(this.properties.getRepetitionStrategy()).thenReturn(RepetitionStrategyEnum.IGNORE);
    when(mockHeaders.keySet()).thenReturn(new HashSet<>());

    this.contextClientHttpRequestInterceptor.process(this.KEY_1, this.VALUE_1, mockHeaders);

    verify(mockHeaders, times(1)).keySet();
    verify(mockHeaders, times(1)).add(this.KEY_1, this.VALUE_1);
  }

  @Test
  void processWithStrategyIgnoreAndNotExist() {
    HttpHeaders mockHeaders = mock(HttpHeaders.class);

    when(this.properties.getRepetitionStrategy()).thenReturn(RepetitionStrategyEnum.IGNORE);
    when(mockHeaders.keySet()).thenReturn(Stream.of(this.KEY_1).collect(Collectors.toSet()));

    this.contextClientHttpRequestInterceptor.process(this.KEY_1, this.VALUE_1, mockHeaders);

    verify(mockHeaders, times(1)).keySet();
    verify(mockHeaders, never()).add(this.KEY_1, this.VALUE_1);
  }

  @Test
  void processWithStrategyCoverAndExist() {
    HttpHeaders mockHeaders = mock(HttpHeaders.class);

    when(this.properties.getRepetitionStrategy()).thenReturn(RepetitionStrategyEnum.COVER);
    when(mockHeaders.keySet()).thenReturn(Stream.of(this.KEY_1).collect(Collectors.toSet()));

    this.contextClientHttpRequestInterceptor.process(this.KEY_1, this.VALUE_1, mockHeaders);

    verify(mockHeaders, times(1)).keySet();
    verify(mockHeaders, times(1)).remove(this.KEY_1);
    verify(mockHeaders, times(1)).add(this.KEY_1, this.VALUE_1);
  }

  @Test
  void processWithStrategyCoverAndNotExist() {
    HttpHeaders mockHeaders = mock(HttpHeaders.class);

    when(this.properties.getRepetitionStrategy()).thenReturn(RepetitionStrategyEnum.COVER);
    when(mockHeaders.keySet()).thenReturn(new HashSet<>());

    this.contextClientHttpRequestInterceptor.process(this.KEY_1, this.VALUE_1, mockHeaders);

    verify(mockHeaders, times(1)).keySet();
    verify(mockHeaders, never()).remove(this.KEY_1);
    verify(mockHeaders, times(1)).add(this.KEY_1, this.VALUE_1);
  }
}
