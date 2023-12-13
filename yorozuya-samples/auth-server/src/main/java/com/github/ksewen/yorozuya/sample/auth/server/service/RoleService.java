package com.github.ksewen.yorozuya.sample.auth.server.service;

import com.github.ksewen.yorozuya.sample.auth.server.domain.Role;
import java.util.List;

/**
 * @author ksewen
 * @date 12.12.2023 22:10
 */
public interface RoleService {

  List<Role> findByUserId(Long userId);

  List<Role> findByNames(String... name);
}
