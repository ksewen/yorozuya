package com.github.ksewen.yorozuya.sample.auth.server.configuration;

import com.github.ksewen.yorozuya.sample.auth.server.model.UserModel;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author ksewen
 * @date 08.12.2023 17:31
 */
// @Configuration
public class AuthServerAutoConfiguration {

  @Bean
  UserDetailsService userDetailsService(@Autowired PasswordEncoder passwordEncoder) {
    InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
    userDetailsService.createUser(
        new UserModel(1L, "admin", passwordEncoder.encode("123456"), Collections.emptyList()));
    return userDetailsService;
  }
}
