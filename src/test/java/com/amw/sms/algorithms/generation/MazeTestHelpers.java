package com.amw.sms.algorithms.generation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import com.amw.sms.grid.Cell;
import com.amw.sms.grid.Grid;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * Class containing different helper functions for maze tests.
 */
public class MazeTestHelpers {
    /**
     * Recursively checks to see if there are any loops within the grid containing the provided cell.
     * Starts with a single cell, then visits all linked cells, then those linked cell's linked cells, and so on.
     * If there are any loops, this will throw an assertion error.
     * @param cell Cell to check.
     */
    void checkLinkedCellsForLoops(Cell cell){
        checkLinkedCellsForLoops(cell, null, new HashSet<Cell>());
    }

    /**
     * Recursively checks to see if there are any loops within the grid containing the provided cells.
     * Given a next-cell, check all of the cells linked to it to see if they have already been visited, 
     * excluding the previous-cell that was used to get the next-cell (this link does not indicate a loop).
     * If any linked-cell (besides the previous one) has already been visited, this indicates that there is a loop.
     * @param nextCell The next cell to check.
     * @param previousCell The previous cell that brought us to the next cell via a link.
     * @param visitedCells Set of all cells that have already been visited and should not be visited again.
     */
    void checkLinkedCellsForLoops(Cell nextCell, Cell previousCell, Set<Cell> visitedCells){
        assertFalse(visitedCells.contains(nextCell));
        visitedCells.add(nextCell);

        nextCell.getLinks()
            .forEach(cell -> {
                if(!cell.equals(previousCell)){
                    checkLinkedCellsForLoops(cell, nextCell, visitedCells);
                }
            });
    }

    //
    //Test the helper with the simplest cases
    //
    @Test
    void testCheckLinkedCellsForLoops_whenThereAreNoLoops_doesNotThrowException(){
        final var grid = new Grid(2,2);
        grid.getCell(0, 0).get().link(grid.getCell(0,1).get());
        grid.getCell(0, 0).get().link(grid.getCell(1,0).get());
        grid.getCell(1, 0).get().link(grid.getCell(1,1).get());

        checkLinkedCellsForLoops(grid.getFirstCell());
    }

    @Test
    void testCheckLinkedCellsForLoops_whenThereIsALoop_throwsAssertionFailedException(){
        final var grid = new Grid(2,2);
        grid.getCell(0, 0).get().link(grid.getCell(0,1).get());
        grid.getCell(0, 0).get().link(grid.getCell(1,0).get());
        grid.getCell(1, 0).get().link(grid.getCell(1,1).get());
        grid.getCell(1, 1).get().link(grid.getCell(0,1).get());

        assertThrows(AssertionFailedError.class, () -> {
            checkLinkedCellsForLoops(grid.getFirstCell());
        });
    }
}
