package com.javatraining.basics.httptesting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Simple REST controller used to demonstrate {@code @WebMvcTest} and {@code MockMvc}. */
@RestController
@RequestMapping("/api")
public class GreetingController {

  /**
   * Returns a greeting for the given name.
   *
   * @param name path variable
   */
  @GetMapping("/greet/{name}")
  public ResponseEntity<String> greet(@PathVariable("name") String name) {
    return ResponseEntity.ok("Hello, " + name + "!");
  }

  /** Health check endpoint. */
  @GetMapping("/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("OK");
  }

  /**
   * Echoes the request body back to the caller.
   *
   * @param body the request body string
   */
  @PostMapping("/echo")
  public ResponseEntity<String> echo(@RequestBody String body) {
    return ResponseEntity.ok(body);
  }
}
