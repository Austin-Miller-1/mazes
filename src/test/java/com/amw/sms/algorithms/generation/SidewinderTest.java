package com.amw.sms.algorithms.generation;

/**
 * Tests for Sidewinder.
 */
public class SidewinderTest extends MazeGenAlgorithmTest {
    @Override
    MazeGenAlgorithm getAlgorithmUnderTest() {
        return new Sidewinder();
    }
}
