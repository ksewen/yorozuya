package com.github.ksewen.yorozuya.sample.auth.server.controller;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import com.github.ksewen.yorozuya.common.exception.CommonException;
import com.github.ksewen.yorozuya.common.facade.response.Result;
import com.github.ksewen.yorozuya.sample.auth.server.constant.AuthenticationConstants;
import com.github.ksewen.yorozuya.sample.auth.server.domain.Role;
import com.github.ksewen.yorozuya.sample.auth.server.domain.User;
import com.github.ksewen.yorozuya.sample.auth.server.dto.request.UserRegisterRequest;
import com.github.ksewen.yorozuya.sample.auth.server.dto.response.UserInfoResponse;
import com.github.ksewen.yorozuya.sample.auth.server.service.RoleService;
import com.github.ksewen.yorozuya.sample.auth.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ksewen
 * @date 12.12.2023 22:36
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegisterController {

  private final UserService userService;

  private final RoleService roleService;

  private final PasswordEncoder passwordEncoder;

  @Operation(summary = "register")
  @PostMapping("/register")
  public Result<UserInfoResponse> register(@Valid @RequestBody UserRegisterRequest request) {
    List<Role> roles = this.roleService.findByNames(AuthenticationConstants.USER_ROLE_NAME);
    if (CollectionUtils.isEmpty(roles)) {
      throw new CommonException(DefaultResultCodeEnums.NOT_FOUND, "the given role name invalid");
    }
    String password = this.passwordEncoder.encode(request.getPassword());
    User registered = this.userService.add(request.getUsername(), password, roles);
    return Result.success(
        UserInfoResponse.builder()
            .id(registered.getId())
            .username(registered.getUsername())
            .build());
  }
}
