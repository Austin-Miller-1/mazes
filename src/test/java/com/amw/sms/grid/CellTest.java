package com.amw.sms.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import com.amw.sms.grid.Cell;

import org.junit.jupiter.api.Test;

/**
 * Test Cell class.
 */
public class CellTest {
    @Test
    void testLink_andTestIsLinkedTo_linkedCellsAreConsideredLinked() {
        //Test fixture
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(2, 2);

        //Method
        cell1.link(cell2);

        //Assertion
        assertTrue(cell1.isLinkedTo(cell2));
        assertTrue(cell2.isLinkedTo(cell1));
    }

    @Test
    void testUnlink_andTestIsLinkedTo_usingLink_unlinkedCellsAreNotConsideredLinked() {
        //Test fixture
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(2, 2);
        cell1.link(cell2);

        //Method
        cell1.unlink(cell2);

        //Assertion
        assertFalse(cell1.isLinkedTo(cell2));
        assertFalse(cell2.isLinkedTo(cell1));
    }

    @Test
    void testUnlink_andTestIsLinkedTo_usingLink_unlinkedCellsAreNotConsideredLinked_bidirectionalCheck() {
        //Test fixture
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(2, 2);
        cell1.link(cell2);

        //Method - unlink using cell2 specifically
        cell2.unlink(cell1);

        //Assertion
        assertFalse(cell1.isLinkedTo(cell2));
        assertFalse(cell2.isLinkedTo(cell1));
    }

    @Test
    void testUnlink_andTestGetLinks_usingLink_unlinkedCellsAreNotInLinkCollection() {
        //Test fixture
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(2, 2);
        cell1.link(cell2);

        //Method - unlink using cell2 specifically
        cell2.unlink(cell1);

        //Assertion
        assertFalse(cell1.getLinks().contains(cell2));
    }

    @Test
    void testGetLinks_usingLink_setOfAllLinkedCellsIsReturned() {
        //Test fixture
        final var cell = new Cell(1, 1);
        final var linkedCell1 = new Cell(2, 2);
        final var linkedCell2 = new Cell(3, 3);
        final var unlinkedCell = new Cell(4, 4);
        cell.link(linkedCell1);
        cell.link(linkedCell2);

        //Method
        final var links = cell.getLinks();

        //Assert
        assertEquals(2, links.size());
        assertTrue(links.contains(linkedCell1));
        assertTrue(links.contains(linkedCell2));
        assertFalse(links.contains(unlinkedCell));
    }

    @Test
    void testGetNeighbors_andTestSetters_collectionContainsAllNeighbors() {
        //Test fixture
        final var cell = new Cell(1, 1);
        final var northCell = new Cell(2, 2);
        final var eastCell = new Cell(3, 3);
        final var southCell = new Cell(4, 4);
        final var westCell = new Cell(5, 5);

        cell.setNorth(Optional.of(northCell));
        cell.setEast(Optional.of(eastCell));
        cell.setSouth(Optional.of(southCell));
        cell.setWest(Optional.of(westCell));
        
        //Method
        final var neighbors = cell.getNeighbors();

        //Assert
        assertEquals(4, neighbors.size());
        assertTrue(neighbors.contains(northCell));
        assertTrue(neighbors.contains(eastCell));
        assertTrue(neighbors.contains(southCell));
        assertTrue(neighbors.contains(westCell));
    }

    @Test
    void testGetNeighbors_andTestSetters_whenNeighborsAreUnset_collectionIsEmpty() {
        //Test fixture
        final var cell = new Cell(1, 1);

        //Method
        final var neighbors = cell.getNeighbors();

        //Assert
        assertTrue(neighbors.isEmpty());
    }

