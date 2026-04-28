# Java Bank Transfer Quest

Welcome to the **Java Bank Transfer Quest**! In this challenge you will implement the `POST /v1/transfers` endpoint in a pre-scaffolded Spring Boot bank service.

This quest focuses on idiomatic Spring MVC controller patterns, SpringDoc OpenAPI annotation, Spring Security JWT authorization, exception mapping, and `@WebMvcTest` integration testing — without getting distracted by database or repository concerns.

Everything below the controller layer is pre-built. If you want to understand how the underlying layers work, read the source in `src/bank-api/src/main/java/com/javatraining/bank/`.

**`AccountController` is your fully-working reference** — read it, understand every pattern, then replicate it for transfers.

---

## Your Quests

Work through the quests in order. Each step builds on the previous one.

---

### Quest 1: Annotate the Transfer Endpoint

**File:** `src/bank-api/src/main/java/com/javatraining/bank/controller/TransferController.java`

**Context:**
The Bank API uses **code-first** design — SpringDoc scans `@ApiResponse` annotations and generates the OpenAPI spec automatically from your Java code. The contract lives in the code.

`AccountController` is fully annotated and is your reference. Study how `@ApiResponse` attributes document every possible HTTP response on `createAccount`, then apply the same discipline to `createTransfer`.

**Task:**
`TransferController.createTransfer` is missing its full response documentation. Add `@ApiResponse` annotations for all seven possible response codes:

| Code | When |
| :--- | :--- |
| `201 Created` | Transfer succeeded |
| `400 Bad Request` | Malformed or invalid input |
| `401 Unauthorized` | Missing or invalid JWT token |
| `403 Forbidden` | Valid token but missing `transfers:write` scope, or caller is not the source account owner |
| `404 Not Found` | Source or destination account does not exist |
| `422 Unprocessable Entity` | Business rule violation (e.g. insufficient funds) |
| `500 Internal Server Error` | Unexpected error (global exception handler) |

**Definition of Done:**
- Code compiles:
  ```bash
  ./gradlew :src:bank-api:compileJava
  ```
- Run the Bank API and inspect the generated spec:
  ```bash
  make run-bank-api
  curl http://localhost:8080/v3/api-docs | jq '.paths["/v1/transfers"].post.responses | keys'
  # Expected: ["200","201","400","401","403","404","422","500"]
  ```

---

### Quest 2: Wire Authentication to the Transfer Controller

**File:** `src/bank-api/src/main/java/com/javatraining/bank/controller/TransferController.java`

**Context:**
`SecurityConfig` already has JWT Bearer authentication fully configured — it validates every incoming `Authorization: Bearer <token>` header and populates Spring's `SecurityContext` with the caller's authorities. What's missing is enforcing this on `TransferController`.

`AccountController` is your reference — it already checks for `SCOPE_accounts:write` in the authority list. Replicate the same two-step pattern for transfers.

**Task:**
1. Add a `transfers:write` scope check at the top of `createTransfer`, following the pattern in `AccountController.createAccount`
2. Remove the `@Disabled` annotation from these two tests in `TransferControllerQuestTest.java` and confirm they now pass:
   - `createTransfer_returns401_whenNoToken`
   - `createTransfer_returns403_whenScopeMissing`

**Definition of Done:**
```bash
./gradlew :src:bank-api:test --tests "*.TransferControllerQuestTest"
```
Both un-disabled tests pass.

<details>
<summary>Hints</summary>

**How authorities work in Spring Security**

`SecurityConfig` maps JWT `scope` claims to `GrantedAuthority` objects prefixed with `SCOPE_`. So a token with scope `transfers:write` produces the authority `SCOPE_transfers:write`. Check for it like this:

```java
boolean hasScope = auth.getAuthorities().stream()
    .anyMatch(a -> a.getAuthority().equals("SCOPE_transfers:write"));
if (!hasScope) {
    return ResponseEntity.status(403)
        .body(Map.of("message", "Missing required scope: transfers:write"));
}
```

