package com.github.ksewen.yorozuya.sample.auth.server.service;

import com.github.ksewen.yorozuya.sample.auth.server.domain.UserRole;
import java.util.List;

/**
 * @author ksewen
 * @date 12.12.2023 22:21
 */
public interface UserRoleService {

  List<UserRole> findByUserId(Long userId);

  UserRole saveAndFlush(UserRole userRole);

  List<UserRole> saveAllAndFlush(List<UserRole> userRoles);
}
