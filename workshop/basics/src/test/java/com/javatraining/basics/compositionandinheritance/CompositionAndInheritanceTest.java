package com.javatraining.basics.compositionandinheritance;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class CompositionAndInheritanceTest {

    @Test
    void duck_implementsAllThreeInterfaces() {
        Duck duck = new Duck("Donald");
        assertThat(duck).isInstanceOf(Flyable.class);
        assertThat(duck).isInstanceOf(Swimmable.class);
        assertThat(duck).isInstanceOf(Walkable.class);
    }

    @Test
    void duck_fly_printsFlyingMessage() {
        Duck duck = new Duck("Donald");
        String output = captureOutput(duck::fly);
        assertThat(output).contains("Donald").contains("flying");
    }

    @Test
    void duck_swim_printsSwimmingMessage() {
        Duck duck = new Duck("Daffy");
        String output = captureOutput(duck::swim);
        assertThat(output).contains("Daffy").contains("swimming");
    }

    @Test
    void duck_walk_printsWalkingMessage() {
        Duck duck = new Duck("Huey");
        String output = captureOutput(duck::walk);
        assertThat(output).contains("Huey").contains("walking");
    }

    @Test
    void consoleLogger_log_printsWithPrefix() {
        ConsoleLogger logger = new ConsoleLogger();
        String output = captureOutput(() -> logger.log("test message"));
        assertThat(output).contains("[LOG]").contains("test message");
    }

    @Test
    void timestampedLogger_wrapsDelegate_prependsTimestamp() {
        // Use a capturing logger as delegate to inspect what gets passed
        StringBuilder captured = new StringBuilder();
        Logger capturingDelegate = message -> captured.append(message);

        TimestampedLogger timestampedLogger = new TimestampedLogger(capturingDelegate);
        timestampedLogger.log("hello");

        // The delegate receives the message with a timestamp prefix
        assertThat(captured.toString()).contains("hello");
        // Timestamp is a numeric prefix inside brackets
        assertThat(captured.toString()).matches(".*\\[\\d+\\].*hello.*");
    }

    @Test
    void timestampedLogger_wrapsConsoleLogger_noException() {
        ConsoleLogger console = new ConsoleLogger();
        TimestampedLogger timestamped = new TimestampedLogger(console);
        // Should complete without exception
        String output = captureOutput(() -> timestamped.log("wrapped"));
        assertThat(output).contains("wrapped");
    }

    @Test
    void composition_duck_usableAsEachInterface() {
        Duck duck = new Duck("Scrooge");

        // Use duck through each interface reference — demonstrates polymorphism
        Flyable flyable = duck;
        Swimmable swimmable = duck;
        Walkable walkable = duck;

        assertThat(flyable).isSameAs(duck);
        assertThat(swimmable).isSameAs(duck);
        assertThat(walkable).isSameAs(duck);
    }

    // --- Helper ---

    private String captureOutput(Runnable action) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(baos));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return baos.toString();
    }
}
