package com.amw.sms.algorithms.generation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amw.sms.algorithms.Dijkstra;
import com.amw.sms.algorithms.ObservableMazeAlgorithm;
import com.amw.sms.algorithms.ObservableMazeAlgorithmTest_InheritedTests;
import com.amw.sms.grid.Grid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Abstract test class containing tests for all MazeGenAlgorithm subclasses to inherit
 * in order to validate that these algorithms create mazes as expected.
 */
public abstract class MazeGenAlgorithmTest extends ObservableMazeAlgorithmTest_InheritedTests {
    private MazeTestHelpers testHelpers;

    //Objects under test
    private Grid grid;
    private Dijkstra dijk;
    private MazeGenAlgorithm genAlgorithm;

    @BeforeEach
    protected void beforeEach(){
        this.grid = new Grid(5, 5);
        this.dijk = new Dijkstra();
        this.genAlgorithm = this.getGenAlgorithmUnderTest();

        this.testHelpers = new MazeTestHelpers();

        super.beforeEach();
    }

    /**
     * {@inheritDoc}
     */
    protected ObservableMazeAlgorithm getAlgorithmUnderTest(){
        return this.genAlgorithm;
    }

    /**
     * {@inheritDoc}
     */
    protected void invokeAlgorithm(){
        this.genAlgorithm.apply(new Grid(2,2));
    }

    /**
     * Abstract method to be implemented by algorithm subclass tests.
     * @return Instance of the maze-gen algorithm to test.
     */
    abstract MazeGenAlgorithm getGenAlgorithmUnderTest(); 

    @Test
    void testApply_resultsInAGridWhereEachCellHasAMinimumOfOneLinkToAnotherCell(){
        genAlgorithm.apply(grid);

        grid.getCells()
            .forEach(cell -> {
                assertTrue(cell.getLinks().size() >= 1);
            });
    }
    
    @Test
    void testApply_viaDijkstra_resultsInAGridWhereEachCellHasAPathToEveryOtherCell() {
        //Method under test
        genAlgorithm.apply(grid);

        //Dijkstra for asserting
        final var distances = dijk.getDistances(grid, grid.getFirstCell());

        //Every cell should have a distance from a set root. If true, then all cells can be reached from the root.
        grid.getCells()
            .forEach(cell -> {
                assertTrue(distances.isDistanceSet(cell));
            });
    }

    //
    //Perfect maze tests
    //
    @Test
    void testApply_resultsInAGridWhereThereAreNoLoopsMakingMultiplePathsToTheSameCell(){
        genAlgorithm.apply(grid);

        testHelpers.checkLinkedCellsForLoops(grid.getFirstCell());
    }


}
