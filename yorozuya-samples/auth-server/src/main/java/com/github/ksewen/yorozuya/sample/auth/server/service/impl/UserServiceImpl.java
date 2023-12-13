package com.github.ksewen.yorozuya.sample.auth.server.service.impl;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import com.github.ksewen.yorozuya.common.exception.CommonException;
import com.github.ksewen.yorozuya.sample.auth.server.domain.Role;
import com.github.ksewen.yorozuya.sample.auth.server.domain.User;
import com.github.ksewen.yorozuya.sample.auth.server.domain.UserRole;
import com.github.ksewen.yorozuya.sample.auth.server.mapper.UserMapper;
import com.github.ksewen.yorozuya.sample.auth.server.service.UserRoleService;
import com.github.ksewen.yorozuya.sample.auth.server.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author ksewen
 * @date 12.12.2023 21:33
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;

  private final UserRoleService userRoleService;

  @Override
  public Optional<User> findById(long userId) {
    return this.userMapper.findById(userId);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return this.userMapper.findByUsername(username);
  }

  @Override
  public User add(String username, String password, List<Role> roles) {
    Optional<User> exist = this.userMapper.findByUsername(username);
    if (exist.isPresent()) {
      throw new CommonException(DefaultResultCodeEnums.ALREADY_EXIST);
    }
    User user = User.builder().username(username).password(password).build();
    User result = this.userMapper.saveAndFlush(user);

    List<UserRole> userRoles = new ArrayList<>();
    for (Role role : roles) {
      UserRole userRole = new UserRole();
      userRole.setUserId(result.getId());
      userRole.setRoleId(role.getId());
      userRoles.add(userRole);
    }
    this.userRoleService.saveAllAndFlush(userRoles);
    return result;
  }
}
