package com.github.ksewen.yorozuya.starter.configuration.redis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ksewen
 * @date 24.09.2023 14:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

  private Long id;

  private String name;

  private Double price;
}
