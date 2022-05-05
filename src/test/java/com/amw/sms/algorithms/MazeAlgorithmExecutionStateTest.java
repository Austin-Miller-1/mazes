package com.amw.sms.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amw.sms.grid.data.GridData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MazeAlgorithmExecutionStateTest {
    @Mock
    private GridData mockGridData;

    //Test instance
    private MazeAlgorithmExecutionState executionState;

    @BeforeEach
    void beforeEach(){
        executionState = new MazeAlgorithmExecutionState();
    }

    //Grid data
    @Test
    void testGetAlgorithmGridData_whenUnset_returnsEmptyOptional() {
        assertTrue(executionState.getGridData().isEmpty());
    }

    @Test
    void testSetAlgorithmGridData_andGetAlgorithmGridData_returnsSetGridData() {
        executionState.setGridData(mockGridData);
        assertEquals(mockGridData, executionState.getGridData().get());
    }

    //Clear
    @Test
    void testClear_andSetAlgorithmGridData_andGetAlgorithmGridData_clearsSetGridData() {
        executionState.setGridData(mockGridData);
        executionState.clear();
        assertTrue(executionState.getGridData().isEmpty());
    }

    @Test
    void testClear_whenNotSet_doesNotCauseException() {
        executionState.clear();
        executionState.clear();
    }
}
