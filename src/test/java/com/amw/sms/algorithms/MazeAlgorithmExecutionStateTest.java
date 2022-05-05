package com.amw.sms.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import com.amw.sms.grid.Cell;
import com.amw.sms.grid.CellGroup;
import com.amw.sms.grid.CellPath;
import com.amw.sms.grid.griddata.GridData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MazeAlgorithmExecutionStateTest {
    @Mock
    private GridData mockGridData;

    @Mock
    private List<Cell> mockPath;

    @Mock private CellGroup mockGroup1;
    @Mock private CellGroup mockGroup2;

    @Mock private CellPath mockPath1;
    @Mock private CellPath mockPath2;

    //Test instance
    private MazeAlgorithmExecutionState executionState;

    @BeforeEach
    void beforeEach(){
        executionState = new MazeAlgorithmExecutionState();
    }

    //Grid data
    @Test
    void testGetAlgorithmGridData_whenUnset_returnsEmptyOptional() {
        assertTrue(executionState.getAlgorithmGridData().isEmpty());
    }

    @Test
    void testSetAlgorithmGridData_andGetAlgorithmGridData_returnsSetGridData() {
        executionState.setAlgorithmGridData(mockGridData);
        assertEquals(mockGridData, executionState.getAlgorithmGridData().get());
    }

    //Cell groups
    @Test
    void testGetAlgorithmCellGroups_whenUnset_returnsEmptyList() {
        assertTrue(executionState.getAlgorithmCellGroups().isEmpty());
    }

    @Test
    void testAddAlgorithmCellGroup_andGetAlgorithmCellGroups_returnsListOfAllAddedGroups(){
        executionState.addAlgorithmCellGroup(mockGroup1);
        executionState.addAlgorithmCellGroup(mockGroup2);

        assertEquals(Arrays.asList(mockGroup1, mockGroup2), executionState.getAlgorithmCellGroups());
    }    

    //Cell paths
    @Test
    void testGetAlgorithmPaths_whenUnset_returnsEmptyList() {
        assertTrue(executionState.getAlgorithmPaths().isEmpty());
    }

    @Test
    void testAddAlgorithmPaths_andGetAlgorithmPaths_returnsListOfAllAddedPaths() {
        executionState.addAlgorithmPath(mockPath1);
        executionState.addAlgorithmPath(mockPath2);

        assertEquals(Arrays.asList(mockPath1, mockPath2), executionState.getAlgorithmPaths());
    }

    //Clear
    @Test
    void testClear_andSetAlgorithmGridData_andGetAlgorithmGridData_clearsSetGridData() {
        executionState.setAlgorithmGridData(mockGridData);
        executionState.clear();
        assertTrue(executionState.getAlgorithmGridData().isEmpty());
    }

    @Test
    void testClear_andAddAlgorithmPath_andGetAlgorithmGridPath_clearsSetGridPath() {
        executionState.addAlgorithmPath(mockPath1);
        executionState.clear();
        assertTrue(executionState.getAlgorithmPaths().isEmpty());
    }

    @Test
    void testClear_whenNotSet_doesNotCauseException() {
        executionState.clear();
        executionState.clear();
    }
}
