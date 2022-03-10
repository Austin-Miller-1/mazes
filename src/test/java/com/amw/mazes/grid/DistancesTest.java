package com.amw.mazes.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Optional;

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

    @Test
    void testPathToGoal_whenNoPath_returnsEmptyList(){
        final var path = distances.pathToGoal(cell1);
        assertTrue(path.isEmpty());
    }

    @Test
    void testPathToGoal_whenTwoCellPath_returnsExpectedPathBetweenTwoCells(){
        root.link(cell1);
        root.setEast(Optional.of(cell1));
        distances.addCell(cell1, 1);

        final var expectedPath = Arrays.asList(root, cell1);
        assertEquals(expectedPath, distances.pathToGoal(cell1));
    }

    @Test
    void testPathToGoal_whenThreeCellPath_returnsExpectedPathBetweenThreeCells(){
        root.link(cell1);
        root.setEast(Optional.of(cell1));

        cell1.link(cell2);
        cell1.setSouth(Optional.of(cell2));

        distances.addCell(cell1, 1);
        distances.addCell(cell2, 2);

        final var expectedPath = Arrays.asList(root, cell1, cell2);
        assertEquals(expectedPath, distances.pathToGoal(cell2));
    }

    @Test
    void testPathToGoal_whenMutliplePathsToSameCell_returnsExpectedSmallestPath(){
        //Create simple square grid, 3 paths to same cell, 1 shorter than others
        final var cell3 = new Cell(4, 4);
      
        root.link(cell1);
        root.setEast(Optional.of(cell1));
        distances.addCell(cell1, 1);

        root.link(cell2);
        root.setEast(Optional.of(cell2));
        distances.addCell(cell2, 1);

        root.link(cell3);
        root.setEast(Optional.of(cell3));
        distances.addCell(cell3, 1);

        cell1.link(cell3);
        cell1.setSouth(Optional.of(cell3));

        cell2.link(cell3);
        cell2.setEast(Optional.of(cell3));

        final var expectedPath = Arrays.asList(root, cell3);
        assertEquals(expectedPath, distances.pathToGoal(cell3));
    }
}
