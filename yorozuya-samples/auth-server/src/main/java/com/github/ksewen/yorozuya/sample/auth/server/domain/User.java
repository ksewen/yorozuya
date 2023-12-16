package com.github.ksewen.yorozuya.sample.auth.server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ksewen
 * @date 12.12.2023 21:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Column(nullable = false, columnDefinition = "VARCHAR(64)")
  private String username;

  @Column(nullable = false, columnDefinition = "VARCHAR(255)")
  private String password;
}
