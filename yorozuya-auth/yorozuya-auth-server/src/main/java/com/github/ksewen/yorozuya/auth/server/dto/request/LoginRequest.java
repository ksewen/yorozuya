package com.github.ksewen.yorozuya.auth.server.dto.request;

import lombok.*;

/**
 * @author ksewen
 * @date 02.12.2023 17:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

  String username;

  String password;
}
