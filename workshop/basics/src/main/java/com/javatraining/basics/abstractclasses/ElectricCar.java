package com.javatraining.basics.abstractclasses;

/** A concrete ElectricCar — provides the electric engine start behaviour. */
public class ElectricCar extends Vehicle {

  @Override
  public void startEngine() {
    System.out.println("Electric motor humming");
  }
}
