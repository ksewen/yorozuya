package com.github.ksewen.yorozuya.auth.server.token.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * @author ksewen
 * @date 04.12.2023 21:27
 */
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

  @Override
  public UsernamePasswordAuthenticationToken convert(Jwt source) {
    String username = source.getSubject();
    List<SimpleGrantedAuthority> claim =
        Optional.ofNullable(source.<List<String>>getClaim(JwtTokenProvider.AUTHORITIES_KEY))
            .map(
                list -> list.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
            .orElse(Collections.emptyList());
    String defaultPassword = "";
    return new UsernamePasswordAuthenticationToken(
        new User(username, defaultPassword, claim), null, claim);
  }
}
