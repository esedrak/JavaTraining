package com.javatraining.basics.dataclasses;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Money POJO demonstrating manual equals/hashCode/toString and a static factory method.
 *
 * <p>When you cannot use a record (e.g., mutable state, inheritance), implement these methods
 * manually.
 */
public final class Money {

  private final BigDecimal amount;
  private final String currency;

  private Money(BigDecimal amount, String currency) {
    this.amount = Objects.requireNonNull(amount, "amount must not be null");
    this.currency = Objects.requireNonNull(currency, "currency must not be null");
  }

  public static Money of(BigDecimal amount, String currency) {
    return new Money(amount, currency);
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getCurrency() {
    return currency;
  }

  public Money add(Money other) {
    if (!this.currency.equals(other.currency)) {
      throw new IllegalArgumentException("Cannot add different currencies");
    }
    return new Money(this.amount.add(other.amount), this.currency);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Money money)) return false;
    return amount.compareTo(money.amount) == 0 && Objects.equals(currency, money.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount.stripTrailingZeros(), currency);
  }

  @Override
  public String toString() {
    return amount + " " + currency;
  }
}
