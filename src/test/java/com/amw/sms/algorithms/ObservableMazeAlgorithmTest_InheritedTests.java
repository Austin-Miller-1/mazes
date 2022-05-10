package com.amw.sms.algorithms;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for ObservableMazeAlgorithm subclasses. Includes basic tests to assert that the observers are
 * notified like expected. More complicated cases should be in the algorithm tests themselves.
 */
@ExtendWith(MockitoExtension.class)
public abstract class ObservableMazeAlgorithmTest_InheritedTests {
    @Mock 
    private MazeAlgorithmObserver mockObserver;

    //Objects under test
    private ObservableMazeAlgorithm observableAlgorithm;

    @BeforeEach
    protected void beforeEach(){
        this.observableAlgorithm = getAlgorithmUnderTest();
        this.observableAlgorithm.attach(mockObserver);
    }    

    /**
     * Returns instance of algorithm under test. For test subclasses.
     * @return Instance of algorithm to test.
     */
    protected abstract ObservableMazeAlgorithm getAlgorithmUnderTest();

    /**
     * Invokes the algorithm under test. For test subclasses. 
     * This should invoke the same instance of the algorithm that is returned by
     * {@link ObservableMazeAlgorithmTest_InheritedTests#getAlgorithmUnderTest()}.
     * Algorithm is expected to be invoked on data that is expected to have at least a few steps, i.e. not a 1-by-1 grid.
     */
    protected abstract void invokeAlgorithm();

    @Test
    void testAlgorithm_whenInvoked_notifiesStartOneTime(){
        invokeAlgorithm();

        Mockito.verify(mockObserver, times(1))
            .algorithmStarted();
    } 

    @Test
    void testAlgorithm_whenInvoked_notifiesFinishedOneTime(){
        invokeAlgorithm();

        Mockito.verify(mockObserver, times(1))
            .algorithmFinished();
    } 

    @Test
    void testAlgorithm_whenInvoked_notifiesCompletedStepAtLeastOnce(){
        invokeAlgorithm();

        Mockito.verify(mockObserver, atLeastOnce())
            .algorithmCompletedStep();
    }
}
