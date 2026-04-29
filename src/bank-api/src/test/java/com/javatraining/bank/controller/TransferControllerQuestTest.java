package com.javatraining.bank.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatraining.bank.config.JwtAuthenticationFilter;
import com.javatraining.bank.config.SecurityConfig;
import com.javatraining.bank.domain.Account;
import com.javatraining.bank.domain.Transfer;
import com.javatraining.bank.domain.TransferStatus;
import com.javatraining.bank.dto.CreateTransferRequest;
import com.javatraining.bank.security.BankPermission;
import com.javatraining.bank.service.BankService;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
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

/**
 * Bank Transfer Quest — integration tests.
 *
 * <p>Quest 2: Remove {@code @Disabled} from the two auth tests once you have added
 * {@code @RequiresTransfersWrite} to {@code createTransfer}.
 *
 * <p>Quest 3: Remove {@code @Disabled} from the 201 and 400 tests once {@code createTransfer} is
 * implemented.
 *
 * <p>Quest 4: Implement the three {@code @Disabled("TODO")} stubs at the bottom of this file, then
 * remove their {@code @Disabled} annotations.
 *
 * <p>Run just these tests:
 *
 * <pre>./gradlew :src:bank-api:test --tests "*.TransferControllerQuestTest"</pre>
 */
