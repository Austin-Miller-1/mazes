package com.amw.sms.mazes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.amw.sms.algorithms.AlgorithmFactory;
import com.amw.sms.algorithms.Dijkstra;
import com.amw.sms.algorithms.generation.MazeGenAlgorithm;
import com.amw.sms.grid.CellDistances;
import com.amw.sms.grid.GridAnimator;
import com.amw.sms.grid.GridAnimatorFactory;
import com.amw.sms.grid.GridFactory;
import com.amw.sms.mazes.goals.MazeGoalBuilderFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Integration unit tests for MazeBuilder. Uses actual Grid functionality underneath.
 * Algorithmic components are currently mocked in these tests.
 */
@ExtendWith(MockitoExtension.class)
public class MazeBuilderTest_Integration {
    @Mock 
    private AlgorithmFactory mockAlgorithmFactory;

    @Mock
    private MazeGenAlgorithm mockMazeGenAlgorithm;

    @Mock
    private Dijkstra mockDijkstra;

    @Mock
    private CellDistances mockCellDistances;

    @Mock
    private GridAnimatorFactory mockGridAnimatorFactory;

    @Mock
    private GridAnimator mockGridAnimator;

    private GridFactory gridFactory;
    private MazeGoalBuilderFactory goalBuilderFactory;

    @BeforeEach
    void beforeEach(){
        //Mocks
        Mockito.when(mockAlgorithmFactory.getGenerationAlgorithm())
           .thenReturn(mockMazeGenAlgorithm);

        Mockito.when(mockAlgorithmFactory.getDijkstra())
           .thenReturn(mockDijkstra);

        Mockito.when(mockDijkstra.getDistances(any(), any()))
            .thenReturn(mockCellDistances);

        Mockito.when(mockGridAnimatorFactory.create(any(), any()))
            .thenReturn(mockGridAnimator);

        //Actual factories
        this.gridFactory = new GridFactory();
        this.goalBuilderFactory = new MazeGoalBuilderFactory();
    }

    /*Size tests */
    @Test
    void testWithSize_returnsMazeWithExpectedRowAndColumnCount() throws InvalidMazeException {
        final var maze = new MazeBuilder(this.gridFactory, this.goalBuilderFactory, this.mockAlgorithmFactory, this.mockGridAnimatorFactory)
            .withSize(5, 6)
            .build();

        final var grid = maze.getGrid();
        assertEquals(5, grid.getRowCount());
        assertEquals(6, grid.getColumnCount());
    }

    /* Setting entrances and exits */
    @Test
    void testStartFrom_andTestWithSize_returnsMazeThatStartsWithSpecifiedCell() throws InvalidMazeException {
        final var maze = new MazeBuilder(this.gridFactory, this.goalBuilderFactory, this.mockAlgorithmFactory, this.mockGridAnimatorFactory)
            .withSize(5, 5)
            .startFrom(1, 3)
            .build();

        final var startCell = maze.getStartCell();
        assertEquals(1, startCell.getRowPosition());
        assertEquals(3, startCell.getColumnPosition());
    }

    @Test
    void testEndAt_andTestWithSize_returnsMazeThatEndsWithSpecifiedCell() throws InvalidMazeException {
        final var maze = new MazeBuilder(this.gridFactory, this.goalBuilderFactory, this.mockAlgorithmFactory, this.mockGridAnimatorFactory)
            .withSize(5, 5)
            .endAt(1, 3)
            .build();

        final var endCell = maze.getEndCell();
        assertEquals(1, endCell.getRowPosition());
        assertEquals(3, endCell.getColumnPosition());
    }

    /* Non-set entrances and exits */
    @Test
    void testBuild_whenNoStartProvided_returnsMazeThatStartsWithFirstCell() throws InvalidMazeException {
        final var maze = new MazeBuilder(this.gridFactory, this.goalBuilderFactory, this.mockAlgorithmFactory, this.mockGridAnimatorFactory)
            .withSize(5, 5)
            .build();

        final var startCell = maze.getStartCell();
        assertEquals(0, startCell.getRowPosition());
        assertEquals(0, startCell.getColumnPosition());
    }
    

    @Test
    void testBuild_whenNoEndProvided_returnsMazeWithLastCellAsExit() throws InvalidMazeException {
        final var rowCount = 4;
        final var colCount = 5;
        
        final var maze = new MazeBuilder(this.gridFactory, this.goalBuilderFactory, this.mockAlgorithmFactory, this.mockGridAnimatorFactory)
            .withSize(rowCount, colCount)
            .build();

        final var endCell = maze.getEndCell();
        assertEquals(rowCount-1, endCell.getRowPosition());
        assertEquals(colCount-1, endCell.getColumnPosition());
    }
}
