package com.amw.mazes.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test Distances class.
 */
public class DistancesTest {
    Cell root;              //Root cell
    Cell cell1;             //Different cell1
    Cell cell2;             //Different cell2
    Distances distances;    //Distances instance using root cell. No distances set.

    @BeforeEach
    void beforeEach(){
        root = new Cell(1, 1);
        cell1 = new Cell(2, 2);
        cell2 = new Cell(3, 3);
        distances = new Distances(root);
    }

    @Test
    void testAddCell_andTestGetCells_returnsCollectionContainingCell() {
        distances.addCell(cell1, 3);
        assertTrue(distances.getCells().contains(cell1));
    }

    @Test
    void testAddCell_andTestGetDistance_returnsTheSetDistance() {
        distances.addCell(cell1, 3);
        assertEquals(3, distances.getDistance(cell1));

        distances.addCell(cell2, 100);
        assertEquals(100, distances.getDistance(cell2));
    }

    @Test
    void testGetDistance_returnsZeroForRootCell() {
        assertEquals(0, distances.getDistance(root));
    }

    @Test
    void testGetDistance_returnsNegativeOneForUnknownCell() {
        assertEquals(-1, distances.getDistance(new Cell(2, 2)));
    }

    @Test
    void testGetCells_andTestAddCell_returnsCollectionContainingAllAddedCells() {
        final var cell3 = new Cell(10, 10);
        distances.addCell(cell1, 1);
        distances.addCell(cell2, 2);
        distances.addCell(cell3, 3);

        final var cells = distances.getCells();
        assertEquals(4, cells.size());
        assertTrue(cells.contains(root));
        assertTrue(cells.contains(cell1));
        assertTrue(cells.contains(cell2));
        assertTrue(cells.contains(cell3));
    }

    @Test
    void testGetCells_andTestAddCell_andTestGetDistances_returnsCollectionContainingCellsThatHaveASetDistance() {
        final var cell3 = new Cell(10, 10);
        distances.addCell(cell1, 1);
        distances.addCell(cell2, 2);
        distances.addCell(cell3, 3);

        distances.getCells()
            .stream()
            .forEach((var cell) -> assertTrue(distances.getDistance(cell) >= 0));
    }
}
