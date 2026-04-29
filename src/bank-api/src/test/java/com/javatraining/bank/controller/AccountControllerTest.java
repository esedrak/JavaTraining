package com.javatraining.bank.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatraining.bank.config.SecurityConfig;
import com.javatraining.bank.domain.Account;
import com.javatraining.bank.domain.exception.AccountNotFoundException;
import com.javatraining.bank.dto.CreateAccountRequest;
import com.javatraining.bank.security.BankPermission;
import com.javatraining.bank.service.BankService;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccountController.class)
@Import({
  SecurityConfig.class,
  com.javatraining.bank.config.JwtAuthenticationFilter.class,
  BankPermission.class
})
@DisplayName("AccountController")
class AccountControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private BankService bankService;

  private static Account account(String owner, String balance) {
    var a = new Account();
    a.setOwner(owner);
    a.setBalance(new BigDecimal(balance));
    ReflectionTestUtils.setField(a, "id", UUID.randomUUID());
    return a;
  }

  @Nested
  @DisplayName("GET /v1/accounts")
  class ListAccounts {

    @Test
    @DisplayName("returns 200 with list of accounts")
    void returns200WithList() throws Exception {
      var a = account("Alice", "1000.00");
      when(bankService.listAccounts()).thenReturn(List.of(a));

      mockMvc
          .perform(get("/v1/accounts").with(user("alice")))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].owner").value("Alice"));
    }

    @Test
    @DisplayName("returns 401 when unauthenticated")
    void returns401WhenUnauthenticated() throws Exception {
      mockMvc.perform(get("/v1/accounts")).andExpect(status().isUnauthorized());
    }
  }

  @Nested
  @DisplayName("GET /v1/accounts/{id}")
  class GetAccount {

    @Test
    @DisplayName("returns 200 when account exists")
    void returns200WhenFound() throws Exception {
      var a = account("Bob", "500.00");
      when(bankService.getAccount(a.getId())).thenReturn(a);

      mockMvc
          .perform(get("/v1/accounts/{id}", a.getId()).with(user("alice")))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.owner").value("Bob"));
    }

    @Test
    @DisplayName("returns 404 when account not found")
    void returns404WhenNotFound() throws Exception {
      var id = UUID.randomUUID();
      when(bankService.getAccount(id)).thenThrow(new AccountNotFoundException(id));

      mockMvc
          .perform(get("/v1/accounts/{id}", id).with(user("alice")))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message").exists());
    }
  }

  @Nested
  @DisplayName("POST /v1/accounts")
  class CreateAccount {

    @Test
    @DisplayName("returns 201 with accounts:write scope")
    void returns201WithScope() throws Exception {
      var request = new CreateAccountRequest("Charlie", new BigDecimal("200.00"));
      var created = account("Charlie", "200.00");
      when(bankService.createAccount(any(), any())).thenReturn(created);

      mockMvc
          .perform(
              post("/v1/accounts")
                  .with(
                      user("alice").authorities(new SimpleGrantedAuthority("SCOPE_accounts:write")))
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.owner").value("Charlie"));
    }

    @Test
    @DisplayName("returns 403 without accounts:write scope")
    void returns403WithoutScope() throws Exception {
      var request = new CreateAccountRequest("Charlie", new BigDecimal("200.00"));

      mockMvc
          .perform(
              post("/v1/accounts")
                  .with(user("alice")) // no SCOPE_accounts:write
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("returns 400 when owner is blank")
    void returns400WhenOwnerBlank() throws Exception {
      var request = new CreateAccountRequest("", BigDecimal.ZERO);
      when(bankService.createAccount(any(), any()))
          .thenThrow(new IllegalArgumentException("Owner must not be blank"));

      mockMvc
          .perform(
              post("/v1/accounts")
                  .with(
                      user("alice").authorities(new SimpleGrantedAuthority("SCOPE_accounts:write")))
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").exists());
    }
  }
}
