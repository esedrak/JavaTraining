package com.javatraining.basics.generics;

/**
 * A generic ordered pair of two values, possibly of different types.
 * Implemented as a record for conciseness.
 */
public record Pair<A, B>(A first, B second) {

    public static <A, B> Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    /**
     * Returns a new Pair with the elements in reversed order.
     */
    public Pair<B, A> swap() {
        return new Pair<>(second, first);
    }
}
