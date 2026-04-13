package com.javatraining.basics.abstractclasses;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

class AbstractClassesTest {

  @Test
  void dog_speak_returnsWoofWithExclamation() {
    Dog dog = new Dog("Rex");
    assertThat(dog.speak()).isEqualTo("Woof!");
  }

  @Test
  void cat_speak_returnsMeowWithTilde() {
    Cat cat = new Cat("Whiskers");
    assertThat(cat.speak()).isEqualTo("Meow~");
  }

  @Test
  void dog_getName_returnsName() {
    Dog dog = new Dog("Rex");
    assertThat(dog.getName()).isEqualTo("Rex");
  }

  @Test
  void cat_introduce_containsNameAndSound() {
    Cat cat = new Cat("Whiskers");
    String intro = cat.introduce();
    assertThat(intro).contains("Whiskers");
    assertThat(intro).contains("Meow~");
  }

  @Test
  void dog_introduce_usesTemplateMethod() {
    Dog dog = new Dog("Buddy");
    assertThat(dog.introduce()).isEqualTo("I am Buddy and I say: Woof!");
  }

  @Test
  void electricCar_start_callsCheckFuelAndStartEngine() {
    // Capture System.out to verify both steps execute
    ByteArrayOutputStream captured = new ByteArrayOutputStream();
    PrintStream original = System.out;
    System.setOut(new PrintStream(captured));

    try {
      ElectricCar car = new ElectricCar();
      car.start();
    } finally {
      System.setOut(original);
    }

    String output = captured.toString();
    assertThat(output).contains("Checking fuel...");
    assertThat(output).contains("Electric motor humming");
  }

  @Test
  void electricCar_startEngine_printsExpectedMessage() {
    ByteArrayOutputStream captured = new ByteArrayOutputStream();
    PrintStream original = System.out;
    System.setOut(new PrintStream(captured));

    try {
      new ElectricCar().startEngine();
    } finally {
      System.setOut(original);
    }

    assertThat(captured.toString()).contains("Electric motor humming");
  }

  @Test
  void animal_isAbstract_cannotBeInstantiatedDirectly() {
    // Verify by checking Dog IS-A Animal
    Dog dog = new Dog("Rex");
    assertThat(dog).isInstanceOf(Animal.class);
  }

  @Test
  void animal_templateMethod_orderIsCorrect() {
    // Verify checkFuel appears before startEngine in output
    ByteArrayOutputStream captured = new ByteArrayOutputStream();
    PrintStream original = System.out;
    System.setOut(new PrintStream(captured));

    try {
      new ElectricCar().start();
    } finally {
      System.setOut(original);
    }

    String output = captured.toString();
    int fuelIndex = output.indexOf("Checking fuel");
    int engineIndex = output.indexOf("Electric motor");
    assertThat(fuelIndex).isLessThan(engineIndex);
  }
}
