package com.javatraining.basics.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.http.HttpClient;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for the built-in Java HTTP client wrapper. */
@DisplayName("HTTP Client Tests")
class HttpTest {

  @Test
  @DisplayName("HttpClient builder creates a valid client instance")
  void httpClientBuilderCreatesValidInstance() {
    HttpClient client =
        HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    assertThat(client).isNotNull();
    assertThat(client.connectTimeout()).isPresent();
    assertThat(client.connectTimeout().get()).isEqualTo(Duration.ofSeconds(5));
  }

  @Test
  @DisplayName("HttpDemo.withTimeout creates a non-null instance")
  void withTimeoutCreatesInstance() {
    HttpDemo demo = HttpDemo.withTimeout(Duration.ofSeconds(3));
    assertThat(demo).isNotNull();
  }

  @Test
  @DisplayName("get with invalid URL throws RuntimeException")
  void getWithInvalidUrlThrowsRuntimeException() {
    HttpDemo demo = HttpDemo.withTimeout(Duration.ofSeconds(3));

    // An invalid URL should cause URI.create to throw IllegalArgumentException,
    // which is not caught and propagates directly.
    assertThatThrownBy(() -> demo.get("not a valid url"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("get with unreachable host throws wrapped RuntimeException")
  void getWithUnreachableHostThrowsWrappedRuntimeException() {
    HttpDemo demo = HttpDemo.withTimeout(Duration.ofMillis(500));

    assertThatThrownBy(() -> demo.get("http://localhost:19999/no-such-endpoint"))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("HTTP GET failed");
  }

  @Test
  @DisplayName("getAsync returns a non-null CompletableFuture")
  void getAsyncReturnsCompletableFuture() {
    HttpDemo demo = HttpDemo.withTimeout(Duration.ofSeconds(3));

    // Just verify the future is created — don't wait for it since no server is running
    assertThat(demo.getAsync("http://localhost:19999/path")).isNotNull();
  }
}
