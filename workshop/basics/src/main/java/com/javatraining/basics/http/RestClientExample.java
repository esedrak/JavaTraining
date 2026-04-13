package com.javatraining.basics.http;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Demonstrates Spring 6's {@link RestClient} API — a modern, fluent alternative to {@code
 * RestTemplate}.
 *
 * <p>Note: The methods in this class show the API surface without making real HTTP calls, since no
 * server is running during tests. Use {@code @SpringBootTest} with a running server, or WireMock,
 * for end-to-end RestClient testing.
 */
@Component
public class RestClientExample {

  private final RestClient restClient;

  public RestClientExample(RestClient.Builder builder) {
    this.restClient = builder.baseUrl("https://api.example.com").build();
  }

  /**
   * Example of a synchronous GET that deserializes the response body.
   *
   * @param path URL path (e.g. "/users/1")
   * @return the response body as a String
   */
  public String fetchBody(String path) {
    // Fluent RestClient API:
    //   get()          — select HTTP method
    //   .uri(path)     — set the URI
    //   .retrieve()    — perform the request
    //   .body(...)     — deserialize the response body
    return restClient.get().uri(path).retrieve().body(String.class);
  }

  /**
   * Example of a POST with a request body.
   *
   * @param path URL path
   * @param payload request body
   * @return response body as String
   */
  public String postBody(String path, String payload) {
    return restClient.post().uri(path).body(payload).retrieve().body(String.class);
  }
}
