package com.amw.sms.util;

/**
 * Simple data class for containing a pair of values of any type.
 */
public class Pair<T, S> {
    private final T first;
    private final S second;

    /**
     * Constructs new pair of values.
     * @param first First value in the pair.
     * @param second Second value in the pair.
     */
    public Pair(final T first, final S second){
        this.first = first;
        this.second = second;
    }

    /**
     * Get the first value in the pair.
     * @return First value in the pair.
     */
    public T getFirst(){
        return this.first;
    }

    /**
     * Get the second value in the pair.
     * @return Second value in the pair.
     */
    public S getSecond(){
        return this.second;
    }
}
