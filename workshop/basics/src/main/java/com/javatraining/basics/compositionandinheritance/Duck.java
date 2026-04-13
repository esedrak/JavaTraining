package com.javatraining.basics.compositionandinheritance;

/**
 * Duck implements three interfaces via composition.
 *
 * <p>Composition over inheritance: rather than inheriting from FlyingAnimal, SwimmingAnimal, etc.
 * (which Java doesn't even allow for multiple concrete bases), Duck simply implements the relevant
 * interface contracts directly.
 */
public class Duck implements Flyable, Swimmable, Walkable {

  private final String name;

  public Duck(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public void fly() {
    System.out.println(name + " is flying");
  }

  @Override
  public void swim() {
    System.out.println(name + " is swimming");
  }

  @Override
  public void walk() {
    System.out.println(name + " is walking");
  }
}
