# Java/Spring Boot Training — Conversion Plan

## Context

Convert the existing C#/.NET training workshop into a **standalone Java/Spring Boot training repo** for students. No references to C#/.NET. This is a brand-new project that teaches equivalent concepts using idiomatic Java 21 and Spring Boot 3.x.

**Target Stack:** Java 21 LTS | Spring Boot 3.3.x | Spring Data JPA | Temporal Java SDK | Gradle Kotlin DSL | JUnit 5

**Reference:** The C#/.NET version lives at `~/Development/personal/CSharpTraining/`. Use it as a structural reference for content, challenge design, and documentation format — but produce a fully standalone Java repo with zero .NET references.

---

## Project Structure

```
JavaTraining/
├── build.gradle.kts                     # Root build (plugins, shared config)
├── settings.gradle.kts                  # Module declarations
├── gradle/                              # Gradle wrapper + version catalog
│   └── libs.versions.toml               # Centralized dependency versions
├── gradlew / gradlew.bat
├── PLAN.md
├── README.md
├── docker-compose.yaml                  # Postgres, Temporal, WireMock, Jaeger
├── Makefile                             # Wraps Gradle commands
├── .editorconfig
├── .gitignore
│
├── src/
│   ├── hello/                           # Simple app — warm-up
│   │   ├── build.gradle.kts
│   │   ├── Dockerfile
│   │   └── src/main/java/...
│   │
│   ├── bank-domain/                     # Plain Java entities & exceptions
│   │   └── build.gradle.kts
│   ├── bank-repository/                 # Spring Data JPA + Flyway
│   │   └── build.gradle.kts
│   ├── bank-service/                    # Business logic (@Service)
│   │   └── build.gradle.kts
│   ├── bank-api/                        # Spring Boot REST API
│   │   ├── build.gradle.kts
│   │   ├── Dockerfile
│   │   └── src/
│   │       ├── main/java/.../controller/, filter/, config/, dto/
│   │       └── main/resources/application.yml
│   ├── bank-cli/                        # picocli CLI
│   │   └── build.gradle.kts
│   │
│   ├── temporal-domain/                 # Workflow domain models
│   ├── temporal-workflows/              # @WorkflowInterface + @ActivityInterface
│   ├── temporal-worker/                 # Worker process
│   ├── temporal-client/                 # Workflow starter
│   │
│   └── shared/
│       ├── shared-api/
│       └── shared-http/
│
├── workshop/
│   ├── basics/                          # Module 2: 33 Java topics
│   │   ├── build.gradle.kts             # Shared deps (JUnit5, AssertJ, Mockito, JMH, Spring Boot Test)
│   │   ├── README.md
│   │   └── src/
│   │       ├── main/java/.../           # Topic implementations
│   │       └── test/java/.../           # Topic tests
│   │
│   ├── fundamentals/                    # Module 1: API concepts (docs only)
│   │   ├── README.md
│   │   ├── ApiDesign/
│   │   ├── ApiFundamentals/
│   │   ├── ApiLifecycleAndDeployment/
│   │   ├── SecurityAndObservability/
│   │   └── TheAgenticFuture/
│   │
│   └── challenges/
│       ├── README.md
│       ├── basics/                      # FixMe (8) + ImplMe (3)
│       ├── bank/                        # Bank Transfer Quest
│       └── temporal/                    # Durable Transfer Quest
│
├── migration/                           # Flyway SQL migrations
│   └── V001__seed_accounts.sql
├── wiremock/                            # Reuse as-is (language-agnostic)
├── docs/openapi/
└── .github/workflows/build.yml
```

---

## Technology Stack

| Purpose | Java/Spring Boot Equivalent |
|---------|---------------------------|
| Language | Java 21 LTS (preview features where useful) |
| Web Framework | Spring Boot 3.3.x + Spring MVC (`@RestController`) |
| Build System | **Gradle Kotlin DSL** (`build.gradle.kts`) |
| ORM | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| Migrations | Flyway (plain SQL) |
| DI Container | Spring IoC (`@Component`, `@Service`, `@Repository`) |
| Configuration | `application.yml` + `@ConfigurationProperties` |
| Logging | SLF4J + Logback (Spring Boot default) |
| JSON | Jackson |
| JWT Auth | Spring Security + jjwt (`io.jsonwebtoken`) |
| OpenAPI | SpringDoc OpenAPI (`springdoc-openapi-starter-webmvc-ui`) |
| CLI | picocli |
| Testing | JUnit 5 (Jupiter) |
| Assertions | AssertJ |
| Mocking | Mockito |
| Benchmarking | JMH (Java Microbenchmark Harness) |
| HTTP Client | Spring `RestClient` (6.1+) / `java.net.http.HttpClient` |
| HTTP Integration Test | `@SpringBootTest` + `MockMvc` / `TestRestTemplate` |
| Tracing | OpenTelemetry Java SDK + Micrometer |
| Background Tasks | `@Scheduled` / `@Async` / `TaskExecutor` |
| Temporal | `io.temporal:temporal-sdk` |
| Code Formatting | Spotless plugin + Google Java Format |

