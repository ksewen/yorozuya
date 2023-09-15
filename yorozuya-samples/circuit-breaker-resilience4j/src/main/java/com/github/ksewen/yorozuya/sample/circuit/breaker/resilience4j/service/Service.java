package com.github.ksewen.yorozuya.sample.circuit.breaker.resilience4j.service;
/**
 * @author ksewen
 * @date 14.09.2023 10:49
 */
public interface Service {

  String success();

  String failure();

  String timeout();

  String ignoreException();
}
