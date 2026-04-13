package com.javatraining.bank.controller;

import com.javatraining.bank.dto.TokenRequest;
import com.javatraining.bank.dto.TokenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Developer-only endpoint for generating JWT tokens with custom scopes.
 *
 * <p>Only active when the {@code dev} Spring profile is active (or no profile, i.e. default
 * development mode). Returns 404 in production.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * POST /v1/token
 * { "userName": "alice", "scopes": ["accounts:write", "transfers:write"] }
 * }</pre>
 */
@RestController
@RequestMapping("/v1/token")
@Tag(name = "Auth", description = "Development token generation (dev profile only)")
public class TokenController {

  private final String jwtSecret;
  private final Environment environment;

  public TokenController(@Value("${bank.jwt.secret}") String jwtSecret, Environment environment) {
    this.jwtSecret = jwtSecret;
    this.environment = environment;
  }

  @PostMapping
  @Operation(summary = "Generate a development JWT token")
  @ApiResponse(responseCode = "200", description = "Token generated")
  @ApiResponse(responseCode = "404", description = "Not available in production")
  public ResponseEntity<TokenResponse> generateToken(@RequestBody TokenRequest request) {
    // Only available outside of production
    if (environment.acceptsProfiles(Profiles.of("prod"))) {
      return ResponseEntity.notFound().build();
    }

    var key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

    var token =
        Jwts.builder()
            .subject(request.userName())
            .claim("scope", request.scopes())
            .expiration(new Date(System.currentTimeMillis() + 3_600_000L)) // 1 hour
            .signWith(key)
            .compact();

    return ResponseEntity.ok(new TokenResponse(token));
  }
}
