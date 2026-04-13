package com.javatraining.basics.testing;

/** Utility math methods used to demonstrate parameterized testing with JUnit 5. */
public class MathUtils {

  /**
   * Computes the factorial of n.
   *
   * @param n non-negative integer
   * @throws IllegalArgumentException if n is negative
   */
  public int factorial(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("n must be non-negative, got: " + n);
    }
    int result = 1;
    for (int i = 2; i <= n; i++) {
      result *= i;
    }
    return result;
  }

  /**
   * Returns true if the given string is a palindrome, ignoring case.
   *
   * @param s string to test (may be null — null returns false)
   */
  public boolean isPalindrome(String s) {
    if (s == null) return false;
    String normalized = s.toLowerCase();
    String reversed = new StringBuilder(normalized).reverse().toString();
    return normalized.equals(reversed);
  }
}
