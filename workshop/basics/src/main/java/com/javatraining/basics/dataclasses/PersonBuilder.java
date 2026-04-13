package com.javatraining.basics.dataclasses;

/**
 * PersonBuilder demonstrates the manual Builder pattern for a class with many fields, which is the
 * idiomatic Java alternative to named or default constructor parameters.
 */
public final class PersonBuilder {

  private final String firstName;
  private final String lastName;
  private final String email;
  private final int age;

  private PersonBuilder(Builder builder) {
    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
    this.email = builder.email;
    this.age = builder.age;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public int getAge() {
    return age;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }

  @Override
  public String toString() {
    return "PersonBuilder{firstName='"
        + firstName
        + "', lastName='"
        + lastName
        + "', email='"
        + email
        + "', age="
        + age
        + "}";
  }

  public static final class Builder {

    private String firstName;
    private String lastName;
    private String email;
    private int age;

    public Builder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public Builder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder age(int age) {
      this.age = age;
      return this;
    }

    public PersonBuilder build() {
      return new PersonBuilder(this);
    }
  }
}
