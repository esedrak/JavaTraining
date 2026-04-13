package com.javatraining.basics.httptesting;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** Controller tests using {@code @WebMvcTest} and {@code MockMvc} — no running server required. */
@WebMvcTest(GreetingController.class)
@DisplayName("GreetingController MockMvc Tests")
class GreetingControllerTest {

  @Autowired MockMvc mockMvc;

  @Test
  @DisplayName("GET /api/greet/{name} returns 200 with greeting")
  void greetReturnsGreeting() throws Exception {
    mockMvc
        .perform(get("/api/greet/World"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello, World!"));
  }

  @Test
  @DisplayName("GET /api/health returns 200 with OK")
  void healthReturnsOk() throws Exception {
    mockMvc
        .perform(get("/api/health"))
        .andExpect(status().isOk())
        .andExpect(content().string("OK"));
  }

  @Test
  @DisplayName("POST /api/echo echoes the request body")
  void echoReturnsRequestBody() throws Exception {
    mockMvc
        .perform(post("/api/echo").contentType(MediaType.TEXT_PLAIN).content("test"))
        .andExpect(status().isOk())
        .andExpect(content().string("test"));
  }

  @Test
  @DisplayName("GET /api/greet/{name} with different name")
  void greetWithDifferentName() throws Exception {
    mockMvc
        .perform(get("/api/greet/Java"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello, Java!"));
  }
}
