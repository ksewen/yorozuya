package com.github.ksewen.yorozuya.common.exception;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ksewen
 * @date 27.10.2023 15:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceNotFoundException extends CommonException {

  public ResourceNotFoundException() {
    super(DefaultResultCodeEnums.NOT_FOUND);
  }

  public ResourceNotFoundException(String message) {
    super(DefaultResultCodeEnums.NOT_FOUND, message);
  }
}
