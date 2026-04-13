package com.javatraining.basics.abstractclasses;

/**
 * A concrete Cat — provides the specific sound behaviour.
 */
public class Cat extends Animal {

    public Cat(String name) {
        super(name, "Meow");
    }

    @Override
    public String speak() {
        return sound + "~";
    }
}
