package com.github.ksewen.yorozuya.common.exception;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ksewen
 * @date 04.09.2023 10:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvalidParamException extends CommonException {

  public InvalidParamException() {
    super(DefaultResultCodeEnums.PARAM_INVALID);
  }

  public InvalidParamException(String message) {
    super(DefaultResultCodeEnums.PARAM_INVALID, message);
  }
}
