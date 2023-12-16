package com.github.ksewen.yorozuya.sample.auth.server.service;

import com.github.ksewen.yorozuya.sample.auth.server.domain.Role;
import com.github.ksewen.yorozuya.sample.auth.server.domain.User;
import java.util.List;
import java.util.Optional;

/**
 * @author ksewen
 * @date 12.12.2023 21:31
 */
public interface UserService {

  Optional<User> findById(long userId);

  Optional<User> findByUsername(String username);

  User add(String username, String password, List<Role> roles);
}
