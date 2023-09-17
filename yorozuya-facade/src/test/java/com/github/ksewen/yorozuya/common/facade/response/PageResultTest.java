package com.github.ksewen.yorozuya.common.facade.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.yorozuya.common.constant.DefaultPageConstants;
import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import java.util.Arrays;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

/**
 * @author ksewen
 * @date 03.09.2023 15:59
 */
class PageResultTest {

  private String MESSAGE = "test message";

  private String DATA_NAME = "test";

  private List<Item> DATA =
      Arrays.asList(
          Item.builder().id(1).build(), Item.builder().id(2).build(), Item.builder().id(3).build());

  @Test
  void success() {
    PageResult result = PageResult.success();
    assertThat(result)
        .isNotNull()
        .matches(r -> r.isSuccess(), "it's failed")
        .matches(r -> DefaultResultCodeEnums.SUCCESS.getCode() == r.getCode(), "wrong code")
        .matches(
            r -> DefaultResultCodeEnums.SUCCESS.getMessage().equals(r.getMessage()),
            "wrong message")
        .matches(r -> r.getData() == null, "data is not null");
  }

  @Test
  void testSuccess() {
    PageResult result = PageResult.success(this.MESSAGE);
    assertThat(result)
        .isNotNull()
        .matches(r -> r.isSuccess(), "it's failed")
        .matches(r -> DefaultResultCodeEnums.SUCCESS.getCode() == r.getCode(), "wrong code")
        .matches(r -> this.MESSAGE.equals(r.getMessage()), "wrong message")
        .matches(r -> r.getData() == null, "data is not null");
  }

  @Test
  void testSuccess1() {
    PageResult result = PageResult.success(2, 15, 100L);
    assertThat(result)
        .isNotNull()
        .matches(r -> r.isSuccess(), "it's failed")
        .matches(r -> DefaultResultCodeEnums.SUCCESS.getCode() == r.getCode(), "wrong code")
        .matches(
            r -> DefaultResultCodeEnums.SUCCESS.getMessage().equals(r.getMessage()),
            "wrong message")
        .matches(r -> r.getPage() == 2, "wrong page")
        .matches(r -> r.getPageSize() == 15, "wrong page size")
        .matches(r -> r.getTotal() == 100L, "wrong total")
        .matches(r -> r.getData() == null, "data is not null");
  }

  @Test
  void testSuccess2() {
    PageResult<Item> result = PageResult.success(this.DATA, 2, 3, 100L);
    assertThat(result)
        .isNotNull()
        .matches(r -> r.isSuccess(), "it's failed")
        .matches(r -> DefaultResultCodeEnums.SUCCESS.getCode() == r.getCode(), "wrong code")
        .matches(
            r -> DefaultResultCodeEnums.SUCCESS.getMessage().equals(r.getMessage()),
            "wrong message")
        .matches(r -> r.getPage() == 2, "wrong page")
        .matches(r -> r.getPageSize() == 3, "wrong page size")
        .matches(r -> r.getTotal() == 100L, "wrong total")
        .matches(r -> r.getData() != null, "data is null")
        .matches(r -> r.getData().size() == 3, "wrong size of the data")
        .matches(r -> r.getData().stream().anyMatch(i -> i.getId() == 1), "wrong id of the item 1")
        .matches(r -> r.getData().stream().anyMatch(i -> i.getId() == 2), "wrong id of the item 2")
        .matches(r -> r.getData().stream().anyMatch(i -> i.getId() == 3), "wrong id of the item 3")
        .matches(r -> !r.getData().stream().anyMatch(i -> i.getId() == 4), "unexpected item");
  }

  @Test
  void testSuccess3() {
    PageResult<Item> result = PageResult.success(this.MESSAGE, this.DATA, 2, 3, 100L);
    assertThat(result)
        .isNotNull()
        .matches(r -> r.isSuccess(), "it's failed")
        .matches(r -> DefaultResultCodeEnums.SUCCESS.getCode() == r.getCode(), "wrong code")
        .matches(r -> this.MESSAGE.equals(r.getMessage()), "wrong message")
        .matches(r -> r.getPage() == 2, "wrong page")
        .matches(r -> r.getPageSize() == 3, "wrong page size")
        .matches(r -> r.getTotal() == 100L, "wrong total")
        .matches(r -> r.getData() != null, "data is null")
        .matches(r -> r.getData().stream().anyMatch(i -> i.getId() == 1), "wrong id of the item 1")
        .matches(r -> r.getData().stream().anyMatch(i -> i.getId() == 2), "wrong id of the item 2")
        .matches(r -> r.getData().stream().anyMatch(i -> i.getId() == 3), "wrong id of the item 3")
        .matches(r -> !r.getData().stream().anyMatch(i -> i.getId() == 4), "unexpected item");
  }

  @Test
  void testSuccess4() {
    PageResult result = PageResult.success(this.MESSAGE, 2, 15, 100L);
    assertThat(result)
        .isNotNull()
        .matches(r -> r.isSuccess(), "it's failed")
        .matches(r -> DefaultResultCodeEnums.SUCCESS.getCode() == r.getCode(), "wrong code")
        .matches(r -> this.MESSAGE.equals(r.getMessage()), "wrong message")
        .matches(r -> r.getPage() == 2, "wrong page")
        .matches(r -> r.getPageSize() == 15, "wrong page size")
        .matches(r -> r.getTotal() == 100L, "wrong total")
        .matches(r -> r.getData() == null, "data is not null");
  }

