package com.github.ksewen.yorozuya.auth.server.controller;

import com.github.ksewen.yorozuya.auth.server.dto.request.LoginRequest;
import com.github.ksewen.yorozuya.auth.server.dto.response.TokenResponse;
import com.github.ksewen.yorozuya.auth.server.service.UserDetailsExtraService;
import com.github.ksewen.yorozuya.auth.server.token.TokenProvider;
import com.github.ksewen.yorozuya.common.facade.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

/**
 * @author ksewen
 * @date 02.12.2023 16:58
 */
@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationProvider authenticationProvider;

  private final UserDetailsService userDetailsService;

  private final UserDetailsExtraService<Long, UserDetails> userDetailsExtraService;

  private final TokenProvider tokenProvider;

  @Operation(summary = "login")
  @PostMapping(path = "/login")
  public Result<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    UsernamePasswordAuthenticationToken upToken =
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(), loginRequest.getPassword());
    final Authentication authentication = this.authenticationProvider.authenticate(upToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    final UserDetails userDetails =
        this.userDetailsService.loadUserByUsername(loginRequest.getUsername());
    final String token = this.tokenProvider.generateToken(authentication);
    final String refreshToken = this.tokenProvider.generateRefreshToken(authentication);
    return Result.success(
        TokenResponse.builder()
            .userId(this.userDetailsExtraService.getUserId(userDetails))
            .username(userDetails.getUsername())
            .token(token)
            .refreshToken(refreshToken)
            .build());
  }
}
