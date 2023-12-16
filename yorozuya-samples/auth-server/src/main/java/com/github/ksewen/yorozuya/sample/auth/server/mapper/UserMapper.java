package com.github.ksewen.yorozuya.sample.auth.server.mapper;

import com.github.ksewen.yorozuya.sample.auth.server.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ksewen
 * @date 12.12.2023 22:01
 */
@Repository
public interface UserMapper extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);
}
