package com.amw.sms.util;

import java.util.Map.Entry;

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
     * Constructs new pair of values based on existing map entry.
     * @param mapEntry Map entry containing both values of the pair.
     * The map entry's "key" is considered the first value in the pair.
     * The map entry's "value" is considered the second value in the pair.
     */
    public Pair(final Entry<T, S> mapEntry){
        this.first = mapEntry.getKey();
        this.second = mapEntry.getValue();
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