---

## Module 2 — Topic-by-Topic Mapping (33 topics)

Each topic gets: `README.md` (concept explanation + Mermaid diagrams + code examples + pitfalls + further reading), Java implementation files, and JUnit 5 tests.

| # | Java Topic | Key Concepts | Notes |
|---|-----------|-------------|-------|
| 1 | **PrimitivesAndObjects** | Primitives vs wrappers, autoboxing/unboxing, `==` vs `.equals()`, pass-by-value semantics, `Optional<T>`, String immutability | No structs/Span in Java. Focus on boxing and identity vs equality. |
| 2 | **Parameters** | Varargs (`T...`), method overloading (no default/named params in Java), `final` params, builder pattern as alternative | Java has no `ref`/`out`/`in`. |
| 3 | **DataClasses** | `record` (Java 16+), POJOs, Builder pattern (manual), `equals()`/`hashCode()` contracts, Jackson `@JsonProperty` | |
| 4 | **NullSafety** | `Optional<T>`, `Objects.requireNonNull()`, `@Nullable`/`@NonNull` annotations, `NullPointerException` prevention | |
| 5 | **DefaultAndStaticInterfaceMethods** | `default` methods, `static` interface methods, utility classes, method references | Java has no extension methods — this is the closest equivalent. |
| 6 | **Interfaces** | Interface declaration, `default`/`static` methods, `sealed` interfaces (17+), DI with Spring `@Component`/`@Service` | |
| 7 | **AbstractClasses** | `abstract` classes/methods, `final` methods, Template Method pattern, `@Override` | |
| 8 | **TypeCheckingAndCasting** | `instanceof`, pattern matching for `instanceof` (16+), explicit casts, `ClassCastException` | |
| 9 | **PatternMatching** | Pattern matching for `switch` (21), guarded patterns, `sealed` classes, record patterns, `_` unnamed | Java 21 finalized features. |
| 10 | **CompositionAndInheritance** | Composition over inheritance, delegation, Decorator pattern, classpath resources | |
| 11 | **Generics** | Bounded types `<T extends X>`, wildcards `? extends`/`? super`, **type erasure** (critical Java concept), generic methods/classes | Type erasure is the biggest Java-specific lesson here. |
| 12 | **ErrorHandling** | **Checked vs unchecked** exceptions, multi-catch, custom hierarchies, `throws` declaration, Result pattern | Checked exceptions are Java-specific and important. |
| 13 | **ResourceManagement** | `AutoCloseable`/`Closeable`, `try-with-resources`, `@PreDestroy` | |
| 14 | **DependencyInjection** | Spring IoC, `@Component`/`@Service`/`@Repository`, constructor injection, `@Bean` factories, `@Scope`, `@Qualifier` | |
| 15 | **Configuration** | `application.yml`, `@ConfigurationProperties`, `@Value`, Spring profiles, `Environment` | |
| 16 | **ScheduledTasks** | `@Scheduled`, `@EnableScheduling`, `@Async`, `TaskExecutor`, `CommandLineRunner` | |
| 17 | **StreamsApi** | `Stream<T>`, `filter/map/flatMap/collect/reduce`, `Collectors`, lazy evaluation, parallel streams | |
| 18 | **ReactiveStreams** | `Flow.Publisher`/`Subscriber` (Java 9), Project Reactor `Flux<T>`/`Mono<T>`, backpressure | |
| 19 | **ModernJavaFeatures** | Records (16+), sealed classes (17+), text blocks (15+), switch expressions (14+), `var` (10+), unnamed variables (22) | |
| 20 | **ProjectLayout** | Gradle project structure, `src/main/java`, `build.gradle.kts`, package conventions, access modifiers, JPMS basics | |
| 21 | **BuildSystems** | **Gradle Kotlin DSL vs Gradle Groovy vs Maven** — syntax comparison, use cases, performance, ecosystem, when to pick which | **NEW** topic. |
| 22 | **Initialization** | `static {}` initializer blocks, `@PostConstruct`, lazy init patterns, Spring bean lifecycle, class loading order | |
| 23 | **Testing** | JUnit 5: `@Test`, `@ParameterizedTest`, `@ValueSource`, `@CsvSource`, `@MethodSource`, `@Nested`, `@DisplayName` | |
| 24 | **AssertJ** | `assertThat().isEqualTo()`, collection/exception assertions, soft assertions | |
| 25 | **Mocking** | Mockito: `mock()`, `when().thenReturn()`, `verify()`, `@Mock`/`@InjectMocks`, argument captors | |
| 26 | **Http** | `@RestController`, `@GetMapping`/`@PostMapping`, `RestClient`, `@RequestBody`/`@PathVariable`, `ResponseEntity<T>` | |
| 27 | **HttpTesting** | `@SpringBootTest`, `MockMvc`, `@WebMvcTest`, `TestRestTemplate`, `@MockBean` | |
| 28 | **Benchmarking** | JMH: `@Benchmark`, `@BenchmarkMode`, `@State`, `@Setup`, `@Fork`, `@Warmup` | |
| 29 | **Concurrency** | **Virtual threads** (21), `CompletableFuture<T>`, `ExecutorService`, `synchronized`, `ReentrantLock`, `AtomicInteger`, `BlockingQueue<T>`, structured concurrency (preview) | Virtual threads are the headline feature. |
| 30 | **Context** | `ThreadLocal<T>`, `InheritableThreadLocal`, Spring `RequestContextHolder`, ScopedValues (preview), timeout patterns | |
| 31 | **BuildProfiles** | Gradle profiles, Spring `@Profile`, `System.getProperty()`/`getenv()`, runtime OS detection, feature flags | No `#if` preprocessor in Java — everything is runtime. |
| 32 | **AnnotationsAndReflection** | Built-in annotations (`@Override`, `@Deprecated`, `@FunctionalInterface`), custom annotations, `@Retention`/`@Target`, runtime reflection, Spring's annotation model | **NEW** — foundational to Java/Spring. |
| 33 | **FunctionalInterfaces** | `@FunctionalInterface`, `Predicate/Function/Consumer/Supplier`, method references, lambda expressions, `.andThen()`/`.compose()` | **NEW** — foundation for Streams & CompletableFuture. |

