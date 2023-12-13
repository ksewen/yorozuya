package com.github.ksewen.yorozuya.sample.auth.server.model;

import com.github.ksewen.yorozuya.auth.server.service.UserDetailsExtraDefinition;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @author ksewen
 * @date 12.12.2023 18:44
 */
public class UserModel extends User implements UserDetailsExtraDefinition {

  private Long id;

  public UserModel(
      Long id,
      String username,
      String password,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.id = id;
  }

  public UserModel(
      Long id,
      String username,
      String password,
      boolean enabled,
      boolean accountNonExpired,
      boolean credentialsNonExpired,
      boolean accountNonLocked,
      Collection<? extends GrantedAuthority> authorities) {
    super(
        username,
        password,
        enabled,
        accountNonExpired,
        credentialsNonExpired,
        accountNonLocked,
        authorities);
    this.id = id;
  }

  @Override
  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
