package com.amw.sms.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;

import com.amw.sms.grid.CellGroup;
import com.amw.sms.grid.CellPath;
import com.amw.sms.grid.griddata.GridData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for ObservableMazeAlgorithm.
 */
@ExtendWith(MockitoExtension.class)
public class ObservableMazeAlgorithmTest {
    private SampleObservableMazeAlgorithmImpl sampleObservableAlgorithm;

    @Mock private MazeAlgorithmObserver mockObserver1;
    @Mock private MazeAlgorithmObserver mockObserver2;
    @Mock private MazeAlgorithmObserver mockObserver3;
    private List<MazeAlgorithmObserver> mockObservers;

    @Mock
    private GridData mockGridData;

    @Mock
    private CellGroup mockCellGroup;

    @Mock
    private CellPath mockCellPath;

    @BeforeEach
    public void beforeEach(){
        sampleObservableAlgorithm = new SampleObservableMazeAlgorithmImpl();
        mockObservers = Arrays.asList(mockObserver1, mockObserver2, mockObserver3);
    }

    @Test
    void testAttach_andStarted_attachedObserverIsNotified() {
        sampleObservableAlgorithm.attach(mockObserver1);
        sampleObservableAlgorithm.started();

        Mockito.verify(mockObserver1, times(1))
            .algorithmStarted();
    }

    @Test
    void testDetach_andAttach_andStarted_detachedObserverIsNotNotified() {
        sampleObservableAlgorithm.attach(mockObserver1);
        sampleObservableAlgorithm.attach(mockObserver2);

        //Method under test
        sampleObservableAlgorithm.detach(mockObserver1);

        sampleObservableAlgorithm.started();

        Mockito.verify(mockObserver1, times(0))
            .algorithmStarted();
    }

    @Test
    void testStarted_andAttach_allObserversAreNotified() {
        mockObservers.forEach(sampleObservableAlgorithm::attach);

        sampleObservableAlgorithm.started();

        mockObservers.forEach((var observer) -> {
            Mockito.verify(observer, times(1))
                .algorithmStarted();
        });
    }

    @Test
    void testCompletedStep_andAttach_allObserversAreNotified() {
        mockObservers.forEach(sampleObservableAlgorithm::attach);

        sampleObservableAlgorithm.completedStep();

        mockObservers.forEach((var observer) -> {
            Mockito.verify(observer, times(1))
                .algorithmCompletedStep();
        });
    }

    @Test
    void testFinished_andAttach_allObserversAreNotified() {
        mockObservers.forEach(sampleObservableAlgorithm::attach);

        sampleObservableAlgorithm.finished();

        mockObservers.forEach((var observer) -> {
            Mockito.verify(observer, times(1))
                .algorithmFinished();
        });
    }

    @Test
    void testFinished_andGetExecutionState_executionStateIsClearedOnFinish(){
        sampleObservableAlgorithm.setDataInExecutionState(mockGridData, mockCellGroup, mockCellPath);

        sampleObservableAlgorithm.finished();

        final var actualExecutionState = sampleObservableAlgorithm.getExecutionState();
        assertTrue(actualExecutionState.getAlgorithmGridData().isEmpty());
        assertTrue(actualExecutionState.getAlgorithmCellGroups().isEmpty());
        assertTrue(actualExecutionState.getAlgorithmPaths().isEmpty());
    }

    @Test
    void testGetExecutionState_returnsAlgorithmExecutionState(){
        sampleObservableAlgorithm.setDataInExecutionState(mockGridData, mockCellGroup, mockCellPath);
        
        final var actualExecutionState = sampleObservableAlgorithm.getExecutionState();
        assertEquals(mockGridData, actualExecutionState.getAlgorithmGridData().get());
        assertEquals(mockCellGroup, actualExecutionState.getAlgorithmCellGroups().get(0));
        assertEquals(mockCellPath, actualExecutionState.getAlgorithmPaths().get(0));
    }
}
