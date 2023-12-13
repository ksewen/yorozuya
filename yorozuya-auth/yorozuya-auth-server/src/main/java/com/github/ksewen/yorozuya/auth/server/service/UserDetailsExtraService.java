package com.github.ksewen.yorozuya.auth.server.service;

/**
 * @author ksewen
 * @date 24.11.2023 22:03
 */
public interface UserDetailsExtraService<R, T> {

  R getUserId(T t);
}
