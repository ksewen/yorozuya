package com.github.ksewen.yorozuya.auth.server.annotation;

import com.github.ksewen.yorozuya.auth.server.configuration.AuthServerMarkerAutoConfiguration;
import java.lang.annotation.*;
import org.springframework.context.annotation.Import;

/**
 * @author ksewen
 * @date 26.11.2023 21:10
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AuthServerMarkerAutoConfiguration.class)
public @interface EnableAuthServer {}