  @Test
  void operationFailed() {
    PageResult result = PageResult.operationFailed();
    assertThat(result)
        .isNotNull()
        .matches(r -> !r.isSuccess(), "it's success")
        .matches(
            r -> DefaultResultCodeEnums.OPERATION_FAILED.getCode() == r.getCode(), "wrong code")
        .matches(
            r -> DefaultResultCodeEnums.OPERATION_FAILED.getMessage().equals(r.getMessage()),
            "wrong message")
        .matches(r -> r.getData() == null, "data is not null")
        .matches(r -> r.getPage() == null, "page is not null")
        .matches(r -> r.getPageSize() == null, "page size is not null")
        .matches(r -> r.getTotal() == null, "total size is not null");
  }

  @Test
  void testOperationFailed() {
    PageResult result = PageResult.operationFailed(this.MESSAGE);
    assertThat(result)
        .isNotNull()
        .matches(r -> !r.isSuccess(), "it's success")
        .matches(
            r -> DefaultResultCodeEnums.OPERATION_FAILED.getCode() == r.getCode(), "wrong code")
        .matches(r -> this.MESSAGE.equals(r.getMessage()), "wrong message")
        .matches(r -> r.getData() == null, "data is not null")
        .matches(r -> r.getPage() == null, "page is not null")
        .matches(r -> r.getPageSize() == null, "page size is not null")
        .matches(r -> r.getTotal() == null, "total size is not null");
  }

  @Test
  void systemError() {
    PageResult result = PageResult.systemError();
    assertThat(result)
        .isNotNull()
        .matches(r -> !r.isSuccess(), "it's success")
        .matches(r -> DefaultResultCodeEnums.SYSTEM_ERROR.getCode() == r.getCode(), "wrong code")
        .matches(
            r -> DefaultResultCodeEnums.SYSTEM_ERROR.getMessage().equals(r.getMessage()),
            "wrong message")
        .matches(r -> r.getData() == null, "data is not null")
        .matches(r -> r.getPage() == null, "page is not null")
        .matches(r -> r.getPageSize() == null, "page size is not null")
        .matches(r -> r.getTotal() == null, "total size is not null");
  }

  @Test
  void testSystemError() {
    PageResult result = PageResult.systemError(this.MESSAGE);
    assertThat(result)
        .isNotNull()
        .matches(r -> !r.isSuccess(), "it's success")
        .matches(r -> DefaultResultCodeEnums.SYSTEM_ERROR.getCode() == r.getCode(), "wrong code")
        .matches(r -> this.MESSAGE.equals(r.getMessage()), "wrong message")
        .matches(r -> r.getData() == null, "data is not null")
        .matches(r -> r.getPage() == null, "page is not null")
        .matches(r -> r.getPageSize() == null, "page size is not null")
        .matches(r -> r.getTotal() == null, "total size is not null");
  }

  @Test
  void paramInvalid() {
    PageResult result = PageResult.paramInvalid();
    assertThat(result)
        .isNotNull()
        .matches(r -> !r.isSuccess(), "it's success")
        .matches(r -> DefaultResultCodeEnums.PARAM_INVALID.getCode() == r.getCode(), "wrong code")
        .matches(
            r -> DefaultResultCodeEnums.PARAM_INVALID.getMessage().equals(r.getMessage()),
            "wrong message")
        .matches(r -> r.getData() == null, "data is not null")
        .matches(r -> r.getPage() == null, "page is not null")
        .matches(r -> r.getPageSize() == null, "page size is not null")
        .matches(r -> r.getTotal() == null, "total size is not null");
  }

  @Test
  void testParamInvalid() {
    PageResult result = PageResult.paramInvalid(this.MESSAGE);
    assertThat(result)
        .isNotNull()
        .matches(r -> !r.isSuccess(), "it's success")
        .matches(r -> DefaultResultCodeEnums.PARAM_INVALID.getCode() == r.getCode(), "wrong code")
        .matches(r -> this.MESSAGE.equals(r.getMessage()), "wrong message")
        .matches(r -> r.getData() == null, "data is not null")
        .matches(r -> r.getPage() == null, "page is not null")
        .matches(r -> r.getPageSize() == null, "page size is not null")
        .matches(r -> r.getTotal() == null, "total size is not null");
  }

  @Test
  void testPageResult() {
    PageResult<Item> result =
        PageResult.<Item>builder()
            .success(Boolean.TRUE)
            .code(100)
            .message(this.MESSAGE)
            .page(DefaultPageConstants.DEFAULT_PAGE)
            .pageSize(this.DATA.size())
            .total(100L)
            .data(this.DATA)
            .build();
    assertThat(result)
        .isNotNull()
        .matches(r -> r.isSuccess(), "it's failed")
        .matches(r -> r.getCode() == 100, "wrong code")
        .matches(r -> this.MESSAGE.equals(r.getMessage()), "wrong message")
        .matches(r -> r.getPage() != null, "page is null")
        .matches(r -> r.getPage() == DefaultPageConstants.DEFAULT_PAGE, "wrong page")
        .matches(r -> r.getPageSize() != null, "page size is null")
        .matches(r -> r.getPageSize() == this.DATA.size(), "wrong page size")
        .matches(r -> r.getTotal() != null, "total is null")
        .matches(r -> r.getTotal() == 100L, "wrong total")
        .matches(r -> r.getData() != null, "data is null")
        .matches(r -> r.getData().size() == 3, "wrong size of the data")
        .matches(r -> r.getData().stream().anyMatch(i -> i.getId() == 1), "wrong id of the item 1")
        .matches(r -> r.getData().stream().anyMatch(i -> i.getId() == 2), "wrong id of the item 2")
        .matches(r -> r.getData().stream().anyMatch(i -> i.getId() == 3), "wrong id of the item 3")
        .matches(r -> !r.getData().stream().anyMatch(i -> i.getId() == 4), "unexpected item");
  }

  @Data
  @Builder
  static class Item {

    private int id;
  }
}
