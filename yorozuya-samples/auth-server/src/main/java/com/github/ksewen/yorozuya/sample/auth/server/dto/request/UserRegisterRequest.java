package com.github.ksewen.yorozuya.sample.auth.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ksewen
 * @date 12.12.2023 22:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {

  @NotBlank(message = "{auth.username.null}")
  private String username;

  @NotBlank(message = "{auth.password.null}")
  private String password;
}