---

## Challenges (Adapted for Java)

### FixMe (8 bugs to debug)
1. Race condition — `count++` without `synchronized`/`AtomicInteger`
2. Virtual thread pinning — `CompletableFuture.join()` blocking on carrier thread
3. NullPointerException — dereference without null check
4. Off-by-one — loop bounds error
5. Resource leak — `FileInputStream` without try-with-resources
6. Mutable object aliasing — sharing mutable objects across threads
7. Connection leak in loop — `Connection` not closed
8. Swallowed exception — empty `catch (Exception e) {}`

### ImplMe (3 stubs to implement)
1. `CompletableFuture` pipeline with `Semaphore` concurrency limiting
2. Spring `OncePerRequestFilter` implementing correlation ID
3. Streams API banking queries (filter, groupBy, aggregate)

### Bank Transfer Quest (4 quests + 2 bonus)
Same structure as C# version, adapted for Spring Boot:
- Quest 1: Annotate with SpringDoc `@ApiResponse` annotations
- Quest 2: Wire Spring Security `@PreAuthorize` / `SecurityContext`
- Quest 3: Implement controller action with `@ExceptionHandler`
- Quest 4: Write integration tests with `@SpringBootTest` + `MockMvc`
- Bonus 1: picocli `account get` command
- Bonus 2: Authenticated transfer via picocli CLI

### Durable Transfer Quest (7 quests)
Same structure with Temporal Java SDK:
- Uses `@WorkflowInterface`/`@WorkflowMethod` instead of `[Workflow]`/`[WorkflowRun]`
- Uses `Workflow.newActivityStub()` instead of `Workflow.ExecuteActivityAsync()`
- Uses `TestWorkflowEnvironment` for testing
- Same PRD, adapted grading prompt

---

## Gradle Configuration

