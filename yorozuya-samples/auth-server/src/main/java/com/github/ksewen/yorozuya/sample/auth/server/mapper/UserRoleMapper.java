package com.github.ksewen.yorozuya.sample.auth.server.mapper;

import com.github.ksewen.yorozuya.sample.auth.server.domain.UserRole;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ksewen
 * @date 12.12.2023 22:23
 */
@Repository
public interface UserRoleMapper extends JpaRepository<UserRole, Long> {

  List<UserRole> findByUserId(Long userId);
}
