package com.github.ksewen.yorozuya.dashboard;

import com.github.ksewen.yorozuya.dashboard.annotation.EnableDashboard;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDashboard
public class YorozuyaDashboardApplication {

  public static void main(String[] args) {
    SpringApplication.run(YorozuyaDashboardApplication.class, args);
  }
}
