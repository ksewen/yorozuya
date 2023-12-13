package com.github.ksewen.yorozuya.sample.auth.server.controller;

import com.github.ksewen.yorozuya.auth.server.security.AuthenticationManager;
import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import com.github.ksewen.yorozuya.common.exception.CommonException;
import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.auth.server.domain.User;
import com.github.ksewen.yorozuya.sample.auth.server.dto.response.UserInfoResponse;
import com.github.ksewen.yorozuya.sample.auth.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ksewen
 * @date 12.12.2023 22:09
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "jwt-auth")
public class UserController {

  private final UserService userService;

  private final AuthenticationManager authentication;

  @Operation(summary = "show detail of user")
  @GetMapping("/detail")
  public Result<UserInfoResponse> detail() {
    User user =
        this.userService
            .findById(this.authentication.getUserId())
            .orElseThrow(() -> new CommonException(DefaultResultCodeEnums.NOT_FOUND));
    return Result.success(
        UserInfoResponse.builder().id(user.getId()).username(user.getUsername()).build());
  }
}
