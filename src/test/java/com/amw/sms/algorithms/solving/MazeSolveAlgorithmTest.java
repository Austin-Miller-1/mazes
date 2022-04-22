package com.amw.sms.algorithms.solving;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amw.sms.algorithms.AlgorithmFactory;
import com.amw.sms.grid.Grid;
import com.amw.sms.mazes.InvalidMazeException;
import com.amw.sms.mazes.Maze;
import com.amw.sms.mazes.goals.MazeGoal;
import com.amw.sms.mazes.goals.MazeGoalBuilder;
import com.amw.sms.util.Pair;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for all MazeSolveAlgorithm implementations.
 * For these tests to actually be meaningful and not built of very complicated
 * mocking-logic, almost all of the internal maze components are not mocked out.
 * As such, these can be seen as integration unit-tests. 
 */
@ExtendWith(MockitoExtension.class)
public abstract class MazeSolveAlgorithmTest {
    @Mock
    private AlgorithmFactory mockAlgorithmFactory;

    @Test
    void testGetSolution_returnsPathBetweenTwoConnectedCells() throws InvalidMazeException{
        final var grid = new Grid(10, 10);
        
        //Grid of 2 vertically placed cells 
        final var cell1 = grid.getCell(1, 1).get();
        final var cell2 = grid.getCell(2, 1).get();
        cell1.link(cell2);

        //Maze
        final var start = new MazeGoalBuilder(mockAlgorithmFactory, grid).atPosition(1, 1);
        final var end = new MazeGoalBuilder(mockAlgorithmFactory, grid).atPosition(2, 1);
        final var maze = new Maze(grid, new Pair<MazeGoal, MazeGoal>(start, end));

        //Test
        final var solution = this.getAlgorithmUnderTest().getSolution(maze);
        assertEquals(2, solution.size());
        assertEquals(cell1, solution.get(0));
        assertEquals(cell2, solution.get(1));
    }

    @Test
    void testGetSolution_whenMazeIsThreeConnectedCells_andStartAndEndAreOnOppositeEnds_returnsPathContainingAllCells() throws InvalidMazeException{
        final var grid = new Grid(10, 10);
        
        //Grid of 3 cells connected in L shape 
        final var cell1 = grid.getCell(1, 1).get();
        final var cell2 = grid.getCell(2, 1).get();
        final var cell3 = grid.getCell(1, 2).get();
        cell1.link(cell2);
        cell1.link(cell3);

        //Maze
        final var start = new MazeGoalBuilder(mockAlgorithmFactory, grid).atPosition(2, 1);
        final var end = new MazeGoalBuilder(mockAlgorithmFactory, grid).atPosition(1, 2);
        final var maze = new Maze(grid, new Pair<MazeGoal, MazeGoal>(start, end));

        //Test
        final var solution = this.getAlgorithmUnderTest().getSolution(maze);
        assertEquals(3, solution.size());
        assertEquals(cell2, solution.get(0));
        assertEquals(cell1, solution.get(1));
        assertEquals(cell3, solution.get(2));
    }

    @Test
    void testGetSolution_whenMazeIsThreeConnectedCells_andStartAndEndAreAdjacent_returnsPathContainingOnlyTwoCells() throws InvalidMazeException{
        final var grid = new Grid(10, 10);
        
        //Grid of 3 cells connected in L shape 
        final var cell1 = grid.getCell(1, 1).get();
        final var cell2 = grid.getCell(2, 1).get();
        final var cell3 = grid.getCell(1, 2).get();
        cell1.link(cell2);
        cell1.link(cell3);

        //Maze
        final var start = new MazeGoalBuilder(mockAlgorithmFactory, grid).atPosition(1, 1);
        final var end = new MazeGoalBuilder(mockAlgorithmFactory, grid).atPosition(2, 1);
        final var maze = new Maze(grid, new Pair<MazeGoal, MazeGoal>(start, end));

        //Test
        final var solution = this.getAlgorithmUnderTest().getSolution(maze);
        assertEquals(2, solution.size());
        assertEquals(cell1, solution.get(0));
        assertEquals(cell2, solution.get(1));
    }

    @Test
    void testGetSolution_whenGridContainsBranchingPaths_returnsStartAndEnd() throws InvalidMazeException{
        final var grid = new Grid(10, 10);
        
        //Grid of 6 cells connected in upside-down T shape 
        final var cell1 = grid.getCell(5, 5).get();
        final var cell2 = grid.getCell(4, 5).get();
        final var cell3 = grid.getCell(6, 5).get();
        final var cell4 = grid.getCell(7, 5).get();
        final var cell5 = grid.getCell(7, 4).get();
        final var cell6 = grid.getCell(7, 6).get();
        cell1.link(cell2);
        cell1.link(cell3);
        cell3.link(cell4);
        cell4.link(cell5);
        cell4.link(cell6);

        //Maze - start at bottom right, end at top
        final var start = new MazeGoalBuilder(mockAlgorithmFactory, grid).atPosition(7, 6);
        final var end = new MazeGoalBuilder(mockAlgorithmFactory, grid).atPosition(4, 5);
        final var maze = new Maze(grid, new Pair<MazeGoal, MazeGoal>(start, end));

        //Test
        final var solution = this.getAlgorithmUnderTest().getSolution(maze);
        assertEquals(5, solution.size());
        assertEquals(cell6, solution.get(0));
        assertEquals(cell4, solution.get(1));
        assertEquals(cell3, solution.get(2));
        assertEquals(cell1, solution.get(3));
        assertEquals(cell2, solution.get(4));
    }

    @Test
    void testGetSolution_whenImperfectMaze_returnsAnyValidPathFromStartToEnd() throws InvalidMazeException{
        //Imperfect maze (loop): Multiple paths to cell3. 
        final var grid = new Grid(10, 10);
        final var cell1 = grid.getCell(1, 1).get();
        final var cell2 = grid.getCell(1, 2).get();
        final var cell3 = grid.getCell(1, 3).get();
        cell1.link(cell2);
        cell2.link(cell3);
        cell1.link(cell3);

        final var start = new MazeGoalBuilder(mockAlgorithmFactory, grid).atPosition(1, 1);
        final var end = new MazeGoalBuilder(mockAlgorithmFactory, grid).atPosition(1, 3);
        final var maze = new Maze(grid, new Pair<MazeGoal, MazeGoal>(start, end));

        //Test
        final var solution = this.getAlgorithmUnderTest().getSolution(maze);
        assertEquals(cell1, solution.get(0));
        assertEquals(cell3, solution.get(solution.size()-1));

        //Asserting path is list of linked cells (skip first, already asserted that it's the start)
        for(int index = 1; index < solution.size(); index++){
            //Assert cell in path is linked to previous cell
            assertTrue(solution.get(index).isLinkedTo(solution.get(index-1)));
        }
    }

    /**
     * Get the specific algorithm instance to test.
     * @return Instance of MazeSolveAlgorithm subtype to test.
     */
    protected abstract MazeSolveAlgorithm getAlgorithmUnderTest();
}
