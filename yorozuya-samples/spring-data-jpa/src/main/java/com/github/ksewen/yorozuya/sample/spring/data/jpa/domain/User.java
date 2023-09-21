package com.github.ksewen.yorozuya.sample.spring.data.jpa.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author ksewen
 * @date 21.09.2023 10:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`user`")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Column(nullable = false, columnDefinition = "VARCHAR(64)")
  private String firstName;

  @Column(nullable = false, columnDefinition = "VARCHAR(64)")
  private String lastName;
}
