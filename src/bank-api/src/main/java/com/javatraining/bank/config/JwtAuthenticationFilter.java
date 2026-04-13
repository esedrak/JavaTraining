package com.javatraining.bank.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Validates a JWT Bearer token on every request and populates {@link
 * org.springframework.security.core.context.SecurityContext}.
 *
 * <p>Scope claims in the JWT become {@code SCOPE_<scope>} granted authorities, e.g. {@code
 * accounts:write} → {@code SCOPE_accounts:write}.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  private static final String BEARER_PREFIX = "Bearer ";

  private final SecretKey signingKey;

  public JwtAuthenticationFilter(@Value("${bank.jwt.secret}") String jwtSecret) {
    this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    var authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
      var token = authHeader.substring(BEARER_PREFIX.length());
      try {
        Claims claims =
            Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();

        var subject = claims.getSubject();
        @SuppressWarnings("unchecked")
        java.util.Collection<Object> rawScopes = (java.util.Collection<Object>) claims.get("scope");

        List<String> scopes =
            rawScopes != null
                ? rawScopes.stream().map(Object::toString).collect(Collectors.toList())
                : Collections.emptyList();

        var authorities =
            scopes.stream()
                .map(s -> new SimpleGrantedAuthority("SCOPE_" + s))
                .collect(Collectors.toList());

        var authentication = new UsernamePasswordAuthenticationToken(subject, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

      } catch (JwtException ex) {
        log.warn("JWT validation failed: {}", ex.getMessage());
        // Leave SecurityContext empty — downstream security config will reject the request
      }
    }

    filterChain.doFilter(request, response);
  }
}
