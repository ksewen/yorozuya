package com.github.ksewen.yorozuya.common.exception;

import com.github.ksewen.yorozuya.common.enums.ResultCode;
import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import lombok.Data;

/**
 * @author ksewen
 * @date 04.09.2023 10:26
 */
@Data
public class CommonException extends RuntimeException {

  private ResultCode code;

  public CommonException() {
    super(DefaultResultCodeEnums.SYSTEM_ERROR.getMessage());
    this.code = DefaultResultCodeEnums.SYSTEM_ERROR;
  }

  public CommonException(ResultCode code) {
    super(code.getMessage());
    this.code = code;
  }

  public CommonException(String message) {
    super(message);
    this.code = DefaultResultCodeEnums.SYSTEM_ERROR;
  }

  public CommonException(ResultCode code, String message) {
    super(message);
    this.code = code;
  }
}
