package com.amw.sms.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amw.sms.grid.Grid;

import org.junit.jupiter.api.Test;

public class GridTest {
    @Test
    void testConstructor_usingGetCell_innerCellsAreGivenExpectedNeighbors(){
        final var grid = new Grid(10, 10);

        final var rowPos = 4;
        final var colPos = 5;
        final var cellNeighbors = grid
            .getCell(rowPos, colPos)
            .get()
            .getNeighbors();

        assertEquals(4, cellNeighbors.size());
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos-1, colPos).get()));
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos+1, colPos).get()));
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos, colPos-1).get()));
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos, colPos+1).get()));
    }

    @Test
    void testConstructor_usingGetCell_firstOuterCellIsGivenExpectedNeighbors(){
        final var grid = new Grid(10, 10);

        final var rowPos = 0;
        final var colPos = 0;
        final var cellNeighbors = grid
            .getCell(rowPos, colPos)
            .get()
            .getNeighbors();

        assertEquals(2, cellNeighbors.size());
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos+1, colPos).get()));
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos, colPos+1).get()));
    }

    @Test
    void testConstructor_usingGetCell_lastOuterCellIsGivenExpectedNeighbors(){
        final var grid = new Grid(10, 10);

        final var rowPos = 9;
        final var colPos = 9;
        final var cellNeighbors = grid
            .getCell(rowPos, colPos)
            .get()
            .getNeighbors();

        assertEquals(2, cellNeighbors.size());
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos-1, colPos).get()));
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos, colPos-1).get()));
    }

    @Test
    void testGetCell_whenValidRowAndColumn_returnsExpectedCell(){
        final var grid = new Grid(10, 10);

        final var cell = grid.getCell(4, 6);

        assertTrue(cell.isPresent());
        assertEquals(4, cell.get().getRowPosition());
        assertEquals(6, cell.get().getColumnPosition());
    }

    @Test
    void testGetCell_whenFirstCell_returnsExpectedCell(){
        final var grid = new Grid(10, 10);

        final var cell = grid.getCell(0, 0);

        assertTrue(cell.isPresent());
        assertEquals(0, cell.get().getRowPosition());
        assertEquals(0, cell.get().getColumnPosition());
    }

    @Test
    void testGetCell_whenLastCell_returnsExpectedCell(){
        final var grid = new Grid(10, 10);

        final var cell = grid.getCell(9, 9);

        assertTrue(cell.isPresent());
        assertEquals(9, cell.get().getRowPosition());
        assertEquals(9, cell.get().getColumnPosition());
    }

    @Test
    void testGetCell_whenRowPosNegative_returnsEmptyOptional(){
        final var grid = new Grid(10, 10);

        assertTrue(grid.getCell(-1, 6).isEmpty());
    }

    @Test
    void testGetCell_whenRowPosTooLarge_returnsEmptyOptional(){
        final var grid = new Grid(10, 10);

        assertTrue(grid.getCell(10, 6).isEmpty());
    }

    @Test
    void testGetCell_whenColPosNegative_returnsEmptyOptional(){
        final var grid = new Grid(10, 10);

        assertTrue(grid.getCell(4, -1).isEmpty());
    }

    @Test
    void testGetCell_whenColPosTooLarge_returnsEmptyOptional(){
        final var grid = new Grid(10, 10);

        assertTrue(grid.getCell(4, 10).isEmpty());
    }

    @Test
    void testGetRandomCell_usingGetCells_returnsCellFromGrid(){
        final var grid = new Grid(10, 10);

        assertTrue(grid.getCells().contains(grid.getRandomCell()));
    }


    @Test
    void testGetRandomCell_returnsRandomCellEachTime(){
        //Get 3 random cells. Assert that at least one of them is different ((1/200)^3 chance of failure)
        final var grid = new Grid(10, 20);
        final var cell1 = grid.getRandomCell();
        final var cell2 = grid.getRandomCell();
        final var cell3 = grid.getRandomCell();

        assertTrue(!cell1.equals(cell2) || !cell1.equals(cell3));
    }

    @Test
    void testGetCellCount_returnsCorrectCount(){
        assertEquals(4*15, (new Grid(4, 15)).getCellCount());
        assertEquals(9*7, (new Grid(9, 7)).getCellCount());
    }

    @Test
    void testGetRows_returnsListOfRows(){
        final var grid = new Grid(9, 10);

        final var rows = grid.getRows();

        assertEquals(9, rows.size());
        assertEquals(10, rows.get(0).size());
        assertEquals(grid.getCell(5, 9).get(), rows.get(5).get(9));
    }

    @Test
    void testGetCells_returnsListOfAllCells(){
        final var grid = new Grid(9, 10);

        final var cells = grid.getCells();

        assertEquals(9*10, cells.size());
        assertTrue(cells.contains(grid.getCell(5, 9).get()));
    }
}
