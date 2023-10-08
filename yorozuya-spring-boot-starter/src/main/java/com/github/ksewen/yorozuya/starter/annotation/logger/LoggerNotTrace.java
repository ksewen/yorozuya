package com.github.ksewen.yorozuya.starter.annotation.logger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to deactivate the AroundLogger when {@link
 * com.github.ksewen.yorozuya.starter.aspect.logger.impl.RecordByDefaultAroundLogger} is enabled.
 *
 * @author ksewen
 * @date 05.10.2023 16:58
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggerNotTrace {}
