package com.github.ksewen.yorozuya.dashboard.service;

import com.github.ksewen.yorozuya.dashboard.dto.EurekaQueryDTO;

/**
 * @author ksewen
 * @date 31.10.2023 16:27
 */
public interface EurekaService {

  EurekaQueryDTO applications(String server);
}
