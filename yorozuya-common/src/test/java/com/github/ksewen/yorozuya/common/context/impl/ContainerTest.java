package com.github.ksewen.yorozuya.common.context.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

/**
 * @author ksewen
 * @date 13.10.2023 22:14
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContainerTest {

  private ServiceContext.Container container = new ServiceContext.Container();

  @BeforeAll
  void beforeAll() {
    this.container.put("testContext", "test");
    this.container.put("testBlankValue", "");
    this.container.put("testRemove", "remove");
  }

  @Test
  void clear() {
    assertThat(this.container).isNotNull().matches(c -> c.getContext().size() == 3);
    this.container.clear();
    assertThat(this.container).isNotNull().matches(c -> c.getContext().size() == 0);
  }
}
