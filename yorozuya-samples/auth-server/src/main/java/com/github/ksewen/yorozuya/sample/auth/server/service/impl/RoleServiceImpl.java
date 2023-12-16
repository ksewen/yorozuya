package com.github.ksewen.yorozuya.sample.auth.server.service.impl;

import com.github.ksewen.yorozuya.sample.auth.server.domain.Role;
import com.github.ksewen.yorozuya.sample.auth.server.mapper.RoleMapper;
import com.github.ksewen.yorozuya.sample.auth.server.service.RoleService;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author ksewen
 * @date 12.12.2023 22:05
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final RoleMapper roleMapper;

  @Override
  public List<Role> findByUserId(Long userId) {
    return this.roleMapper.findByUserId(userId);
  }

  @Override
  public List<Role> findByNames(String... name) {
    return this.roleMapper.findByNameIn(Arrays.asList(name));
  }
}
