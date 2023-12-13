package com.github.ksewen.yorozuya.auth.server.security;

import com.github.ksewen.yorozuya.auth.server.service.UserDetailsExtraDefinition;
import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import com.github.ksewen.yorozuya.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author ksewen
 * @date 12.12.2023 22:56
 */
@RequiredArgsConstructor
@Slf4j
public class ContextAuthenticationManager implements AuthenticationManager {

  private final UserDetailsService userDetailsService;

  private UserDetails getUserDetails() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal == null) {
      throw new CommonException(DefaultResultCodeEnums.UNAUTHORIZED);
    }
    String username = ((UserDetails) principal).getUsername();
    return this.userDetailsService.loadUserByUsername(username);
  }

  @Override
  public Long getUserId() {
    UserDetails userDetails = this.getUserDetails();
    if (userDetails instanceof UserDetailsExtraDefinition) {
      return ((UserDetailsExtraDefinition) userDetails).getId();
    }
    log.error(
        "To get user id by this class, please implement UserDetailsService#loadUserByUsername and return an implementation of UserDetailsExtraDefinition.");
    throw new UnsupportedOperationException("Unsupported type: " + userDetails.getClass());
  }

  @Override
  public String getUsername() {
    return this.getUserDetails().getUsername();
  }
}
