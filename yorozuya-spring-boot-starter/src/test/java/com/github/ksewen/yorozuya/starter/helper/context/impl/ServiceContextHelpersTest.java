package com.github.ksewen.yorozuya.starter.helper.context.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import com.github.ksewen.yorozuya.common.constant.ContextConstants;
import com.github.ksewen.yorozuya.common.context.Context;
import com.github.ksewen.yorozuya.starter.configuration.context.ServiceContextAutoConfiguration;
import com.github.ksewen.yorozuya.starter.helper.context.ContextHelpers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.StringUtils;

/**
 * @author ksewen
 * @date 15.10.2023 22:31
 */
@SpringBootTest(classes = ServiceContextAutoConfiguration.class)
class ServiceContextHelpersTest {

  @Autowired private ContextHelpers contextHelpers;

  @MockBean private Context context;

  private final String MOCK_KEY = "key";

  private final String MOCK_VALUE = "value";

  @Test
  void hasKey() {
    when(this.context.contains(
            argThat(
                k ->
                    StringUtils.hasLength(k)
                        && k.startsWith(ContextConstants.HEADER_NAME_DEFAULT_PREFIX))))
        .thenReturn(Boolean.TRUE);
    assertThat(this.contextHelpers.hasKey(this.MOCK_KEY)).isTrue();
  }

  @Test
  void get() {
    when(this.context.get(
            argThat(
                k ->
                    StringUtils.hasLength(k)
                        && k.startsWith(ContextConstants.HEADER_NAME_DEFAULT_PREFIX))))
        .thenReturn(this.MOCK_VALUE);
    assertThat(this.contextHelpers.get(this.MOCK_KEY)).isEqualTo(this.MOCK_VALUE);
  }

  @Test
  void put() {
    this.contextHelpers.put(this.MOCK_KEY, this.MOCK_VALUE);
    verify(this.context, times(1))
        .put(ContextConstants.HEADER_NAME_DEFAULT_PREFIX + this.MOCK_KEY, this.MOCK_VALUE);
  }

  @Test
  void remove() {
    this.contextHelpers.remove(this.MOCK_KEY);
    verify(this.context, times(1))
        .remove(ContextConstants.HEADER_NAME_DEFAULT_PREFIX + this.MOCK_KEY);
  }
}