**Why not `@PreAuthorize`?**

You could use `@PreAuthorize("hasAuthority('SCOPE_transfers:write')")`, which is cleaner for simple cases. For this quest, replicating the explicit in-method check from `AccountController` is intentional — it teaches you what the annotation does under the hood.

</details>

---

### Quest 3: Implement the Controller Action

**File:** `src/bank-api/src/main/java/com/javatraining/bank/controller/TransferController.java`

**Context:**
This is the core of the quest. The `createTransfer` method currently returns `501 Not Implemented`. You need to replace it with a real implementation: verify ownership of the source account, call the service layer, map domain exceptions to HTTP responses, log the result, and return a `201 Created`.

**Task:**
The method has 5 guided `TODO` comments. Each points to the exact line in `AccountController` that demonstrates the pattern:

1. **Scope check:** Copy the `transfers:write` authority check from Quest 2 (or use `@PreAuthorize`).
2. **Ownership check:** Fetch the source account via `bankService.getAccount(request.fromAccountId())`. If `auth.getName()` does not match `account.getOwner()`, return `403 Forbidden`.
3. **Call service & map exceptions:**
   ```
   AccountNotFoundException      → 404 NotFound
   InsufficientFundsException    → 422 UnprocessableEntity
   IllegalArgumentException      → 400 BadRequest
   ```
   Anything else should propagate — don't swallow unknown exceptions.
4. **Log & return:** Use `log.info(...)` to log the successful transfer ID. Return `ResponseEntity.created(location).body(transfer)` with a `Location: /v1/transfers/{id}` header.

**Definition of Done:**
Remove `@Disabled` from these two tests and confirm they pass:
- `createTransfer_returns201_whenValid`
- `createTransfer_returns400_whenArgumentInvalid`

```bash
./gradlew :src:bank-api:test --tests "*.TransferControllerQuestTest"
```

<details>
<summary>Hints</summary>

**422 vs 400 for business rule violations**

`400 Bad Request` means the request is syntactically or structurally wrong (missing field, wrong type). `422 Unprocessable Entity` means the request is valid JSON but the server cannot process it due to a business rule (insufficient funds, account frozen). Use `ResponseEntity.unprocessableEntity()` for `InsufficientFundsException`.

**`auth.getName()` vs `auth.getPrincipal()`**

`Authentication.getName()` returns the `sub` claim from the JWT — this is the username. `Authentication.getPrincipal()` returns the underlying principal object (a `Jwt` when using Spring Security OAuth2). For ownership checks, `auth.getName()` is what you want.

**`ResponseEntity.unprocessableEntity()`**

Spring's `ResponseEntity` has a convenience builder for 422:
```java
return ResponseEntity.unprocessableEntity()
    .body(Map.of("message", ex.getMessage()));
```

</details>

---

### Quest 4: Write Integration Tests

**File:** `src/bank-api/src/test/java/com/javatraining/bank/controller/TransferControllerQuestTest.java`

**Context:**
The Spring way to test HTTP controllers is `@WebMvcTest` + `MockMvc`. It loads only the web layer (no database, no service), wires Spring Security, and lets you make real HTTP-style requests through the full filter chain — authentication, routing, model binding, exception handling — all in process.

This is meaningfully different from calling controller methods directly. A `MockMvc` test proves the endpoint actually works end-to-end through the pipeline.

`AccountControllerTest` is your reference — study the `@WebMvcTest` setup, `MockBean`, `user().authorities(...)`, and JSON path assertions.

**Task:**
Three tests in `TransferControllerQuestTest.java` are marked `@Disabled("TODO")`. Implement each one:

- `createTransfer_returns403_whenCallerIsNotOwner` — mock `bankService.getAccount` to return an account owned by `"bob"`, sign the request as `"alice"`, expect `403`
- `createTransfer_returns404_whenSourceAccountNotFound` — mock `bankService.getAccount` to throw `AccountNotFoundException`, expect `404`
- `createTransfer_returns422_whenInsufficientFunds` — mock `getAccount` to succeed (alice owns it), mock `createTransfer` to throw `InsufficientFundsException`, expect `422`

