package com.github.ksewen.yorozuya.auth.server.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.ksewen.yorozuya.auth.server.AuthServerTests;
import com.github.ksewen.yorozuya.auth.server.dto.request.LoginRequest;
import com.github.ksewen.yorozuya.auth.server.service.UserDetailsExtraService;
import com.github.ksewen.yorozuya.auth.server.token.TokenProvider;
import com.github.ksewen.yorozuya.starter.configuration.jackson.JacksonJsonHelpersAutoConfiguration;
import com.github.ksewen.yorozuya.starter.helper.json.JsonHelpers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

/**
 * @author ksewen
 * @date 10.12.2023 22:27
 */
@SpringBootTest(
    classes = {
      AuthServerTests.class,
      JacksonAutoConfiguration.class,
      JacksonJsonHelpersAutoConfiguration.class,
      AuthController.class
    })
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private JsonHelpers jsonHelpers;

  @MockBean private AuthenticationProvider authenticationProvider;

  @MockBean private UserDetailsService userDetailsService;

  @MockBean private UserDetailsExtraService<Long, UserDetails> userDetailsExtraService;

  @MockBean private TokenProvider tokenProvider;

  @Test
  void login() throws Exception {
    long userId = 1L;
    String username = "username";
    String password = "password";
    String token = "test-token";
    String refreshToken = "test-refresh-token";

    UserDetails mockUserDetails = mock(UserDetails.class);
    when(this.userDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);
    when(this.userDetailsExtraService.getUserId(any(UserDetails.class))).thenReturn(userId);
    when(mockUserDetails.getUsername()).thenReturn(username);
    when(this.tokenProvider.generateToken(any())).thenReturn(token);
    when(this.tokenProvider.generateRefreshToken(any())).thenReturn(refreshToken);

    LoginRequest loginRequest =
        LoginRequest.builder().username(username).password(password).build();
    this.mockMvc
        .perform(
            post("/auth/login")
                .content(this.jsonHelpers.toJsonString(loginRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(20000))
        .andExpect(jsonPath("$.message").value("success"))
        .andExpect(jsonPath("$.data.userId").value(userId))
        .andExpect(jsonPath("$.data.username").value(username))
        .andExpect(jsonPath("$.data.token").value(token))
        .andExpect(jsonPath("$.data.refreshToken").value(refreshToken));
  }
}
