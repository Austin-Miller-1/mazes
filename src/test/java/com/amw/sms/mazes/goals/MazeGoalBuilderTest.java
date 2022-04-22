package com.amw.sms.mazes.goals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import com.amw.sms.algorithms.AlgorithmFactory;
import com.amw.sms.algorithms.Dijkstra;
import com.amw.sms.grid.Cell;
import com.amw.sms.grid.CellDistances;
import com.amw.sms.grid.Grid;
import com.amw.sms.mazes.InvalidMazeException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for MazeGoalBuilder class.
 */
@ExtendWith(MockitoExtension.class)
public class MazeGoalBuilderTest {
    @Mock
    private AlgorithmFactory mockAlgorithmFactory;

    @Mock
    private Dijkstra mockDijkstra;

    @Mock 
    private CellDistances mockCellDistances;

    @Mock
    private Grid mockGrid;

    @Mock private Cell mockStartCell;
    @Mock private Cell mockEndCell;
    @Mock private Cell mockRandomCell;
    @Mock private Cell mockCell;
    @Mock private Cell mockFurthestCell;

    private void mockStartCell(){
        Mockito.when(mockGrid.getFirstCell()).thenReturn(mockStartCell);
    }

    @Test
    void test_whenNoGoalTypeSpecified_usesTheExpectedDefaultGoalType(){
        mockStartCell();

        final var mazeGoal = new MazeGoalBuilder(mockAlgorithmFactory, mockGrid).atStart();
        assertEquals(MazeGoalType.GENERIC, mazeGoal.getType());
    }

    @Test
    void testEntrance_setsTheGoalTypeAsExpected(){
        mockStartCell();

        final var mazeGoal = new MazeGoalBuilder(mockAlgorithmFactory, mockGrid)
            .entrance()
            .atStart();
        assertEquals(MazeGoalType.ENTRANCE, mazeGoal.getType()); 
    }

    @Test
    void testExit_setsTheGoalTypeAsExpected(){
        mockStartCell();

        final var mazeGoal = new MazeGoalBuilder(mockAlgorithmFactory, mockGrid)
            .exit()
            .atStart();
        assertEquals(MazeGoalType.EXIT, mazeGoal.getType());  
    }

    @Test
    void testAtStart_returnsGoalSetAtFirstCell(){
        mockStartCell();

        final var mazeGoal = new MazeGoalBuilder(mockAlgorithmFactory, mockGrid).atStart();
        assertEquals(mockStartCell, mazeGoal.getCell());
    }

    @Test
    void testAtEnd_returnsGoalSetAtLastCell(){
        Mockito.when(mockGrid.getLastCell()).thenReturn(mockEndCell);

        final var mazeGoal = new MazeGoalBuilder(mockAlgorithmFactory, mockGrid).atEnd();
        assertEquals(mockEndCell, mazeGoal.getCell());
    }

    @Test
    void testAtRandom_returnsGoalSetAtRandomCell(){
        Mockito.when(mockGrid.getRandomCell()).thenReturn(mockRandomCell);

        final var mazeGoal = new MazeGoalBuilder(mockAlgorithmFactory, mockGrid).atRandom();
        assertEquals(mockRandomCell, mazeGoal.getCell());
    }

    @Test
    void testAtPosition_whenPositionValid_returnsGoalSetAtSpecifiedCell() throws InvalidMazeException{
        Mockito.when(mockGrid.getCell(10, 20))
            .thenReturn(Optional.of(mockCell));

        final var mazeGoal = new MazeGoalBuilder(mockAlgorithmFactory, mockGrid).atPosition(10, 20);
        assertEquals(mockCell, mazeGoal.getCell());
    }

    @Test
    void testAtPosition_whenPositionInvalid_throwsInvalidMazeException(){
        Mockito.when(mockGrid.getCell(10, 20))
            .thenReturn(Optional.empty());

        final var exception = assertThrows(InvalidMazeException.class, () -> {
            new MazeGoalBuilder(mockAlgorithmFactory, mockGrid).atPosition(10, 20);
        });
        assertNotNull(exception.getMessage());
    }

    @Test
    void testFarthestFrom_returnsGoalSetAtCellFurthestFromProvidedCell(){
        Mockito.when(mockAlgorithmFactory.getDijkstra())
            .thenReturn(mockDijkstra);

        Mockito.when(mockDijkstra.getDistances(mockGrid, mockCell))
            .thenReturn(mockCellDistances);

        //Mock CellDistances such that mockFurthestCell is actually considered the furthest cell.
        Mockito.when(mockCellDistances.getFurthestCell())
            .thenReturn(mockFurthestCell);

        final var mazeGoal = new MazeGoalBuilder(mockAlgorithmFactory, mockGrid)
            .farthestFrom(mockCell);
        
        assertEquals(mockFurthestCell, mazeGoal.getCell());
    }
}
