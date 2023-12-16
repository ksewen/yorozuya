package com.github.ksewen.yorozuya.sample.auth.server.service;

import com.github.ksewen.yorozuya.sample.auth.server.domain.Role;
import com.github.ksewen.yorozuya.sample.auth.server.model.RoleModel;
import com.github.ksewen.yorozuya.sample.auth.server.model.UserModel;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author ksewen
 * @date 12.12.2023 18:49
 */
@Service
@RequiredArgsConstructor
public class DaoUserDetailsServiceImpl implements UserDetailsService {

  private final UserService userService;

  private final RoleService roleService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.userService
        .findByUsername(username)
        .map(
            u -> {
              List<Role> roles = this.roleService.findByUserId(u.getId());
              if (CollectionUtils.isEmpty(roles)) {
                return new UserModel(
                    u.getId(), u.getUsername(), u.getPassword(), Collections.emptyList());
              }
              return new UserModel(
                  u.getId(),
                  u.getUsername(),
                  u.getPassword(),
                  roles.stream()
                      .map(i -> RoleModel.builder().id(i.getId()).name(i.getName()).build())
                      .toList());
            })
        .orElseThrow(
            () ->
                new UsernameNotFoundException(
                    String.format("None user found with username '%s'.", username)));
  }
}
