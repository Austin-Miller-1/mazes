package com.amw.sms.mazes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;

import com.amw.sms.algorithms.AlgorithmFactory;
import com.amw.sms.algorithms.Dijkstra;
import com.amw.sms.algorithms.generation.MazeGenAlgorithm;
import com.amw.sms.grid.Cell;
import com.amw.sms.grid.CellDistances;
import com.amw.sms.grid.CellPath;
import com.amw.sms.grid.Grid;
import com.amw.sms.grid.GridAnimator;
import com.amw.sms.grid.GridAnimatorFactory;
import com.amw.sms.grid.GridFactory;
import com.amw.sms.grid.data.SimpleGridDataLayer;
import com.amw.sms.mazes.goals.MazeGoalBuilderFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Integration unit tests for MazeBuilder and Maze classes. Uses actual Maze, Grid, and GridData functionality underneath.
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

    //Objects under test using above mocks
    private GridFactory gridFactory;
    private MazeGoalBuilderFactory goalBuilderFactory;
    private MazeBuilder builder;

    //Reused values in test
    private final int expectedSolutionLayerPosition = 1;    //The expected position of the solution layer within the grid data


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

        //Actual objects
        this.gridFactory = new GridFactory();
        this.goalBuilderFactory = new MazeGoalBuilderFactory();
        this.builder = new MazeBuilder(this.gridFactory, this.goalBuilderFactory, this.mockAlgorithmFactory, this.mockGridAnimatorFactory);
    }

    //Size tests
    @Test
    void testWithSize_returnsMazeWithExpectedRowAndColumnCount() throws InvalidMazeException {
        final var maze = builder
            .withSize(5, 6)
            .build();

        final var grid = maze.getGrid();
        assertEquals(5, grid.getRowCount());
        assertEquals(6, grid.getColumnCount());
    }

    //Setting entrances and exits
    @Test
    void testStartFrom_andTestWithSize_returnsMazeThatStartsWithSpecifiedCell() throws InvalidMazeException {
        final var maze = builder
            .withSize(5, 5)
            .startFrom(1, 3)
            .build();

        final var startCell = maze.getStartCell();
        assertEquals(1, startCell.getRowPosition());
        assertEquals(3, startCell.getColumnPosition());
    }

    @Test
    void testEndAt_andTestWithSize_returnsMazeThatEndsWithSpecifiedCell() throws InvalidMazeException {
        final var maze = builder
            .withSize(5, 5)
            .endAt(1, 3)
            .build();

        final var endCell = maze.getEndCell();
        assertEquals(1, endCell.getRowPosition());
        assertEquals(3, endCell.getColumnPosition());
    }

    //Non-set entrances and exits
    @Test
    void testBuild_whenNoStartProvided_returnsMazeThatStartsWithFirstCell() throws InvalidMazeException {
        final var maze = builder
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
        
        final var maze = builder
            .withSize(rowCount, colCount)
            .build();

        final var endCell = maze.getEndCell();
        assertEquals(rowCount-1, endCell.getRowPosition());
        assertEquals(colCount-1, endCell.getColumnPosition());
    }

    //
    // Constructed Maze tests
    //

    //
    @Test
    void testBuild_andStartFrom_andEndAt_returnsMazeWithStartAndEndCellSetAtTopLayerWithinGridData() throws InvalidMazeException{
        final var maze = builder
            .withSize(4, 5)
            .startFrom(1, 1)
            .endAt(2, 2)
            .build();

        final var grid = maze.getGrid();
        final var gridData = grid.getGridData();
        final var topLayer = gridData.getActiveLayer(0).get();
        assertEquals(Maze.ENTRANCE_START_CONTENTS, topLayer.getCellContents(grid.getCell(1, 1).get()).get());
        assertEquals(Maze.ENTRANCE_EXIT_CONTENTS, topLayer.getCellContents(grid.getCell(2, 2).get()).get());
    }

    /**
     * Test helper - get a list of cells from the grid to be treated as a solution.
     * Starts with grid's first cell. Ends with grid's last cell. All other cells in list are cells
     * whose row position is equal to the col position, that is start, (1,1), (2,2), (3,3), end.
     * @param grid Grid. Grid must be at least a 4 by 4 grid.
     * @return List of cells to be treated as sample solution within tests
     */
    private List<Cell> getSampleSolutionList(Grid grid){
        return Arrays.asList(
            grid.getFirstCell(),
            grid.getCell(1,1).get(),
            grid.getCell(2,2).get(),
            grid.getCell(3,3).get(),
            grid.getLastCell()
        ); 
    }

    @Test
    void testApplySolution_whenSolutionNotAlreadySet_addsNewLayerWithSolutionUnderStartAndEndLayerInGridData() throws InvalidMazeException{
        final var maze = builder
            .withSize(4, 5)
            .build();

        final var grid = maze.getGrid();
        final var sampleSolutionList = getSampleSolutionList(grid);
        final var sampleSolution = new CellPath("solution", sampleSolutionList);

        //Method under test
        maze.applySolution(sampleSolution);

        //Assert start and end is still at top
        final var gridData = grid.getGridData();
        assertEquals(Maze.ENTRANCE_START_CONTENTS, gridData.getPrimaryCellContents(grid.getFirstCell()).get());
        assertEquals(Maze.ENTRANCE_EXIT_CONTENTS, gridData.getPrimaryCellContents(grid.getLastCell()).get());

        //Assert solution cells are all set like expected
        sampleSolutionList.forEach(cell -> {
            assertTrue(gridData.getCellColors(cell).get(expectedSolutionLayerPosition).isPresent());
        });

        //Assert cell outside of solution is not set as expected
        assertTrue(gridData.getCellColors(grid.getCell(1, 2).get()).get(expectedSolutionLayerPosition).isEmpty());
    }

    @Test
    void testApplySoution_whenSolutionAlreadySet_updatesExistingLayerInGridDataWithSolution() throws InvalidMazeException{
        final var maze = builder
            .withSize(4, 5)
            .build();

        final var grid = maze.getGrid();
        final var sampleSolutionList1 = getSampleSolutionList(grid);
        final var sampleSolution1 = new CellPath("solution", sampleSolutionList1);

        //Solution 2 contains NO cells from solution 1 (see getSampleSolutionList )
        final var sampleSolutionList2 = Arrays.asList(
            grid.getCell(1,0).get(),
            grid.getCell(0,1).get(),
            grid.getCell(1,2).get(),
            grid.getCell(2,3).get(),
            grid.getCell(3,2).get()
        );
        final var sampleSolution2 = new CellPath("solution2", sampleSolutionList2);

        //Method under test
        maze.applySolution(sampleSolution1);
        maze.applySolution(sampleSolution2);

        //Assert solution cells are all set like expected
        final var gridData = grid.getGridData();
        for(int index = 0; index < sampleSolutionList1.size(); index++){
            //Cells in new solution have color from solution layer
            assertTrue(gridData.getCellColors(sampleSolutionList2.get(index))
                .get(expectedSolutionLayerPosition).isPresent());

            //cells in old solution do not have color since they are not in new solution layer
            assertTrue(gridData.getCellColors(sampleSolutionList1.get(index))
                .get(expectedSolutionLayerPosition).isEmpty());
        }
    }

    @Test
    void testApplySolution_appliesSolutionAsDataMaskToGridData() throws InvalidMazeException{
        final var maze = builder
            .withSize(4, 5)
            .build();

        final var grid = maze.getGrid();
        final var sampleSolutionList = getSampleSolutionList(grid);
        final var sampleSolution = new CellPath("solution", sampleSolutionList);

        //Method under test
        maze.applySolution(sampleSolution);

        //New layer with data outside of mask
        final var sampleCell = grid.getCell(0,1).get();
        final var sampleGridDataLayer = new SimpleGridDataLayer("test");
        sampleGridDataLayer.setCellContents(sampleCell, "contents");
        final var gridData = grid.getGridData();
        gridData.addAtTop(sampleGridDataLayer);
        
        //Data outside of mask is NOT returned
        assertTrue(gridData.getPrimaryCellContents(sampleCell).isEmpty());
    }
}
