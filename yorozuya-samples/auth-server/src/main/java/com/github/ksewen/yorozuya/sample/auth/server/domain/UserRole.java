package com.github.ksewen.yorozuya.sample.auth.server.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author ksewen
 * @date 12.12.2023 21:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_role")
public class UserRole {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Column(nullable = false, columnDefinition = "BIGINT(20)")
  private Long userId;

  @Column(nullable = false, columnDefinition = "BIGINT(20)")
  private Long roleId;
}
