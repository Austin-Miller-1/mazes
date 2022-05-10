package com.amw.sms.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test Cell class.
 */
public class CellTest {
    //Objects under test
    private Cell cell1;
    private Cell cell2; 
    private Cell cell3; 
    private Cell cell4; 
    private Cell cell5; 

    @BeforeEach
    void beforeEach(){
        this.cell1 = new Cell(1, 1);
        this.cell2 = new Cell(2, 2);
        this.cell3 = new Cell(3, 3);
        this.cell4 = new Cell(3, 3);
        this.cell5 = new Cell(3, 3);
    }

    @Test
    void testLink_andTestIsLinkedTo_linkedCellsAreConsideredLinked() {
        //Method
        cell1.link(cell2);

        //Assertion
        assertTrue(cell1.isLinkedTo(cell2));
        assertTrue(cell2.isLinkedTo(cell1));
    }

    @Test
    void testUnlink_andTestIsLinkedTo_usingLink_unlinkedCellsAreNotConsideredLinked() {
        //Test fixture
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
        cell1.link(cell2);

        //Method - unlink using cell2 specifically
        cell2.unlink(cell1);

        //Assertion
        assertFalse(cell1.isLinkedTo(cell2));
        assertFalse(cell2.isLinkedTo(cell1));
    }

    @Test
    void testUnlink_andTestGetLinks_usingLink_unlinkedCellsAreNotInLinkSet() {
        //Test fixture
        cell1.link(cell2);

        //Method - unlink using cell2 specifically
        cell2.unlink(cell1);

        //Assertion
        assertFalse(cell1.getLinks().contains(cell2));
    }

    @Test
    void testGetLinks_usingLink_setOfAllLinkedCellsIsReturned() {
        //Test fixture
        cell1.link(cell2);
        cell1.link(cell3);

        //Method
        final var links = cell1.getLinks();

        //Assert
        assertEquals(2, links.size());
        assertTrue(links.contains(cell2));
        assertTrue(links.contains(cell3));
        assertFalse(links.contains(cell4)); //Unlinked cell
    }

    @Test
    void testGetNeighbors_andTestSetters_listContainsAllNeighbors() {
        //Test fixture
        cell1.setNorth(Optional.of(cell2)); 
        cell1.setEast(Optional.of(cell3)); 
        cell1.setSouth(Optional.of(cell4)); 
        cell1.setWest(Optional.of(cell5)); 
        
        //Method under test
        final var neighbors = cell1.getNeighbors();

        //Assert
        assertEquals(4, neighbors.size());
        assertTrue(neighbors.contains(cell2));
        assertTrue(neighbors.contains(cell3));
        assertTrue(neighbors.contains(cell4));
        assertTrue(neighbors.contains(cell5));
    }

    @Test
    void testGetNeighbors_andTestSetters_whenNeighborsAreUnset_listIsEmpty() {
        //Method
        final var neighbors = cell1.getNeighbors();

        //Assert
        assertTrue(neighbors.isEmpty());
    }

    @Test
    void testGetNeighbors_andTestSetters_whenOneNeighborIsUnset_listContainsAllActualNeighbors() {
        //Test fixture
        cell1.setNorth(Optional.of(cell2));
        cell1.setWest(Optional.of(cell3));
        
        //Method
        final var neighbors = cell1.getNeighbors();

        //Assert
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(cell2));
        assertTrue(neighbors.contains(cell3));
    }

    @Test
    void testGetNeighbors_andTestSetters_whenNeighborsAreEmpty_listDoesNotContainThem() {
        //Test fixture
        cell1.setNorth(Optional.ofNullable(null));
        cell1.setEast(Optional.ofNullable(null));
        cell1.setSouth(Optional.ofNullable(null));
        cell1.setWest(Optional.ofNullable(null));
        
        //Method & Assert
        assertEquals(0, cell1.getNeighbors().size());
    }

    @Test
    void testGetRandomNeighbor_whenNoNeighbors_returnsEmptyOptional(){
        assertTrue(cell1.getRandomNeighbor().isEmpty());
    }

    @Test
    void testGetRandomNeighbor_andSetWest_whenOneNeighbor_returnsThatNeighbor(){
        cell1.setWest(Optional.of(cell2)); 

        assertEquals(cell2, cell1.getRandomNeighbor().get());
    }

    @Test
    void testGetRandomNeighbor_andSetters_andGetNeighbors_whenAllNeighborsSet_returnsOneOfThem(){
        cell1.setNorth(Optional.of(cell2)); 
        cell1.setEast(Optional.of(cell3)); 
        cell1.setSouth(Optional.of(cell4)); 
        cell1.setWest(Optional.of(cell5)); 

        assertTrue(cell1.getNeighbors().contains(cell1.getRandomNeighbor().get()));
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
