package com.github.ksewen.yorozuya.auth.server.security;

/**
 * @author ksewen
 * @date 12.12.2023 22:55
 */
public interface AuthenticationManager {

  Long getUserId();

  String getUsername();
}
