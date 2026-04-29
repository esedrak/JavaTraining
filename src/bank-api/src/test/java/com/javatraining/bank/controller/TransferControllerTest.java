package com.javatraining.bank.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.javatraining.bank.config.SecurityConfig;
import com.javatraining.bank.domain.Transfer;
import com.javatraining.bank.domain.TransferStatus;
import com.javatraining.bank.domain.exception.TransferNotFoundException;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransferController.class)
@Import({
  SecurityConfig.class,
  com.javatraining.bank.config.JwtAuthenticationFilter.class,
  BankPermission.class
})
@DisplayName("TransferController")
class TransferControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private BankService bankService;

  private static Transfer transfer(BigDecimal amount) {
    var t = new Transfer();
    ReflectionTestUtils.setField(t, "id", UUID.randomUUID());
    t.setFromAccountId(UUID.randomUUID());
    t.setToAccountId(UUID.randomUUID());
    t.setAmount(amount);
    t.setStatus(TransferStatus.COMPLETED);
    return t;
  }

  @Nested
  @DisplayName("GET /v1/transfers")
  class ListTransfers {

    @Test
    @DisplayName("returns 200 with list of transfers")
    void returns200WithList() throws Exception {
      var t = transfer(new BigDecimal("100.00"));
      when(bankService.listTransfers()).thenReturn(List.of(t));

      mockMvc
          .perform(get("/v1/transfers").with(user("alice")))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].amount").value(100.00));
    }

    @Test
    @DisplayName("returns 401 when unauthenticated")
    void returns401WhenUnauthenticated() throws Exception {
      mockMvc.perform(get("/v1/transfers")).andExpect(status().isUnauthorized());
    }
  }

  @Nested
  @DisplayName("GET /v1/transfers/{id}")
  class GetTransfer {

    @Test
    @DisplayName("returns 200 when transfer exists")
    void returns200WhenFound() throws Exception {
      var t = transfer(new BigDecimal("250.00"));
      when(bankService.getTransfer(t.getId())).thenReturn(t);

      mockMvc
          .perform(get("/v1/transfers/{id}", t.getId()).with(user("alice")))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.amount").value(250.00));
    }

    @Test
    @DisplayName("returns 404 when transfer not found")
    void returns404WhenNotFound() throws Exception {
      var id = UUID.randomUUID();
      when(bankService.getTransfer(id)).thenThrow(new TransferNotFoundException(id));

      mockMvc
          .perform(get("/v1/transfers/{id}", id).with(user("alice")))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message").exists());
    }
  }
}
