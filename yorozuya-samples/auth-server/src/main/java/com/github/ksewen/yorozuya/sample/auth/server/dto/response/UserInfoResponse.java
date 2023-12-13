package com.github.ksewen.yorozuya.sample.auth.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ksewen
 * @date 12.12.2023 22:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponse {

  private Long id;

  private String username;
}