### `gradle/libs.versions.toml` (version catalog)
```toml
[versions]
spring-boot = "3.3.5"
temporal = "1.25.0"
picocli = "4.7.6"
assertj = "3.26.3"
jmh = "1.37"
springdoc = "2.6.0"
jjwt = "0.12.6"
flyway = "10.21.0"
spotless = "6.25.0"

[libraries]
spring-boot-starter-web = { module = "org.springframework.boot:spring-boot-starter-web" }
spring-boot-starter-data-jpa = { module = "org.springframework.boot:spring-boot-starter-data-jpa" }
spring-boot-starter-security = { module = "org.springframework.boot:spring-boot-starter-security" }
spring-boot-starter-test = { module = "org.springframework.boot:spring-boot-starter-test" }
postgresql = { module = "org.postgresql:postgresql" }
flyway-core = { module = "org.flywaydb:flyway-core", version.ref = "flyway" }
flyway-postgresql = { module = "org.flywaydb:flyway-database-postgresql", version.ref = "flyway" }
temporal-sdk = { module = "io.temporal:temporal-sdk", version.ref = "temporal" }
temporal-testing = { module = "io.temporal:temporal-testing", version.ref = "temporal" }
picocli = { module = "info.picocli:picocli", version.ref = "picocli" }
jjwt-api = { module = "io.jsonwebtoken:jjwt-api", version.ref = "jjwt" }
jjwt-impl = { module = "io.jsonwebtoken:jjwt-impl", version.ref = "jjwt" }
jjwt-jackson = { module = "io.jsonwebtoken:jjwt-jackson", version.ref = "jjwt" }
springdoc = { module = "org.springdoc:springdoc-openapi-starter-webmvc-ui", version.ref = "springdoc" }
assertj = { module = "org.assertj:assertj-core", version.ref = "assertj" }
jmh-core = { module = "org.openjdk.jmh:jmh-core", version.ref = "jmh" }
jmh-annprocess = { module = "org.openjdk.jmh:jmh-generator-annprocess", version.ref = "jmh" }

[plugins]
spring-boot = { id = "org.springframework.boot", version.ref = "spring-boot" }
spring-dependency-management = { id = "io.spring.dependency-management", version = "1.1.6" }
spotless = { id = "com.diffplug.spotless", version.ref = "spotless" }
jmh = { id = "me.champeau.jmh", version = "0.7.2" }
```

### Makefile targets
```makefile
build:          ./gradlew build
test:           ./gradlew test
run-bank-api:   ./gradlew :src:bank-api:bootRun
run-hello:      ./gradlew :src:hello:bootRun
run-bank-cli:   ./gradlew :src:bank-cli:run --args="$(ARGS)"
run-worker:     ./gradlew :src:temporal-worker:bootRun
run-client:     ./gradlew :src:temporal-client:run
infra-up:       docker compose up -d
infra-down:     docker compose down
db-migrate:     ./gradlew :src:bank-repository:flywayMigrate
fmt:            ./gradlew spotlessApply
lint:           ./gradlew spotlessCheck
clean:          ./gradlew clean
help:           @grep -E '^[a-zA-Z_-]+:' Makefile | sort
```

---

## Implementation Phases

| Phase | Effort | Description |
|-------|--------|-------------|
| 0 | Small | Scaffold Gradle multi-module, folder structure, wrapper, version catalog, `.gitignore`, `.editorconfig` |
| 1 | Small | Docker-compose (rename db to `javabank`), Makefile, Dockerfiles (`eclipse-temurin:21`), copy wiremock/migration |
| 2 | Small | Port Fundamentals docs (replace any .NET refs with Java/Spring equivalents in markdown) |
| 3 | **Large** | 33 Basics topics (code + tests + READMEs) |
| 4 | **Large** | Bank service (6 modules: domain, repo, service, api, cli, tests) |
| 5 | Medium | Temporal workflows (4 modules + tests) |
| 6 | Medium | Challenges (FixMe, ImplMe, Bank Quest, Temporal Quest) |
| 7 | Medium | All READMEs and documentation |
| 8 | Small | GitHub Actions CI/CD |

---

## CI/CD (`.github/workflows/build.yml`)

```yaml
name: Build & Test
on:
  push:
    branches: ["main", "develop"]
  pull_request:
    branches: ["main"]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'
      - uses: gradle/actions/setup-gradle@v4
      - run: ./gradlew build
      - run: ./gradlew spotlessCheck
      - uses: actions/upload-artifact@v4
        if: always()
        with:
          name: test-results
          path: '**/build/test-results/**/*.xml'
```

---

## Key Design Decisions

- **No Lombok** — Use Java 21 records + manual code. Training should teach actual Java.
- **Gradle Kotlin DSL** — Includes a BuildSystems topic comparing Gradle Kotlin vs Groovy vs Maven.
- **Spring Security** (not manual JWT filter) — idiomatic Spring approach, critical real-world skill.
- **Flyway** (not Liquibase) — plain SQL migrations, reuses existing seed SQL.
- **Virtual Threads** prominently featured — Java 21's headline concurrency feature.
- **No C#/.NET references anywhere** — fully standalone Java training.

## Verification

After each phase:
1. `./gradlew build` — compiles all modules
2. `./gradlew test` — all tests green
3. `./gradlew spotlessCheck` — formatting passes
4. `docker compose up -d && make db-migrate` — infrastructure works
5. `make run-bank-api` — API starts and responds to requests
