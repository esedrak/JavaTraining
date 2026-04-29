package com.javatraining.bank.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Permission evaluator for the Bank API.
 *
 * <p>Referenced from {@code @PreAuthorize} expressions as the {@code bankPermission} bean:
 *
 * <pre>
 * {@code @PreAuthorize("@bankPermission.canCreateAccount(authentication)")}
 * </pre>
 *
 * Centralises all permission checks in one testable place, keeping controllers free of
 * authority-string literals.
 */
@Component("bankPermission")
public class BankPermission {

  public boolean canCreateAccount(Authentication authentication) {
    return hasScope(authentication, "accounts:write");
  }

  public boolean canCreateTransfer(Authentication authentication) {
    return hasScope(authentication, "transfers:write");
  }

  private boolean hasScope(Authentication authentication, String scope) {
    return authentication.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("SCOPE_" + scope));
  }
}
