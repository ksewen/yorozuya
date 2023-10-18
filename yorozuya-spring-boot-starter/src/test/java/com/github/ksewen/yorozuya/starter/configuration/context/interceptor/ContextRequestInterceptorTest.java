package com.github.ksewen.yorozuya.starter.configuration.context.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.github.ksewen.yorozuya.common.context.Context;
import com.github.ksewen.yorozuya.starter.configuration.context.ServiceContextProperties;
import feign.RequestTemplate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author ksewen
 * @date 17.10.2023 11:28
 */
@SpringBootTest(classes = ContextRequestInterceptor.class)
class ContextRequestInterceptorTest {

  @Autowired private ContextRequestInterceptor contextRequestInterceptor;

  @MockBean private Context context;

  @MockBean private ServiceContextProperties properties;

  private final String KEY_1 = "key1";

  private final String VALUE_1 = "value1";

  @Test
  void apply() {
    RequestTemplate mockRequestTemplate = mock(RequestTemplate.class);
    Map<String, String> map =
        Stream.of(new AbstractMap.SimpleEntry<>(this.KEY_1, this.VALUE_1))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    when(this.properties.getRepetitionStrategy()).thenReturn(RepetitionStrategyEnum.IGNORE);
    when(this.properties.isEnableWhiteList()).thenReturn(Boolean.FALSE);
    when(this.properties.getLimitSet()).thenReturn(new HashSet<>());
    when(this.context.getContext()).thenReturn(map);

    this.contextRequestInterceptor.apply(mockRequestTemplate);

    verify(this.context, times(1)).getContext();
    verify(mockRequestTemplate, times(1)).headers();
    verify(mockRequestTemplate, times(1)).header(this.KEY_1, this.VALUE_1);
    verify(mockRequestTemplate, never()).removeHeader(anyString());
  }

  @Test
  void applyWithEmptyContext() {
    RequestTemplate mockRequestTemplate = mock(RequestTemplate.class);

    when(this.properties.getRepetitionStrategy()).thenReturn(RepetitionStrategyEnum.IGNORE);
    when(this.properties.isEnableWhiteList()).thenReturn(Boolean.FALSE);
    when(this.properties.getLimitSet()).thenReturn(new HashSet<>());
    when(this.context.getContext()).thenReturn(new HashMap<>());

    this.contextRequestInterceptor.apply(mockRequestTemplate);

    verify(this.context, times(1)).getContext();
    verify(mockRequestTemplate, never()).header(anyString(), anyString());
  }

  @Test
  void processWithStrategyInsert() {
    RequestTemplate mockRequestTemplate = mock(RequestTemplate.class);

    when(this.properties.getRepetitionStrategy()).thenReturn(RepetitionStrategyEnum.INSERT);

    this.contextRequestInterceptor.process(this.KEY_1, this.VALUE_1, mockRequestTemplate);

    verify(mockRequestTemplate, never()).headers();
    verify(mockRequestTemplate, times(1)).header(this.KEY_1, this.VALUE_1);
  }

  @Test
  void processWithStrategyIgnoreAndExist() {
    RequestTemplate mockRequestTemplate = mock(RequestTemplate.class);
    Map<String, Collection<String>> mockHeaders = mock(Map.class);

    when(this.properties.getRepetitionStrategy()).thenReturn(RepetitionStrategyEnum.IGNORE);
    when(mockRequestTemplate.headers()).thenReturn(mockHeaders);
    when(mockHeaders.keySet()).thenReturn(new HashSet<>());

    this.contextRequestInterceptor.process(this.KEY_1, this.VALUE_1, mockRequestTemplate);

    verify(mockRequestTemplate, times(1)).headers();
    verify(mockRequestTemplate, times(1)).header(this.KEY_1, this.VALUE_1);
  }

  @Test
  void processWithStrategyIgnoreAndNotExist() {
    RequestTemplate mockRequestTemplate = mock(RequestTemplate.class);
    Map<String, Collection<String>> mockHeaders = mock(Map.class);

    when(this.properties.getRepetitionStrategy()).thenReturn(RepetitionStrategyEnum.IGNORE);
    when(mockRequestTemplate.headers()).thenReturn(mockHeaders);
    when(mockHeaders.keySet()).thenReturn(Stream.of(this.KEY_1).collect(Collectors.toSet()));

    this.contextRequestInterceptor.process(this.KEY_1, this.VALUE_1, mockRequestTemplate);

    verify(mockRequestTemplate, times(1)).headers();
    verify(mockRequestTemplate, never()).header(this.KEY_1, this.VALUE_1);
  }

  @Test
  void processWithStrategyCoverAndExist() {
    RequestTemplate mockRequestTemplate = mock(RequestTemplate.class);
    Map<String, Collection<String>> mockHeaders = mock(Map.class);

    when(this.properties.getRepetitionStrategy()).thenReturn(RepetitionStrategyEnum.COVER);
    when(mockRequestTemplate.headers()).thenReturn(mockHeaders);
    when(mockHeaders.keySet()).thenReturn(Stream.of(this.KEY_1).collect(Collectors.toSet()));

    this.contextRequestInterceptor.process(this.KEY_1, this.VALUE_1, mockRequestTemplate);

    verify(mockRequestTemplate, times(1)).headers();
    verify(mockRequestTemplate, times(1)).removeHeader(this.KEY_1);
    verify(mockRequestTemplate, times(1)).header(this.KEY_1, this.VALUE_1);
  }

  @Test
  void processWithStrategyCoverAndNotExist() {
    RequestTemplate mockRequestTemplate = mock(RequestTemplate.class);
    Map<String, Collection<String>> mockHeaders = mock(Map.class);

    when(this.properties.getRepetitionStrategy()).thenReturn(RepetitionStrategyEnum.COVER);
    when(mockRequestTemplate.headers()).thenReturn(mockHeaders);
    when(mockHeaders.keySet()).thenReturn(new HashSet<>());

    this.contextRequestInterceptor.process(this.KEY_1, this.VALUE_1, mockRequestTemplate);

    verify(mockRequestTemplate, times(1)).headers();
    verify(mockRequestTemplate, never()).removeHeader(this.KEY_1);
    verify(mockRequestTemplate, times(1)).header(this.KEY_1, this.VALUE_1);
  }
}
