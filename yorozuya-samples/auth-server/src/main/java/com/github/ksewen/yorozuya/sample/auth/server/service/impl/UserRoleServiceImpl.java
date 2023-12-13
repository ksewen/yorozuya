package com.github.ksewen.yorozuya.sample.auth.server.service.impl;

import com.github.ksewen.yorozuya.sample.auth.server.domain.UserRole;
import com.github.ksewen.yorozuya.sample.auth.server.mapper.UserRoleMapper;
import com.github.ksewen.yorozuya.sample.auth.server.service.UserRoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author ksewen
 * @date 12.12.2023 22:23
 */
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

  private final UserRoleMapper userRoleMapper;

  @Override
  public List<UserRole> findByUserId(Long userId) {
    return this.userRoleMapper.findByUserId(userId);
  }

  @Override
  public UserRole saveAndFlush(UserRole userRole) {
    return this.userRoleMapper.saveAndFlush(userRole);
  }

  @Override
  public List<UserRole> saveAllAndFlush(List<UserRole> userRoles) {
    return this.userRoleMapper.saveAllAndFlush(userRoles);
  }
}
