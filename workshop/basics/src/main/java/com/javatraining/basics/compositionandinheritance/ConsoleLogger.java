package com.javatraining.basics.compositionandinheritance;

/**
 * The base (concrete) component in the Decorator pattern.
 * Writes messages to standard output with a [LOG] prefix.
 */
public class ConsoleLogger implements Logger {

    @Override
    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}
