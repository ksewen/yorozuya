package com.github.ksewen.yorozuya.starter.configuration.context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.github.ksewen.yorozuya.common.constant.ContextConstants;
import com.github.ksewen.yorozuya.common.context.Context;
import com.github.ksewen.yorozuya.starter.configuration.context.interceptor.RepetitionStrategyEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author ksewen
 * @date 15.10.2023 22:06
 */
@SpringBootTest(
    classes = ServiceContextAutoConfiguration.class,
    properties = {
      "common.context.service.transfer-with-prefix=false",
      "common.context.service.default_inject_key_set=test1, test2",
      "common.context.service.enable-white-list=false",
      "common.context.service.repetition-strategy=INSERT"
    })
class ServiceContextPropertiesTest {

  @Autowired private ServiceContextProperties properties;

  @SuppressWarnings("unused")
  @MockBean
  private Context context;

  @Test
  void getHeaderPrefix() {
    assertThat(this.properties.getHeaderPrefix())
        .isEqualTo(ContextConstants.HEADER_NAME_DEFAULT_PREFIX);
  }

  @Test
  void transferWithPrefix() {
    assertThat(this.properties.isTransferWithPrefix()).isFalse();
  }

  @Test
  void getDefaultInjectKeySet() {
    assertThat(this.properties.getDefaultInjectKeySet())
        .isNotNull()
        .hasSize(2)
        .matches(s -> s.contains("test1"))
        .matches(s -> s.contains("test2"));
  }

  @Test
  void enableWhiteList() {
    assertThat(this.properties.isEnableWhiteList()).isFalse();
  }

  @Test
  void limitSet() {
    assertThat(this.properties.getLimitSet()).isNotNull().hasSize(0);
  }

  @Test
  void repetitionStrategy() {
    assertThat(this.properties.getRepetitionStrategy()).isEqualTo(RepetitionStrategyEnum.INSERT);
  }
}
