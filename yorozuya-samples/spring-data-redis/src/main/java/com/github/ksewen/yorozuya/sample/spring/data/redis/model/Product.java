package com.github.ksewen.yorozuya.sample.spring.data.redis.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author ksewen
 * @date 22.09.2023 11:41
 */
@Data
@RedisHash("product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product implements Serializable {

  @Id private Long id;

  private String name;

  private double price;
}
