package com.javatraining.basics.testing;

import java.util.ArrayList;
import java.util.List;

/** Simple calculator class used as a subject for JUnit 5 testing demonstrations. */
public class Calculator {

  /** Returns the sum of two integers. */
  public int add(int a, int b) {
    return a + b;
  }

  /** Returns the difference of two integers. */
  public int subtract(int a, int b) {
    return a - b;
  }

  /**
   * Divides a by b.
   *
   * @throws ArithmeticException if b is zero
   */
  public double divide(double a, double b) {
    if (b == 0) {
      throw new ArithmeticException("Cannot divide by zero");
    }
    return a / b;
  }

  /**
   * Returns true if n is a prime number.
   *
   * @param n integer to test
   */
  public boolean isPrime(int n) {
    if (n < 2) return false;
    if (n == 2) return true;
    if (n % 2 == 0) return false;
    for (int i = 3; i * i <= n; i += 2) {
      if (n % i == 0) return false;
    }
    return true;
  }

  /**
   * Returns the first {@code n} Fibonacci numbers starting from 0.
   *
   * @param n number of Fibonacci numbers to generate
   * @return list of Fibonacci numbers
   */
  public List<Integer> fibonacciSequence(int n) {
    List<Integer> result = new ArrayList<>();
    if (n <= 0) return result;
    result.add(0);
    if (n == 1) return result;
    result.add(1);
    for (int i = 2; i < n; i++) {
      result.add(result.get(i - 1) + result.get(i - 2));
    }
    return result;
  }
}
