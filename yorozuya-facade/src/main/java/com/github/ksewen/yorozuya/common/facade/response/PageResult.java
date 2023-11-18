package com.github.ksewen.yorozuya.common.facade.response;

import com.github.ksewen.yorozuya.common.constant.DefaultPageConstants;
import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collection;
import lombok.Builder;
import lombok.Data;

/**
 * @author ksewen
 * @date 03.09.2023 14:58
 */
@Data
@Builder
@Schema(
    description =
        "Service response default paginated result with no data returned in case of failure.")
public class PageResult<T> {

  @Schema(description = "the result code of the processing")
  private int code;

  @Schema(description = "the message of the processing result")
  private String message;

  @Schema(description = "page number")
  @Builder.Default
  private Integer page = DefaultPageConstants.DEFAULT_PAGE;

  @Schema(description = "page size")
  @Builder.Default
  private Integer pageSize = DefaultPageConstants.DEFAULT_PAGE_SIZE;

  @Schema(description = "total data count")
  @Builder.Default
  private Long total = DefaultPageConstants.DEFAULT_TOTAL;

  @Schema(description = "successful flag for processing")
  @Builder.Default
  private boolean success = Boolean.FALSE;

  @Schema(description = "current page response result")
  private Collection<T> data;

  public static PageResult<?> success() {
    return PageResult.builder()
        .code(DefaultResultCodeEnums.SUCCESS.getCode())
        .message(DefaultResultCodeEnums.SUCCESS.getMessage())
        .success(Boolean.TRUE)
        .build();
  }

  public static PageResult<?> success(String message) {
    return PageResult.builder()
        .code(DefaultResultCodeEnums.SUCCESS.getCode())
        .message(message)
        .success(Boolean.TRUE)
        .build();
  }

  public static PageResult<?> success(int page, int pageSize, long total) {
    return PageResult.builder()
        .code(DefaultResultCodeEnums.SUCCESS.getCode())
        .message(DefaultResultCodeEnums.SUCCESS.getMessage())
        .page(page)
        .pageSize(pageSize)
        .total(total)
        .success(Boolean.TRUE)
        .build();
  }

  public static <T> PageResult<T> success(Collection<T> data, int page, int pageSize, long total) {
    return PageResult.<T>builder()
        .code(DefaultResultCodeEnums.SUCCESS.getCode())
        .message(DefaultResultCodeEnums.SUCCESS.getMessage())
        .page(page)
        .pageSize(pageSize)
        .total(total)
        .data(data)
        .success(Boolean.TRUE)
        .build();
  }

  public static <T> PageResult<T> success(
      String message, Collection<T> data, int page, int pageSize, long total) {
    return PageResult.<T>builder()
        .code(DefaultResultCodeEnums.SUCCESS.getCode())
        .message(message)
        .page(page)
        .pageSize(pageSize)
        .total(total)
        .data(data)
        .success(Boolean.TRUE)
        .build();
  }

  public static PageResult<?> success(String message, int page, int pageSize, long total) {
    return PageResult.builder()
        .code(DefaultResultCodeEnums.SUCCESS.getCode())
        .message(message)
        .page(page)
        .pageSize(pageSize)
        .total(total)
        .success(Boolean.TRUE)
        .build();
  }

  public static PageResult<?> operationFailed() {
    return PageResult.builder()
        .code(DefaultResultCodeEnums.OPERATION_FAILED.getCode())
        .message(DefaultResultCodeEnums.OPERATION_FAILED.getMessage())
        .page(null)
        .pageSize(null)
        .total(null)
        .build();
  }

  public static PageResult<?> operationFailed(String message) {
    return PageResult.builder()
        .code(DefaultResultCodeEnums.OPERATION_FAILED.getCode())
        .message(message)
        .page(null)
        .pageSize(null)
        .total(null)
        .build();
  }

  public static PageResult<?> systemError() {
    return PageResult.builder()
        .code(DefaultResultCodeEnums.SYSTEM_ERROR.getCode())
        .message(DefaultResultCodeEnums.SYSTEM_ERROR.getMessage())
        .page(null)
        .pageSize(null)
        .total(null)
        .build();
  }

  public static PageResult<?> systemError(String message) {
    return PageResult.builder()
        .code(DefaultResultCodeEnums.SYSTEM_ERROR.getCode())
        .message(message)
        .page(null)
        .pageSize(null)
        .total(null)
        .build();
  }

  public static PageResult<?> paramInvalid() {
    return PageResult.builder()
        .code(DefaultResultCodeEnums.PARAM_INVALID.getCode())
        .message(DefaultResultCodeEnums.PARAM_INVALID.getMessage())
        .page(null)
        .pageSize(null)
        .total(null)
        .build();
  }

  public static PageResult<?> paramInvalid(String message) {
    return PageResult.builder()
        .code(DefaultResultCodeEnums.PARAM_INVALID.getCode())
        .message(message)
        .page(null)
        .pageSize(null)
        .total(null)
        .build();
  }
}
