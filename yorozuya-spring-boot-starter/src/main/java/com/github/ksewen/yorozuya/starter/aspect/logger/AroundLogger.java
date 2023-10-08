package com.github.ksewen.yorozuya.starter.aspect.logger;

import org.aspectj.lang.JoinPoint;

/**
 * @author ksewen
 * @date 05.10.2023 17:12
 */
public interface AroundLogger {

  /**
   * get the value of applicationName.
   *
   * @return
   */
  String getApplicationName();

  /**
   * get the name of the operation.
   *
   * @return
   */
  String getOperationDescription(JoinPoint joinPoint);

  /**
   * process and get the arguments information or result as String.
   *
   * @param object
   * @return
   */
  String parseArgumentsOrResult(Object object);
}
