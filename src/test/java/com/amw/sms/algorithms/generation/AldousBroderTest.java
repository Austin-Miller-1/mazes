package com.amw.sms.algorithms.generation;

/**
 * Tests for AldousBroder.
 */
public class AldousBroderTest extends MazeGenAlgorithmTest{
    /**
     * {@inheritDoc}
     */
    @Override
    MazeGenAlgorithm getGenAlgorithmUnderTest() {
        return new AldousBroder();
    }
}
