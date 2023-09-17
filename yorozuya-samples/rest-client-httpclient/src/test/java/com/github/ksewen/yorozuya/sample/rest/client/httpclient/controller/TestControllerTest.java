package com.github.ksewen.yorozuya.sample.rest.client.httpclient.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.ksewen.yorozuya.common.enums.impl.DefaultResultCodeEnums;
import org.hamcrest.core.IsNull;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author ksewen
 * @date 15.09.2023 17:30
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = {"server.url=http://127.0.0.1:38080/rest/server"})
@AutoConfigureMockMvc(addFilters = false)
class TestControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void client() throws Exception {
    this.mockMvc
        .perform(
            get("/rest/client")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.SUCCESS.getCode()))
        .andExpect(jsonPath("$.message").value(DefaultResultCodeEnums.SUCCESS.getMessage()))
        .andExpect(jsonPath("$.data").value(Boolean.TRUE));
  }

  @Test
  void clientTimeout() throws Exception {
    this.mockMvc
        .perform(
            get(UriComponentsBuilder.fromUriString("/rest/client")
                    .queryParam("second", 10)
                    .toUriString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getCode()))
        .andExpect(jsonPath("$.message").value(StringContains.containsString("Read timed out")))
        .andExpect(jsonPath("$.data").value(IsNull.nullValue()));
  }

  @Test
  void feignClient() throws Exception {
    this.mockMvc
        .perform(
            get("/rest/feign-client")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.SUCCESS.getCode()))
        .andExpect(jsonPath("$.message").value(DefaultResultCodeEnums.SUCCESS.getMessage()))
        .andExpect(jsonPath("$.data").value(Boolean.TRUE));
  }

  @Test
  void feignClientTimeout() throws Exception {
    this.mockMvc
        .perform(
            get(UriComponentsBuilder.fromUriString("/rest/feign-client")
                    .queryParam("second", 10)
                    .toUriString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.REMOTE_CALL_FAILURE.getCode()))
        .andExpect(jsonPath("$.message").value(StringContains.containsString("Read timed out")))
        .andExpect(jsonPath("$.data").value(IsNull.nullValue()));
  }

  @Test
  void server() throws Exception {
    this.mockMvc
        .perform(
            get("/rest/server")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(DefaultResultCodeEnums.SUCCESS.getCode()))
        .andExpect(jsonPath("$.message").value(DefaultResultCodeEnums.SUCCESS.getMessage()))
        .andExpect(jsonPath("$.data").value(Boolean.TRUE));
  }
}
