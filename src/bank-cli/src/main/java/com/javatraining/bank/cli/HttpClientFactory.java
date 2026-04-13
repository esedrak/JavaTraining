package com.javatraining.bank.cli;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

/** Shared utilities for building authenticated HTTP requests. */
class HttpClientFactory {

  static final String API_URL =
      System.getenv().getOrDefault("BANK_API_URL", "http://localhost:8080");

  static final HttpClient CLIENT =
      HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

  /**
   * Returns the Bearer token from the {@code BANK_TOKEN} environment variable, or prints an error
   * and returns {@code null} if not set.
   */
  static String requireToken() {
    var token = System.getenv("BANK_TOKEN");
    if (token == null || token.isBlank()) {
      System.err.println("Error: BANK_TOKEN environment variable is not set.");
      System.err.println("  Run: export BANK_TOKEN=$(curl -s -X POST ...) ");
      return null;
    }
    return token;
  }

  /** Adds {@code Authorization: Bearer <token>} to the request builder. */
  static HttpRequest.Builder withBearer(HttpRequest.Builder builder, String token) {
    return builder.header("Authorization", "Bearer " + token);
  }

  private HttpClientFactory() {}
}
