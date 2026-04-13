package com.javatraining.basics.parameters;

/**
 * Topic 2: Parameters
 *
 * Demonstrates:
 * - Varargs
 * - Method overloading
 * - final parameters
 * - Builder pattern as alternative to named/default parameters
 */
public class Parameters {

    // --- Varargs ---

    public static int sum(int... numbers) {
        int total = 0;
        for (int n : numbers) {
            total += n;
        }
        return total;
    }

    // --- Method Overloading ---

    public static String format(String s) {
        return "[" + s + "]";
    }

    public static String format(String s, int n) {
        return "[" + s + ":" + n + "]";
    }

    public static String format(String s, String prefix) {
        return prefix + s;
    }

    // --- Final Parameters ---

    /**
     * final parameters cannot be reassigned within the method body.
     * This prevents accidental mutation of the parameter binding.
     */
    public static String greet(final String name) {
        // name = "other";  // would not compile
        return "Hello, " + name + "!";
    }

    public static int doubleIt(final int value) {
        return value * 2;
    }

    // --- Builder Pattern (Config) ---

    /**
     * Config is a value-holding class with many fields.
     * Java has no named or default parameters, so the Builder pattern
     * is the idiomatic alternative.
     */
    public static final class Config {

        private final String host;
        private final int port;
        private final int timeout;

        private Config(Builder builder) {
            this.host = builder.host;
            this.port = builder.port;
            this.timeout = builder.timeout;
        }

        public String getHost() { return host; }
        public int getPort() { return port; }
        public int getTimeout() { return timeout; }

        @Override
        public String toString() {
            return "Config{host='" + host + "', port=" + port + ", timeout=" + timeout + "}";
        }

        public static final class Builder {

            // Defaults
            private String host = "localhost";
            private int port = 8080;
            private int timeout = 30;

            public Builder host(String host) {
                this.host = host;
                return this;
            }

            public Builder port(int port) {
                this.port = port;
                return this;
            }

            public Builder timeout(int timeout) {
                this.timeout = timeout;
                return this;
            }

            public Config build() {
                return new Config(this);
            }
        }
    }
}