**Definition of Done:**
Remove `@Disabled` from all three tests. All tests pass, 0 skipped:

```bash
./gradlew :src:bank-api:test --tests "*.TransferControllerQuestTest"
```

<details>
<summary>Hints</summary>

**Mock setup pattern**

```java
when(bankService.getAccount(fromId))
    .thenReturn(account("bob", "500.00")); // alice is the caller → 403

when(bankService.getAccount(fromId))
    .thenThrow(new AccountNotFoundException(fromId)); // → 404

when(bankService.createTransfer(any(), any(), any()))
    .thenThrow(new InsufficientFundsException(fromId, BigDecimal.TEN, new BigDecimal("100"))); // → 422
```

**Sending a JSON body with MockMvc**

```java
var body = objectMapper.writeValueAsString(new CreateTransferRequest(fromId, toId, amount));
mockMvc.perform(post("/v1/transfers")
    .with(user("alice").authorities(new SimpleGrantedAuthority("SCOPE_transfers:write")))
    .contentType(MediaType.APPLICATION_JSON)
    .content(body))
    .andExpect(status().isCreated());
```

</details>

---

### Bonus Quest 1: Check Account Balance CLI

**File:** `src/bank-cli/src/main/java/com/javatraining/bank/cli/AccountCommand.java`

**Context:**
The `account get <id>` CLI command is stubbed with a TODO. The `AccountCommand.List` and `AccountCommand.Create` commands are fully working — use them as your reference.

**Task:**
Implement `AccountCommand.Get.call()`:
1. Call `requireToken()` — return `1` if not set
2. Build `GET /v1/accounts/{id}` with the bearer token via `withBearer(...)`
3. Send with `CLIENT.send(...)` and print the response body
4. Print a friendly message on `404`, return exit code `1` on any `4xx`/`5xx`

**Definition of Done:**
```bash
make infra-up && make db-migrate
# In a separate terminal: make run-bank-api
export BANK_TOKEN=$(curl -s -X POST http://localhost:8080/v1/token \
  -H "Content-Type: application/json" \
  -d '{"userName":"alice","scopes":["accounts:write","transfers:write"]}' \
  | jq -r '.token')

# alice → 00000000-0000-0000-0000-000000000001
./gradlew :src:bank-cli:run --args="account get 00000000-0000-0000-0000-000000000001"
```

---

### Bonus Quest 2: Authenticated Transfer CLI

**File:** `src/bank-cli/src/main/java/com/javatraining/bank/cli/TransferCommand.java`

**Context:**
`TransferCommand.Create` has a TODO. The `transfer list` command and `account create` are fully working references.

**Task:**
Implement `TransferCommand.Create.call()`:
1. Call `requireToken()` — return `1` if not set
2. Build a JSON body: `{"fromAccountId":"...","toAccountId":"...","amount":...}`
3. `POST /v1/transfers` with `Content-Type: application/json` and `Authorization: Bearer <token>`
4. Print the response body. On `401` print a helpful "token expired" message; on `403` print "missing transfers:write scope".

**Definition of Done:**
```bash
./gradlew :src:bank-cli:run --args="transfer create \
  --from 00000000-0000-0000-0000-000000000001 \
  --to   00000000-0000-0000-0000-000000000002 \
  --amount 100"

# Verify graceful error when token is absent:
unset BANK_TOKEN
./gradlew :src:bank-cli:run --args="transfer create \
  --from 00000000-0000-0000-0000-000000000001 \
  --to   00000000-0000-0000-0000-000000000002 \
  --amount 100"
# Expected: "Error: BANK_TOKEN environment variable is not set."
```

---

## Next Step

Mastered the standard REST controller? Now learn how to orchestrate long-running, failure-tolerant business processes.

→ **[Temporal Durable Transfer Quest](../temporal/README.md)**

---

*`AccountController` is your ultimate reference guide. When in doubt, read it.*
