package com.javatraining.basics.dataclasses;

/**
 * A Person record with a custom instance method. Records can have additional methods beyond the
 * auto-generated ones.
 */
public record Person(String name, int age) {

  public String greeting() {
    return "Hi, I'm " + name;
  }

  public boolean isAdult() {
    return age >= 18;
  }
}
