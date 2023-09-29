package com.github.ksewen.yorozuya.common.exception;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ksewen
 * @date 26.09.2023 22:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SerializationOrDeserializationException extends CommonException {

  public SerializationOrDeserializationException() {
    super(DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR);
  }

  public SerializationOrDeserializationException(String message) {
    super(DefaultResultCodeEnums.SERIALIZATION_OR_DESERIALIZATION_ERROR, message);
  }
}
