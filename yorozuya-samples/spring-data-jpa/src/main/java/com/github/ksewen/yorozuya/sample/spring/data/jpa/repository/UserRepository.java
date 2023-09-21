package com.github.ksewen.yorozuya.sample.spring.data.jpa.repository;

import com.github.ksewen.yorozuya.sample.spring.data.jpa.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ksewen
 * @date 21.09.2023 10:54
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  List<User> findAllByFirstName(String firstName);
}
