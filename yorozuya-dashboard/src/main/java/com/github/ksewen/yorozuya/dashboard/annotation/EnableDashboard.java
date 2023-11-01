package com.github.ksewen.yorozuya.dashboard.annotation;

import com.github.ksewen.yorozuya.dashboard.configuration.DashboardMarkerAutoConfiguration;
import java.lang.annotation.*;
import org.springframework.context.annotation.Import;

/**
 * @author ksewen
 * @date 25.10.2023 17:33
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DashboardMarkerAutoConfiguration.class)
public @interface EnableDashboard {}
