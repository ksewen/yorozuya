package com.github.ksewen.yorozuya.starter.helper.json.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import lombok.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ksewen
 * @date 26.09.2023 22:43
 */
@SpringBootTest(
    classes = {JacksonJsonHelpers.class, JacksonAutoConfiguration.class},
    properties = {
      "spring.jackson.time-zone=GMT+2",
      "spring.jackson.date-format=yyyy-MM-dd HH:mm:ss"
    })
class JacksonJsonHelpersTest {
  @Autowired private JsonHelpers jsonHelpers;

  private String SOURCE_STRING = "{\"name\":\"test\",\"time\":\"2023-01-01 17:00:00\"}";
  private String SOURCE_STRING_2 = "{\"name\":\"test\",\"time\":\"01.01.2023 17:00:00\"}";

  private String NAME_STRING = "test";

  private String TIME_STRING = "2023-01-01 17:00:00";

  private String TIME_STRING_2 = "01.01.2023 17:00:00";

  private ObjectMapper CUSTOMER_OBJECT_MAPPER =
      new ObjectMapper()
          .setDateFormat(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"))
          .setTimeZone(TimeZone.getTimeZone("GMT+2"));

  private Date DATE;

  {
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
      this.DATE = simpleDateFormat.parse("2023-01-01 17:00:00");
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void toObject() {
    TestObject result = this.jsonHelpers.toObject(this.SOURCE_STRING, TestObject.class);
    assertThat(result)
        .matches(t -> this.NAME_STRING.equals(t.name))
        .matches(t -> this.DATE.getTime() == result.getTime().getTime());
  }

  @Test
  void testToObject() {
    HashMap<String, String> result =
        this.jsonHelpers.toObject(this.SOURCE_STRING, new TypeReference<>() {});
    assertThat(result)
        .matches(m -> this.NAME_STRING.equals(m.get("name")))
        .matches(m -> this.TIME_STRING.equals(m.get("time")));
  }

  @Test
  void toMap() {
    Map result = this.jsonHelpers.toMap(SOURCE_STRING);
    assertThat(result)
        .isNotEmpty()
        .contains(entry("name", this.NAME_STRING))
        .contains(entry("time", this.TIME_STRING));
  }

  @Test
  void toJsonString() {
    TestObject source = TestObject.builder().name(this.NAME_STRING).time(this.DATE).build();
    String result = this.jsonHelpers.toJsonString(source);
    assertThat(result).isEqualTo(this.SOURCE_STRING);
  }

  @Test
  void toObjectList() {
    String source = "[{\"name\":\"test1\"}, {\"name\":\"test2\"}]";
    List<TestObject> result =
        this.jsonHelpers.toObjectList(source, new TypeReference<List<TestObject>>() {});
    assertThat(result)
        .matches(l -> l.size() == 2)
        .matches(l -> "test1".equals(l.get(0).getName()))
        .matches(l -> "test2".equals(l.get(1).getName()));
  }

  @Test
  void toJsonNode() {
    JsonNode result = this.jsonHelpers.toJsonNode(this.SOURCE_STRING);
    assertThat(result)
        .extracting(JsonNode::asText)
        .contains(this.NAME_STRING)
        .contains(this.TIME_STRING);
  }

  @Test
  void testToJsonNode() {
    TestObject source = TestObject.builder().name(this.NAME_STRING).time(this.DATE).build();
    JsonNode result = this.jsonHelpers.toJsonNode(source);
    assertThat(result)
        .extracting(JsonNode::asText)
        .contains(this.NAME_STRING)
        .contains(this.TIME_STRING);
  }

  @Test
  void testToObject1() {
    TestObject result =
        this.jsonHelpers.toObject(
            this.SOURCE_STRING_2, TestObject.class, this.CUSTOMER_OBJECT_MAPPER);
    assertThat(result)
        .matches(t -> this.NAME_STRING.equals(t.name))
        .matches(t -> this.DATE.getTime() == result.getTime().getTime());
  }

  @Test
  void testToObject2() {
    HashMap<String, String> result =
        this.jsonHelpers.toObject(
            this.SOURCE_STRING, new TypeReference<>() {}, this.CUSTOMER_OBJECT_MAPPER);
    assertThat(result)
        .matches(m -> this.NAME_STRING.equals(m.get("name")))
        .matches(m -> this.TIME_STRING.equals(m.get("time")));
  }

  @Test
  void testToMap() {
    Map result = this.jsonHelpers.toMap(SOURCE_STRING, this.CUSTOMER_OBJECT_MAPPER);
    assertThat(result)
        .isNotEmpty()
        .contains(entry("name", this.NAME_STRING))
        .contains(entry("time", this.TIME_STRING));
  }

  @Test
  void testToJsonString() {
    TestObject source = TestObject.builder().name(this.NAME_STRING).time(this.DATE).build();
    String result = this.jsonHelpers.toJsonString(source, this.CUSTOMER_OBJECT_MAPPER);
    assertThat(result).isEqualTo(this.SOURCE_STRING_2);
  }

  @Test
  void testToObjectList() throws ParseException {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
    Date except = simpleDateFormat.parse(this.TIME_STRING_2);
    String source =
        "[{\"name\":\"test1\", \"time\":\"" + this.TIME_STRING_2 + "\"}, {\"name\":\"test2\"}]";
    List<TestObject> result =
        this.jsonHelpers.toObjectList(
            source, new TypeReference<>() {}, this.CUSTOMER_OBJECT_MAPPER);
    assertThat(result)
        .matches(l -> l.size() == 2)
        .matches(l -> "test1".equals(l.get(0).getName()))
        .matches(l -> except.equals(l.get(0).getTime()))
        .matches(l -> "test2".equals(l.get(1).getName()));
  }

  @Test
  void testToJsonNode1() {
    JsonNode result =
        this.jsonHelpers.toJsonNode(this.SOURCE_STRING_2, this.CUSTOMER_OBJECT_MAPPER);
    assertThat(result)
        .extracting(JsonNode::asText)
        .contains(this.NAME_STRING)
        .contains(this.TIME_STRING_2);
  }

  @Test
  void testToJsonNode2() {
    TestObject source = TestObject.builder().name(this.NAME_STRING).time(this.DATE).build();
    JsonNode result = this.jsonHelpers.toJsonNode(source, this.CUSTOMER_OBJECT_MAPPER);
    assertThat(result)
        .extracting(JsonNode::asText)
        .contains(this.NAME_STRING)
        .contains(this.TIME_STRING_2);
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  static class TestObject {
    private String name;
    private Date time;
  }
}
