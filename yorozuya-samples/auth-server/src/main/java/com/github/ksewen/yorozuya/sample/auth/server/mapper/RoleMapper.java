package com.github.ksewen.yorozuya.sample.auth.server.mapper;

import com.github.ksewen.yorozuya.sample.auth.server.domain.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author ksewen
 * @date 12.12.2023 22:07
 */
@Repository
public interface RoleMapper extends JpaRepository<Role, Long> {

  @Query(
      "SELECT new Role(r.id, r.name) FROM Role r WHERE r.id in (SELECT ur.roleId FROM UserRole ur WHERE ur.userId = ?1)")
  List<Role> findByUserId(Long userId);

  List<Role> findByNameIn(List<String> names);
}
