package com.javatraining.basics.abstractclasses;

/**
 * A concrete Dog — provides the specific sound behaviour.
 */
public class Dog extends Animal {

    public Dog(String name) {
        super(name, "Woof");
    }

    @Override
    public String speak() {
        return sound + "!";
    }
}
