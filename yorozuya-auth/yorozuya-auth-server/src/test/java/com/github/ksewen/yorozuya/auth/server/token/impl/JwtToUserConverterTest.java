package com.github.ksewen.yorozuya.auth.server.token.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.CollectionUtils;

/**
 * @author ksewen
 * @date 04.12.2023 21:47
 */
class JwtToUserConverterTest {

  private final JwtToUserConverter CONVERTER = new JwtToUserConverter();

  private final String USERNAME = "username";

  @Test
  void convert() {
    Jwt mockJwt = mock(Jwt.class);
    when(mockJwt.getSubject()).thenReturn(this.USERNAME);
    when(mockJwt.getClaim(JwtTokenProvider.AUTHORITIES_KEY))
        .thenReturn(Arrays.asList("A", "B", "C"));
    UsernamePasswordAuthenticationToken result = this.CONVERTER.convert(mockJwt);
    assertThat(result)
        .isNotNull()
        .matches(t -> this.USERNAME.equals(((User) t.getPrincipal()).getUsername()))
        .matches(t -> !CollectionUtils.isEmpty(t.getAuthorities()))
        .matches(t -> t.getAuthorities().stream().anyMatch(i -> "A".equals(i.getAuthority())))
        .matches(t -> t.getAuthorities().stream().anyMatch(i -> "B".equals(i.getAuthority())))
        .matches(t -> t.getAuthorities().stream().anyMatch(i -> "B".equals(i.getAuthority())));
  }
}