    @Test
    void testGetNeighbors_andTestSetters_whenOneNeighborIsUnset_collectionContainsAllActualNeighbors() {
        //Test fixture
        final var cell = new Cell(1, 1);
        final var northCell = new Cell(2, 2);
        final var westCell = new Cell(5, 5);

        cell.setNorth(Optional.of(northCell));
        cell.setWest(Optional.of(westCell));
        
        //Method
        final var neighbors = cell.getNeighbors();

        //Assert
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(northCell));
        assertTrue(neighbors.contains(westCell));
    }

    @Test
    void testGetNeighbors_andTestSetters_whenNeighborsAreEmpty_collectionDoesNotContainThem() {
        //Test fixture
        final var cell = new Cell(1, 1);

        cell.setNorth(Optional.ofNullable(null));
        cell.setEast(Optional.ofNullable(null));
        cell.setSouth(Optional.ofNullable(null));
        cell.setWest(Optional.ofNullable(null));
        
        //Method & Assert
        assertEquals(0, cell.getNeighbors().size());
    }

    @Test
    void testGetters(){
        //Test fixture
        final var cell = new Cell(10, 20);

        //Method & Assert
        assertEquals(10, cell.getRowPosition());
        assertEquals(20, cell.getColumnPosition());
    }

    @Test
    void testGetDistances_whenNoLinks_returnsDistancesContainingOnlyRootCell(){
        final var cell = new Cell(1, 1);
        final var distances = cell.getDistances();

        assertEquals(0, distances.getDistance(cell));
        final var indexedCells = distances.getCells(); 
        assertEquals(1, indexedCells.size());
        assertTrue(indexedCells.contains(cell));
    }

    @Test
    void testGetDistances_andTestLink_whenOneVerticalLink_returnsDistancesWhereLinkedCellHasDistanceOfOne(){
        //Grid of 2 vertically placed cells 
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(2, 1);
        cell1.link(cell2);
        cell1.setNorth(Optional.of(cell2));

        final var distances = cell1.getDistances();
        assertEquals(1, distances.getDistance(cell2));
    }

    @Test
    void testGetDistances_andTestLink_whenOneHorizontalLink_returnsDistancesWhereLinkedCellHasDistanceOfOne(){
        //Grid of 2 horizontally placed cells 
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(1, 2);
        cell1.link(cell2);
        cell1.setEast(Optional.of(cell2));

        final var distances = cell1.getDistances();
        assertEquals(1, distances.getDistance(cell2));
    }

    @Test
    void testGetDistances_andTestLink_whenTwoVerticalLinksAndDepthOfTwo_returnsExpectedDistances(){
        //Grid of 3 vertically placed cells 
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(2, 1);
        cell1.link(cell2);
        cell1.setNorth(Optional.of(cell2));

        final var cell3 = new Cell(3, 1);
        cell2.link(cell3);
        cell2.setNorth(Optional.of(cell3));

        final var distances = cell1.getDistances();
        assertEquals(1, distances.getDistance(cell2));
        assertEquals(2, distances.getDistance(cell3));
    }

    @Test
    void testGetDistances_andTestLink_whenTwoVerticalLinksAndDepthOfOne_returnsExpectedDistances(){
        //Grid of 3 vertically placed cells 
        final var cell1 = new Cell(2, 1);
        final var cell2 = new Cell(3, 1);
        cell1.link(cell2);
        cell1.setNorth(Optional.of(cell2));

        final var cell3 = new Cell(1, 1);
        cell1.link(cell3);
        cell1.setSouth(Optional.of(cell3));

        final var distances = cell1.getDistances();
        assertEquals(1, distances.getDistance(cell2));
        assertEquals(1, distances.getDistance(cell3));
    }


    @Test
    void testGetDistances_andTestLink_whenTwoHorizontalLinksAndDepthOfTwo_returnsExpectedDistances(){
        //Grid of 3 horizontally placed cells 
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(1, 2);
        cell1.link(cell2);
        cell1.setEast(Optional.of(cell2));

        final var cell3 = new Cell(1, 3);
        cell2.link(cell3);
        cell2.setEast(Optional.of(cell3));

        final var distances = cell1.getDistances();
        assertEquals(1, distances.getDistance(cell2));
        assertEquals(2, distances.getDistance(cell3));
    }

    @Test
    void testGetDistances_andTestLink_whenTwoHorizontalLinksAndDepthOfOne_returnsExpectedDistances(){
        //Grid of 3 horizontally placed cells 
        final var cell1 = new Cell(1, 2);
        final var cell2 = new Cell(1, 3);
        cell1.link(cell2);
        cell1.setEast(Optional.of(cell2));

        final var cell3 = new Cell(1, 1);
        cell1.link(cell3);
        cell1.setWest(Optional.of(cell3));

        final var distances = cell1.getDistances();
        assertEquals(1, distances.getDistance(cell2));
        assertEquals(1, distances.getDistance(cell3));
    }

    @Test
    void testGetDistances_andTestLink_whenOneVerticalAndOneHorizontalLink_returnsExpectedDistances(){
        //L-shaped grid of 3 cells 
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(2, 1);
        cell1.link(cell2);
        cell1.setNorth(Optional.of(cell2));

        final var cell3 = new Cell(1, 2);
        cell1.link(cell3);
        cell1.setEast(Optional.of(cell3));

        final var distances = cell1.getDistances();
        assertEquals(1, distances.getDistance(cell2));
        assertEquals(1, distances.getDistance(cell3));
    }

    @Test
    void testGetDistances_andTestLink_whenMoreComplexGrid_returnsExpectedDistances(){
        //T-shaped grid of 6 cells 
        final var cell1 = new Cell(5, 5);
        final var cell2 = new Cell(4, 5);
        final var cell3 = new Cell(6, 5);
        final var cell4 = new Cell(7, 5);
        final var cell5 = new Cell(7, 4);
        final var cell6 = new Cell(7, 6);

        cell1.link(cell2);
        cell1.setSouth(Optional.of(cell2));

        cell1.link(cell3);
        cell1.setNorth(Optional.of(cell2));

        cell3.link(cell4);
        cell3.setNorth(Optional.of(cell4));

        cell4.link(cell5);
        cell4.setWest(Optional.of(cell5));

        cell4.link(cell6);
        cell4.setEast(Optional.of(cell6));

        final var distances = cell1.getDistances();
        assertEquals(1, distances.getDistance(cell2));
        assertEquals(1, distances.getDistance(cell3));
        assertEquals(2, distances.getDistance(cell4));
        assertEquals(3, distances.getDistance(cell5));
        assertEquals(3, distances.getDistance(cell6));
    }

    @Test
    void testGetDistances_andTestLink_whenImperfectMaze_returnsSmallestDistancesOnly(){
        //Imperfect maze (loop): Multiple paths to cell3. 
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(1, 2);
        final var cell3 = new Cell(1, 3);
       
        cell1.link(cell2);
        cell1.setNorth(Optional.of(cell2));

        cell2.link(cell3);
        cell2.setNorth(Optional.of(cell3));

        cell1.link(cell3);
        cell1.setSouth(Optional.of(cell3));

        final var distances = cell1.getDistances();
        assertEquals(1, distances.getDistance(cell2));
        assertEquals(1, distances.getDistance(cell3));
    }
}
