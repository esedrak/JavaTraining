package com.javatraining.basics.abstractclasses;

/**
 * An abstract Vehicle demonstrating the Template Method pattern with a concrete method that cannot
 * be overridden and an abstract hook.
 */
public abstract class Vehicle {

  /** Abstract hook — each vehicle type defines its own engine-start behaviour. */
  public abstract void startEngine();

  /** Final concrete method — the fuel check process is fixed for all vehicles. */
  public final void checkFuel() {
    System.out.println("Checking fuel...");
  }

  /**
   * Template method — defines the start sequence. Subclasses customise via startEngine(), not
   * start() itself.
   */
  public void start() {
    checkFuel();
    startEngine();
  }
}
