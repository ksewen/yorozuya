package com.github.ksewen.yorozuya.sample.auth.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author ksewen
 * @date 12.12.2023 21:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleModel implements GrantedAuthority {

  private Long id;

  private String name;

  @Override
  public String getAuthority() {
    return this.name;
  }
}
