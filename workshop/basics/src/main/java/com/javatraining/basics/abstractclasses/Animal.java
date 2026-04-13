package com.javatraining.basics.abstractclasses;

/**
 * Topic 7: Abstract Classes
 *
 * Abstract classes differ from interfaces in that they can:
 * - Have instance fields and constructors
 * - Mix abstract and concrete methods
 * - Use the Template Method pattern (concrete method calls abstract methods)
 *
 * Use an abstract class when subclasses share state or a partial implementation.
 * Use an interface when you only want to define a contract.
 */
public abstract class Animal {

    protected final String name;
    protected final String sound;

    protected Animal(String name, String sound) {
        this.name = name;
        this.sound = sound;
    }

    /**
     * Abstract method — each subclass must define how it speaks.
     */
    public abstract String speak();

    /**
     * Final method — subclasses cannot override getName().
     */
    public final String getName() {
        return name;
    }

    /**
     * Template Method pattern:
     * The algorithm skeleton is defined here; the abstract step (speak)
     * is deferred to subclasses.
     */
    public String introduce() {
        return "I am " + getName() + " and I say: " + speak();
    }
}
