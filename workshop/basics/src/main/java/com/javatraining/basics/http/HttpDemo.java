package com.javatraining.basics.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Demonstrates Java's built-in HTTP client introduced in Java 11 ({@code
 * java.net.http.HttpClient}).
 *
 * <p>The client supports both synchronous and asynchronous requests, HTTP/2 by default, and
 * configurable timeouts.
 */
public class HttpDemo {

  private final HttpClient client;

  public HttpDemo(HttpClient client) {
    this.client = client;
  }

  /**
   * Creates an {@code HttpDemo} with a connect timeout.
   *
   * @param timeout the connect timeout
   * @return configured {@code HttpDemo} instance
   */
  public static HttpDemo withTimeout(Duration timeout) {
    HttpClient client =
        HttpClient.newBuilder()
            .connectTimeout(timeout)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    return new HttpDemo(client);
  }

  /**
   * Performs a synchronous HTTP GET and returns the response body as a string.
   *
   * @param url target URL
   * @return response body
   * @throws RuntimeException wrapping any {@link IOException} or {@link InterruptedException}
   */
  public String get(String url) {
    HttpRequest request =
        HttpRequest.newBuilder().uri(URI.create(url)).GET().timeout(Duration.ofSeconds(10)).build();
    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      return response.body();
    } catch (IOException e) {
      throw new RuntimeException("HTTP GET failed for URL: " + url, e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("HTTP GET interrupted for URL: " + url, e);
    }
  }

  /**
   * Performs an asynchronous HTTP GET, returning a {@link CompletableFuture} of the response body.
   *
   * @param url target URL
   * @return future resolving to the response body string
   */
  public CompletableFuture<String> getAsync(String url) {
    HttpRequest request =
        HttpRequest.newBuilder().uri(URI.create(url)).GET().timeout(Duration.ofSeconds(10)).build();
    return client
        .sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body);
  }
}
