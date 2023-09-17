package com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ksewen
 * @date 08.09.2023 11:37
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstanceResponse {

  private String instance;
}
