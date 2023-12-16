package com.github.ksewen.yorozuya.auth.server.dto.response;

import lombok.*;

/**
 * @author ksewen
 * @date 02.12.2023 17:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {

  private Long userId;

  private String username;

  private String token;

  private String refreshToken;
}
