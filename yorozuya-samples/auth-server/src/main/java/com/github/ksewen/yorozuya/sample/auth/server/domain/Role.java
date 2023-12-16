package com.github.ksewen.yorozuya.sample.auth.server.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author ksewen
 * @date 12.12.2023 21:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "role", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Column(nullable = false, columnDefinition = "VARCHAR(64)")
  private String name;
}
