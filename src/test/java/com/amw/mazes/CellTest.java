package com.amw.mazes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

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
    void testUnlink_andTestIsLinkedTo_usingLink_assertUnlinkedCellsAreNotConsideredLinked() {
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
    void testUnlink_andTestIsLinkedTo_usingLink_assertUnlinkedCellsAreNotConsideredLinked_bidirectionalCheck() {
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
    void testUnlink_andTestGetLinks_usingLink_assertUnlinkedCellsAreNotInLinkCollection() {
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
    void testGetLinks_usingLink_assertSetOfAllLinkedCellsIsReturned() {
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
    void testGetNeighbors_andTestSetters_assertCollectionContainsAllNeighbors() {
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
    void testGetNeighbors_AndTestSetters_whenNeighborsAreUnset_assertCollectionIsEmpty() {
        //Test fixture
        final var cell = new Cell(1, 1);

        //Method
        final var neighbors = cell.getNeighbors();

        //Assert
        assertTrue(neighbors.isEmpty());
    }

    @Test
    void testGetNeighbors_AndTestSetters_whenOneNeighborIsUnset_assertCollectionContainsAllActualNeighbors() {
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
    void testGetNeighbors_andTestSetters_whenNeighborsAreEmpty_assertCollectionDoesNotContainThem() {
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
}
