package com.github.ksewen.yorozuya.starter.configuration.context.interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.github.ksewen.yorozuya.common.constant.ContextConstants;
import com.github.ksewen.yorozuya.starter.configuration.context.ServiceContextProperties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author ksewen
 * @date 17.10.2023 12:17
 */
@SpringBootTest(classes = ContextKeyValidChecker.class)
class ContextKeyValidCheckerTest {

  @Autowired private ContextKeyValidChecker checker;

  @MockBean private ServiceContextProperties properties;

  private final String KEY = "key";

  @Test
  void validWithPrefixAndEnable() {
    when(this.properties.isTransferWithPrefix()).thenReturn(Boolean.TRUE);
    when(this.properties.getHeaderPrefix()).thenReturn(ContextConstants.HEADER_NAME_DEFAULT_PREFIX);

    boolean result = this.checker.valid(ContextConstants.HEADER_NAME_DEFAULT_PREFIX + this.KEY);
    assertThat(result).isTrue();
  }

  @Test
  void validWithWhiteListAndMatch() {
    when(this.properties.isEnableWhiteList()).thenReturn(Boolean.TRUE);
    when(this.properties.getLimitSet()).thenReturn(Stream.of(this.KEY).collect(Collectors.toSet()));

    boolean result = this.checker.valid(this.KEY);
    assertThat(result).isTrue();
  }

  @Test
  void invalidWithWhiteListAndNotMatch() {
    when(this.properties.isEnableWhiteList()).thenReturn(Boolean.TRUE);
    when(this.properties.getLimitSet()).thenReturn(Stream.of("key1").collect(Collectors.toSet()));

    boolean result = this.checker.valid(this.KEY);
    assertThat(result).isFalse();
  }

  @Test
  void validWithBlackListAndNotMatch() {
    when(this.properties.isEnableWhiteList()).thenReturn(Boolean.FALSE);
    when(this.properties.getLimitSet()).thenReturn(Stream.of("key1").collect(Collectors.toSet()));

    boolean result = this.checker.valid(this.KEY);
    assertThat(result).isTrue();
  }

  @Test
  void invalidWithBlackListAndMatch() {
    when(this.properties.isEnableWhiteList()).thenReturn(Boolean.FALSE);
    when(this.properties.getLimitSet()).thenReturn(Stream.of(this.KEY).collect(Collectors.toSet()));

    boolean result = this.checker.valid(this.KEY);
    assertThat(result).isFalse();
  }
}
