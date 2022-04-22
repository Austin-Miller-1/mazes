package com.amw.sms.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for Pair.
 */
public class PairTest{
    @Test
    void testGetFirst_returnsFirstValueInPair(){
        final var pair = new Pair<Integer, String>(100, "hello");
        assertEquals(100, pair.getFirst());
    }

    @Test
    void testGetSecond_returnsSecondValueInPair(){
        final var pair = new Pair<Object, Character>(System.out, 'a');
        assertEquals('a', pair.getSecond());
    }
}
