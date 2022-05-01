package com.amw.sms.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map.Entry;

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

    @Test
    void testConstructor_andGetFirst_andGetSecond_constructsNewPairBasedOnMapEntryValues(){
        final var map = new HashMap<String, Object>();
        final var first = "test";
        final var second = 100;
        map.put(first, second);
        final Entry<String, Object> mapEntry = map.entrySet().iterator().next();
 
        //Method under test
        final var pair = new Pair<String, Object>(mapEntry);

        assertEquals(first, pair.getFirst());
        assertEquals(second, pair.getSecond());
    }
}
