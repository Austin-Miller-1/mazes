package com.amw.sms.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test CellDistances class.
 */
@ExtendWith(MockitoExtension.class)
public class CellDistancesTest {
    @Mock
    private Grid mockGrid;

    //Cell mocks
    @Mock private Cell mockCell1;
    @Mock private Cell mockCell2;
    @Mock private Cell mockCell3;
    @Mock private Cell mockRoot;

    private CellDistances distances; //Distances instance using root cell. No distances set.

    @BeforeEach
    void beforeEach(){
        distances = new CellDistances(mockGrid, mockRoot);
    }

    @Test
    void testSetDistance_andTestGetCells_returnsCollectionContainingCell() {
        distances.setDistance(mockCell1, 3);
        assertTrue(distances.getCells().contains(mockCell1));
    }

    @Test
    void testSetDistance_andTestGetDistance_returnsTheSetDistance() {
        distances.setDistance(mockCell1, 3);
        assertEquals(3, distances.getDistance(mockCell1));

        distances.setDistance(mockCell2, 100);
        assertEquals(100, distances.getDistance(mockCell2));
    }

    @Test
    void testGetDistance_returnsZeroForRootCell() {
        assertEquals(0, distances.getDistance(mockRoot));
    }

    @Test
    void testGetDistance_returnsNegativeOneForUnknownCell() {
        assertEquals(-1, distances.getDistance(mockCell1));
    }

    @Test
    void testIsDistanceSet_returnTrueIfCellDistanceIsSet(){
        distances.setDistance(mockCell1, 3);
        assertTrue(distances.isDistanceSet(mockCell1));
    }

    @Test
    void testIsDistanceSet_returnFalseIfCellDistanceIsUnset(){
        assertFalse(distances.isDistanceSet(mockCell1));
    }

    @Test
    void testGetCells_andTestSetDistance_returnsCollectionContainingAllAddedCells() {
        distances.setDistance(mockCell1, 1);
        distances.setDistance(mockCell2, 2);
        distances.setDistance(mockCell3, 3);

        final var cells = distances.getCells();
        assertEquals(4, cells.size());
        assertTrue(cells.contains(mockRoot));
        assertTrue(cells.contains(mockCell1));
        assertTrue(cells.contains(mockCell2));
        assertTrue(cells.contains(mockCell3));
    }

    @Test
    void testGetCells_andTestSetDistance_andTestGetDistance_returnsCollectionContainingCellsThatHaveASetDistance() {
        distances.setDistance(mockCell1, 1);
        distances.setDistance(mockCell2, 2);
        distances.setDistance(mockCell3, 3);

        distances.getCells()
            .stream()
            .forEach((var cell) -> assertTrue(distances.getDistance(cell) >= 0));
    }

    @Test 
    void testGetRootCell_returnsRootCell(){
        assertEquals(mockRoot, distances.getRootCell());
    }

    @Test
    void testGetFurthestCell__andTestSetDistance_returnsCellWithGreatestDistanceFromRoot(){
        distances.setDistance(mockCell1, 1);
        distances.setDistance(mockCell2, 300);
        distances.setDistance(mockCell3, 20);

        assertEquals(mockCell2, distances.getFurthestCell());
    }

    @Test
    void testGetFurthestCell_whenNoCellsSet_returnsRoot(){
        assertEquals(mockRoot, distances.getFurthestCell());
    }

    @Test
    void testGetCellContents_andTestSetDistance_returnsCellDistanceAsString(){
        distances.setDistance(mockCell1, 10);
        distances.setDistance(mockCell2, 20);
        assertEquals("10", distances.getCellContents(mockCell1));
    }

    @Test
    void testGetCellContents_andTestSetDistance_whenCellIsUnset_returnsDefaultValueFromGridDataParentClass(){
        distances.setDistance(mockCell1, 10);
        final var expectedDefaultContents = new SampleGridDataImpl(mockGrid).getCellContents(mockCell2); 
        assertEquals(expectedDefaultContents, distances.getCellContents(mockCell2));
    }
}