@WebMvcTest(TransferController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, BankPermission.class})
@DisplayName("Transfer Controller — Quest Tests")
class TransferControllerQuestTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private BankService bankService;

  // ── Helpers ────────────────────────────────────────────────────────────────

  private static Account account(String owner, String balance) {
    var a = new Account();
    a.setOwner(owner);
    a.setBalance(new BigDecimal(balance));
    ReflectionTestUtils.setField(a, "id", UUID.randomUUID());
    return a;
  }

  private static Transfer transfer(UUID fromId, UUID toId, BigDecimal amount) {
    var t = new Transfer();
    ReflectionTestUtils.setField(t, "id", UUID.randomUUID());
    t.setFromAccountId(fromId);
    t.setToAccountId(toId);
    t.setAmount(amount);
    t.setStatus(TransferStatus.COMPLETED);
    return t;
  }

  private String json(Object value) throws Exception {
    return objectMapper.writeValueAsString(value);
  }

  // ── Quest 2: Authentication ────────────────────────────────────────────────

  @Nested
  @DisplayName("Quest 2 — authentication")
  class Quest2Auth {

    /**
     * Quest 2: Remove {@code @Disabled} once you have added
     * {@code @PreAuthorize("hasAuthority('SCOPE_transfers:write')")} to {@code createTransfer}.
     */
    @Test
    @Disabled(
        "Quest 2 — remove @Disabled once @PreAuthorize(@bankPermission) is added to createTransfer")
    @DisplayName("returns 401 when no token is provided")
    void createTransfer_returns401_whenNoToken() throws Exception {
      var body =
          json(
              new CreateTransferRequest(
                  UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("50.00")));

      mockMvc
          .perform(post("/v1/transfers").contentType(MediaType.APPLICATION_JSON).content(body))
          // No user() post-processor → Spring Security returns 401
          .andExpect(status().isUnauthorized());
    }

    /**
     * Quest 2: Remove {@code @Disabled} once you have added
     * {@code @PreAuthorize("hasAuthority('SCOPE_transfers:write')")} to {@code createTransfer}.
     */
    @Test
    @Disabled(
        "Quest 2 — remove @Disabled once @PreAuthorize(@bankPermission) is added to createTransfer")
    @DisplayName("returns 403 when token lacks transfers:write scope")
    void createTransfer_returns403_whenScopeMissing() throws Exception {
      var body =
          json(
              new CreateTransferRequest(
                  UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("50.00")));

      mockMvc
          .perform(
              post("/v1/transfers")
                  .with(user("alice")) // authenticated but NO SCOPE_transfers:write
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(body))
          .andExpect(status().isForbidden());
    }
  }

  // ── Quest 3: Implementation ────────────────────────────────────────────────

  @Nested
  @DisplayName("Quest 3 — implementation")
  class Quest3Impl {

    /** Quest 3: Remove {@code @Disabled} once {@code createTransfer} returns 201. */
    @Test
    @Disabled("Quest 3 — remove @Disabled once createTransfer is implemented")
    @DisplayName("returns 201 with Location header when transfer succeeds")
    void createTransfer_returns201_whenValid() throws Exception {
      var fromId = UUID.randomUUID();
      var toId = UUID.randomUUID();
      var amount = new BigDecimal("100.00");

      var sourceAccount = account("alice", "500.00");
      ReflectionTestUtils.setField(sourceAccount, "id", fromId);
      when(bankService.getAccount(fromId)).thenReturn(sourceAccount);

      var created = transfer(fromId, toId, amount);
      when(bankService.createTransfer(fromId, toId, amount)).thenReturn(created);

      mockMvc
          .perform(
              post("/v1/transfers")
                  .with(
                      user("alice")
                          .authorities(new SimpleGrantedAuthority("SCOPE_transfers:write")))
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(json(new CreateTransferRequest(fromId, toId, amount))))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.amount").value(100.00));
    }

    /** Quest 3: Remove {@code @Disabled} once exception mapping is implemented. */
    @Test
    @Disabled("Quest 3 — remove @Disabled once createTransfer is implemented")
    @DisplayName("returns 400 when service throws IllegalArgumentException")
    void createTransfer_returns400_whenArgumentInvalid() throws Exception {
      var fromId = UUID.randomUUID();
      var toId = UUID.randomUUID();
      var amount = new BigDecimal("-50.00");

      var sourceAccount = account("alice", "500.00");
      ReflectionTestUtils.setField(sourceAccount, "id", fromId);
      when(bankService.getAccount(fromId)).thenReturn(sourceAccount);

      when(bankService.createTransfer(any(), any(), any()))
          .thenThrow(new IllegalArgumentException("Amount must be positive"));

      mockMvc
          .perform(
              post("/v1/transfers")
                  .with(
                      user("alice")
                          .authorities(new SimpleGrantedAuthority("SCOPE_transfers:write")))
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(json(new CreateTransferRequest(fromId, toId, amount))))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").exists());
    }
  }

  // ── Quest 4: Student-written tests ────────────────────────────────────────

  @Nested
  @DisplayName("Quest 4 — write the missing tests")
  class Quest4Tests {

    /**
     * Quest 4 TODO: implement this test.
     *
     * <p>Scenario: the caller is authenticated as "alice" with {@code transfers:write}, but the
     * source account is owned by "bob". The controller must return {@code 403 Forbidden}.
     *
     * <p>Steps:
     *
     * <ol>
     *   <li>Stub {@code bankService.getAccount(fromId)} to return an account owned by {@code
     *       "bob"}.
     *   <li>Perform POST as {@code user("alice")} with {@code SCOPE_transfers:write}.
     *   <li>Assert {@code status().isForbidden()}.
     * </ol>
     */
    @Test
    @Disabled("TODO — Quest 4: implement this test, then remove @Disabled")
    @DisplayName("returns 403 when caller does not own the source account")
    void createTransfer_returns403_whenCallerIsNotOwner() throws Exception {
      // TODO: implement
      throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Quest 4 TODO: implement this test.
     *
     * <p>Scenario: the source account does not exist. {@code bankService.getAccount} throws {@code
     * AccountNotFoundException}. The controller must return {@code 404 Not Found}.
     *
     * <p>Steps:
     *
     * <ol>
     *   <li>Stub {@code bankService.getAccount(fromId)} to throw {@code new
     *       AccountNotFoundException(fromId)}.
     *   <li>Perform POST as {@code user("alice")} with {@code SCOPE_transfers:write}.
     *   <li>Assert {@code status().isNotFound()} and that the response body has a {@code "message"}
     *       field.
     * </ol>
     */
    @Test
    @Disabled("TODO — Quest 4: implement this test, then remove @Disabled")
    @DisplayName("returns 404 when source account does not exist")
    void createTransfer_returns404_whenSourceAccountNotFound() throws Exception {
      // TODO: implement
      throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Quest 4 TODO: implement this test.
     *
     * <p>Scenario: alice owns the account but has insufficient funds. {@code
     * bankService.createTransfer} throws {@code InsufficientFundsException}. The controller must
     * return {@code 422 Unprocessable Entity}.
     *
     * <p>Steps:
     *
     * <ol>
     *   <li>Stub {@code bankService.getAccount(fromId)} to return an account owned by {@code
     *       "alice"} with a low balance (e.g. {@code "10.00"}).
     *   <li>Stub {@code bankService.createTransfer(any(), any(), any())} to throw {@code new
     *       InsufficientFundsException(fromId, BigDecimal.TEN, new BigDecimal("100"))}.
     *   <li>Perform POST as {@code user("alice")} with {@code SCOPE_transfers:write}.
     *   <li>Assert {@code status().isUnprocessableEntity()} and a {@code "message"} field.
     * </ol>
     */
    @Test
    @Disabled("TODO — Quest 4: implement this test, then remove @Disabled")
    @DisplayName("returns 422 when transfer fails due to insufficient funds")
    void createTransfer_returns422_whenInsufficientFunds() throws Exception {
      // TODO: implement
      throw new UnsupportedOperationException("Not yet implemented");
    }
  }
}
