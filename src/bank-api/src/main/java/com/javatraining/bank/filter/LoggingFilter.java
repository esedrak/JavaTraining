package com.javatraining.bank.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Logs every HTTP request with method, path, status code, and elapsed time in milliseconds. Runs
 * after {@link CorrelationIdFilter} so the MDC correlation ID appears in each log entry.
 */
@Component
@Order(2)
public class LoggingFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    var start = System.currentTimeMillis();
    try {
      chain.doFilter(request, response);
    } finally {
      var elapsed = System.currentTimeMillis() - start;
      log.info(
          "{} {} → {} ({}ms)",
          request.getMethod(),
          request.getRequestURI(),
          response.getStatus(),
          elapsed);
    }
  }
}
