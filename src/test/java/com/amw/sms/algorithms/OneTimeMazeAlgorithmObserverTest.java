package com.amw.sms.algorithms;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for OneTimeMazeAlgorithmObserver.
 */
@ExtendWith(MockitoExtension.class)
public class OneTimeMazeAlgorithmObserverTest {
    @Mock
    private ObservableMazeAlgorithm mockObservableAlgorithm;
    
    private SampleOneTimeMazeAlgorithmObserverImpl algorithmObserver;

    @BeforeEach
    public void beforeEach(){
        algorithmObserver = new SampleOneTimeMazeAlgorithmObserverImpl(mockObservableAlgorithm);
    }

    @Test
    void testAlgorithmFinished_detachesSelfFromAlgorithm() {
        algorithmObserver.algorithmFinished();

        Mockito.verify(mockObservableAlgorithm, times(1))
            .detach(algorithmObserver);
    }

    //Template method test
    @Test
    void testAlgorithmFinished_callsAbstractOnAlgorithmFinishMethod() {
        algorithmObserver.algorithmFinished();
        assertTrue(algorithmObserver.wasOnAlgorithmFinishCalled());
    }

    @Test
    void testHasAlgorithmFinished_whenAlgorithmFinishedUncalled_returnsFalse() {
        assertFalse(algorithmObserver.hasAlgorithmFinished());
    }

    @Test
    void testHasAlgorithmFinished_andAlgorithmFinished_whenAlgorithmFinishedCalled_returnsTrue() {
        algorithmObserver.algorithmFinished();
        assertTrue(algorithmObserver.hasAlgorithmFinished());
    }
}
