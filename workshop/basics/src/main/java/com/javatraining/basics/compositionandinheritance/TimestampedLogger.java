package com.javatraining.basics.compositionandinheritance;

/**
 * Decorator that wraps another Logger and prepends a timestamp.
 *
 * This is the Decorator pattern via composition:
 * TimestampedLogger HAS-A Logger (delegate), not IS-A ConsoleLogger.
 * This means any Logger implementation can be decorated, not just ConsoleLogger.
 */
public class TimestampedLogger implements Logger {

    private final Logger delegate;

    public TimestampedLogger(Logger delegate) {
        this.delegate = delegate;
    }

    @Override
    public void log(String message) {
        long timestamp = System.currentTimeMillis();
        delegate.log("[" + timestamp + "] " + message);
    }
}
