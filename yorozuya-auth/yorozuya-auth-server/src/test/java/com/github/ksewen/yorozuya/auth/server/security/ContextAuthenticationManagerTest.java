package com.github.ksewen.yorozuya.auth.server.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.ksewen.yorozuya.auth.server.service.UserDetailsExtraDefinition;
import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import com.github.ksewen.yorozuya.common.exception.CommonException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

/**
 * @author ksewen
 * @date 13.12.2023 16:49
 */
@SpringBootTest(classes = ContextAuthenticationManager.class)
class ContextAuthenticationManagerTest {

  @Autowired private AuthenticationManager authenticationManager;

  @MockBean private UserDetailsService userDetailsService;

  private final Long USER_ID = 1L;

  private final String USERNAME = "username";

  private final List<SimpleGrantedAuthority> AUTHORITIES =
      Arrays.asList(
          new SimpleGrantedAuthority("A"),
          new SimpleGrantedAuthority("B"),
          new SimpleGrantedAuthority("C"));

  @Test
  void getUserId() {
    Authentication mockAuthentication = mock(Authentication.class);
    UserDetails mockUserDetails = mock(UserDetails.class);
    SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
    when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
    when(mockUserDetails.getUsername()).thenReturn(this.USERNAME);
    when(this.userDetailsService.loadUserByUsername(this.USERNAME))
        .thenReturn(MockUser.builder().id(this.USER_ID).username(this.USERNAME).build());

    Long result = this.authenticationManager.getUserId();
    assertThat(result).isNotNull().isEqualTo(this.USER_ID);
    SecurityContextHolder.clearContext();
  }

  @Test
  void getUserIdWheUnauthorized() {
    Authentication mockAuthentication = mock(Authentication.class);
    SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
    when(mockAuthentication.getPrincipal()).thenReturn(null);
    CommonException exception =
        assertThrows(CommonException.class, () -> this.authenticationManager.getUserId());
    assertThat(exception)
        .isNotNull()
        .matches(e -> DefaultResultCodeEnums.UNAUTHORIZED.equals(e.getCode()))
        .matches(e -> DefaultResultCodeEnums.UNAUTHORIZED.getMessage().equals(e.getMessage()));
    SecurityContextHolder.clearContext();
  }

  @Test
  void getUserIdUnsupported() {
    Authentication mockAuthentication = mock(Authentication.class);
    UserDetails mockUserDetails = mock(UserDetails.class);
    SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
    when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
    when(mockUserDetails.getUsername()).thenReturn(this.USERNAME);
    when(this.userDetailsService.loadUserByUsername(this.USERNAME)).thenReturn(mockUserDetails);

    UnsupportedOperationException exception =
        assertThrows(
            UnsupportedOperationException.class, () -> this.authenticationManager.getUserId());
    assertThat(exception).isNotNull().matches(e -> StringUtils.hasLength(e.getMessage()));
    SecurityContextHolder.clearContext();
  }

  @Test
  void getUsername() {
    Authentication mockAuthentication = mock(Authentication.class);
    UserDetails mockUserDetails = mock(UserDetails.class);
    SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
    when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
    when(mockUserDetails.getUsername()).thenReturn(this.USERNAME);
    when(this.userDetailsService.loadUserByUsername(this.USERNAME))
        .thenReturn(MockUser.builder().id(this.USER_ID).username(this.USERNAME).build());

    String result = this.authenticationManager.getUsername();
    assertThat(result).isNotNull().isEqualTo(this.USERNAME);
    SecurityContextHolder.clearContext();
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static class MockUser implements UserDetails, UserDetailsExtraDefinition {

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
