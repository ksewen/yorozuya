package com.github.ksewen.yorozuya.auth.server.service.impl;

import com.github.ksewen.yorozuya.auth.server.service.UserDetailsExtraDefinition;
import com.github.ksewen.yorozuya.auth.server.service.UserDetailsExtraService;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author ksewen
 * @date 12.12.2023 18:26
 */
public class UserDetailsExtraServiceImpl implements UserDetailsExtraService<Long, UserDetails> {

  @Override
  public Long getUserId(UserDetails userDetails) {
    if (userDetails instanceof UserDetailsExtraDefinition) {
      return ((UserDetailsExtraDefinition) userDetails).getId();
    }
    return null;
  }
}
