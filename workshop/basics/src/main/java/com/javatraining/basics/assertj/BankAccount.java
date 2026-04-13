package com.javatraining.basics.assertj;

/** Simple bank account used to demonstrate AssertJ assertions including exception testing. */
public class BankAccount {

  private final String id;
  private final String owner;
  private double balance;

  public BankAccount(String id, String owner, double initialBalance) {
    this.id = id;
    this.owner = owner;
    this.balance = initialBalance;
  }

  public String getId() {
    return id;
  }

  public String getOwner() {
    return owner;
  }

  public double getBalance() {
    return balance;
  }

  /**
   * Deposits the given amount into the account.
   *
   * @throws IllegalArgumentException if amount is zero or negative
   */
  public void deposit(double amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Deposit amount must be positive, got: " + amount);
    }
    balance += amount;
  }

  /**
   * Withdraws the given amount from the account.
   *
   * @throws IllegalArgumentException if amount is zero or negative, or if balance is insufficient
   */
  public void withdraw(double amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Withdrawal amount must be positive, got: " + amount);
    }
    if (amount > balance) {
      throw new IllegalArgumentException(
          "Insufficient balance: requested " + amount + " but balance is " + balance);
    }
    balance -= amount;
  }

  /** Returns true if the balance is negative. */
  public boolean isOverdrawn() {
    return balance < 0;
  }

  @Override
  public String toString() {
    return "BankAccount{id='" + id + "', owner='" + owner + "', balance=" + balance + "}";
  }
}
