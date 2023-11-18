package com.github.ksewen.yorozuya.dashboard.annotation;

import com.github.ksewen.yorozuya.dashboard.configuration.DisableRepositoryMarkerAutoConfiguration;
import java.lang.annotation.*;
import org.springframework.context.annotation.Import;

/**
 * @author ksewen
 * @date 18.11.2023 12:31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DisableRepositoryMarkerAutoConfiguration.class)
public @interface DisableRepository {}
