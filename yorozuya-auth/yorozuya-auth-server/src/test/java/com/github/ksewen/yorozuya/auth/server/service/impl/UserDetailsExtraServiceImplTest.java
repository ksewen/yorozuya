package com.github.ksewen.yorozuya.auth.server.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.github.ksewen.yorozuya.auth.server.service.UserDetailsExtraDefinition;
import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author ksewen
 * @date 12.12.2023 18:56
 */
@SpringBootTest(classes = UserDetailsExtraServiceImpl.class)
class UserDetailsExtraServiceImplTest {

  @Autowired private UserDetailsExtraServiceImpl userDetailsExtraService;

  @Test
  void getUserId() {
    long userId = 1L;
    String username = "username";
    String password = "password";
    User user = User.builder().id(userId).username(username).password(password).build();

    Long result = this.userDetailsExtraService.getUserId(user);
    assertThat(result).isNotNull().isEqualTo(userId);
  }

  @Test
  void getNull() {
    UserDetails user = mock(UserDetails.class);

    Long result = this.userDetailsExtraService.getUserId(user);
    assertThat(result).isNull();
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  private static class User implements UserDetails, UserDetailsExtraDefinition {

    private Long id;
    private String username;
    private String password;

    @Override
    public Long getId() {
      return this.id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return Collections.emptyList();
    }

    @Override
    public String getPassword() {
      return this.password;
    }

    @Override
    public String getUsername() {
      return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
      return true;
    }

    @Override
    public boolean isAccountNonLocked() {
      return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
      return true;
    }

    @Override
    public boolean isEnabled() {
      return true;
    }
  }
}
