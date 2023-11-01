package com.github.ksewen.yorozuya.dashboard.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author ksewen
 * @date 26.10.2023 15:03
 */
@Data
@Builder
public class Application {

  private String name;

  private List<Instance> instances;
}
