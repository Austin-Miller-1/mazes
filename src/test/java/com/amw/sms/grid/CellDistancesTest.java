package com.amw.sms.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.Arrays;

import com.amw.sms.grid.griddata.SampleGridDataImpl;

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

    //Helper method - get expected default
    private String getExpectedDefaultCellContents(){
        return new SampleGridDataImpl(mockGrid).getCellContentsDEP(mockCell2);
    }

    //Helper method - get expected default
    private Color getExpectedDefaultCellColor(){
        return new SampleGridDataImpl(mockGrid).getCellColorDEP(mockCell2);
    }

    /**
     * Asserts that the first color is darker than the second color. In this method, "darker" is
     * defined as being composed of darker red, green AND blue values. If any of the three 
     * are not darker, the assertion fails. 
     * @param expectedDarker
     * @param expectedBrighter
     */
    private void assertDarkerColor(Color expectedDarker, Color expectedBrighter){
        assertTrue(expectedDarker.getRed() < expectedBrighter.getRed());
        assertTrue(expectedDarker.getGreen() < expectedBrighter.getGreen());
        assertTrue(expectedDarker.getBlue() < expectedBrighter.getBlue());
    }

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
    void testGetMaxEntry_whenNoCellsSet_returnsRootEntry(){
        final var maxEntry = distances.getMaxEntry();
        assertEquals(mockRoot, maxEntry.getFirst());
        assertEquals(0, maxEntry.getSecond());
    }

    @Test
    void testGetMaxEntry__andTestSetDistance_returnsEntryWithGreatestDistanceFromRoot(){
        distances.setDistance(mockCell1, 1);
        distances.setDistance(mockCell2, 300);
        distances.setDistance(mockCell3, 20);

        final var maxEntry = distances.getMaxEntry();
        assertEquals(mockCell2, maxEntry.getFirst());
        assertEquals(300, maxEntry.getSecond());
    }

    //Typically, such a test wouldn't be necessary, but the point is to cover the known caching functionality
    @Test
    void testGetMaxEntry_andTestSetDistance_whenMaxDistanceChanges_returnsNewMaxEntry(){
        distances.setDistance(mockCell1, 1);
        var maxEntry = distances.getMaxEntry();
        assertEquals(mockCell1, maxEntry.getFirst());
        assertEquals(1, maxEntry.getSecond());

        distances.setDistance(mockCell2, 300);
        maxEntry = distances.getMaxEntry();
        assertEquals(mockCell2, maxEntry.getFirst());
        assertEquals(300, maxEntry.getSecond());
    }

    //Typically, such a test wouldn't be necessary, but the point is to cover the known caching functionality
    @Test
    void testGetMaxEntry_andTestSetDistance_whenMaxDistanceDoesntChange_returnsMaxEntry(){
        distances.setDistance(mockCell1, 150);
        distances.setDistance(mockCell2, 10);
        
        //First check
        var maxEntry = distances.getMaxEntry();
        assertEquals(mockCell1, maxEntry.getFirst());
        assertEquals(150, maxEntry.getSecond());

        //Second check - what we care about
        maxEntry = distances.getMaxEntry();
        assertEquals(mockCell1, maxEntry.getFirst());
        assertEquals(150, maxEntry.getSecond());
    }

    @Test
    void testGetFurthestCell_whenNoCellsSet_returnsRoot(){
        assertEquals(mockRoot, distances.getFurthestCell());
    }

    @Test
    void testGetFurthestCell__andTestSetDistance_returnsCellWithGreatestDistanceFromRoot(){
        distances.setDistance(mockCell1, 1);
        distances.setDistance(mockCell2, 300);
        distances.setDistance(mockCell3, 20);

        assertEquals(mockCell2, distances.getFurthestCell());
    }

    //Typically, such a test wouldn't be necessary, but the point is to cover the known caching functionality
    @Test
    void testGetFurthestCell_andTestSetDistance_whenMaxDistanceChanges_returnsNewFurthestCell(){
        distances.setDistance(mockCell1, 1);
        assertEquals(mockCell1, distances.getFurthestCell());

        distances.setDistance(mockCell2, 300);
        assertEquals(mockCell2, distances.getFurthestCell());
    }

    //Typically, such a test wouldn't be necessary, but the point is to cover the known caching functionality
    @Test
    void testGetFurthestCell_andTestSetDistance_whenMaxDistanceDoesntChange_returnsFurthestCell(){
        distances.setDistance(mockCell1, 150);
        distances.setDistance(mockCell2, 10);
        assertEquals(mockCell1, distances.getFurthestCell());
        assertEquals(mockCell1, distances.getFurthestCell());  //Second call is what we're checking
    }

    @Test
    void testGetMaxDistance_whenNoCellsSet_returnsZero(){
        assertEquals(0, distances.getMaxDistance());
    }

    @Test
    void testGetMaxDistance_andTestSetDistance_returnsGreatestDistanceFromRoot(){
        distances.setDistance(mockCell1, 1);
        distances.setDistance(mockCell2, 300);
        distances.setDistance(mockCell3, 20);

        assertEquals(300, distances.getMaxDistance());
    }

    //Typically, such a test wouldn't be necessary, but the point is to cover the known caching functionality
    @Test
    void testGetMaxDistance_andTestSetDistance_whenMaxDistanceChanges_returnsNewGreatestDistance(){
        distances.setDistance(mockCell1, 1);
        assertEquals(1, distances.getMaxDistance());

        distances.setDistance(mockCell2, 300);
        assertEquals(300, distances.getMaxDistance());
    }

    //Typically, such a test wouldn't be necessary, but the point is to cover the known caching functionality
    @Test
    void testGetMaxDistance_andTestSetDistance_whenMaxDistanceDoesntChange_returnsGreatestDistance(){
        distances.setDistance(mockCell1, 150);
        distances.setDistance(mockCell2, 10);
        assertEquals(150, distances.getMaxDistance());
        assertEquals(150, distances.getMaxDistance());  //Second call is what we're checking
    }

    @Test
    void testGetCellContents_andTestSetDistance_returnsCellDistanceAsString(){
        distances.setDistance(mockCell1, 5);
        distances.setDistance(mockCell2, 9);
        assertEquals("5", distances.getCellContentsDEP(mockCell1));
    }

    @Test
    void testGetCellContents_andTestSetDistance_returnsCellDistanceAsBase32String(){
        distances.setDistance(mockCell1, 15);
        distances.setDistance(mockCell2, 30);
        assertEquals("F", distances.getCellContentsDEP(mockCell1).toUpperCase());
    }

    @Test
    void testGetCellContents_andTestSetDistance_whenCellIsUnset_returnsDefaultValueFromGridDataParentClass(){
        distances.setDistance(mockCell1, 10);
        assertEquals(getExpectedDefaultCellContents(), distances.getCellContentsDEP(mockCell2));
    }

    @Test
    void testGetCellColor_andTestSetDistance_whenTwoCellsHaveSameDistance_returnsSameNonDefaultColor(){
        distances.setDistance(mockCell1, 1);
        distances.setDistance(mockCell2, 2);
        distances.setDistance(mockCell3, 2);
        assertEquals(distances.getCellColorDEP(mockCell2), distances.getCellColorDEP(mockCell3));
        assertNotEquals(getExpectedDefaultCellColor(), distances.getCellColorDEP(mockCell2));
    }

    @Test
    void testGetCellColor_andTestSetDistance_whenOnlyRoot_returnsWhiteForRoot(){
        assertEquals(Color.WHITE, distances.getCellColorDEP(mockRoot));
    }

    @Test
    void testGetCellColor_andTestSetDistance_whenTwoCells_returnsWhiteForRoot(){
        distances.setDistance(mockCell1, 1);
        assertEquals(Color.WHITE, distances.getCellColorDEP(mockRoot));
    }

    @Test
    void testGetCellColor_andTestSetDistance_whenTwoCells_returnsDarkerColorForSecondCell(){
        distances.setDistance(mockCell1, 1);
        assertDarkerColor(distances.getCellColorDEP(mockCell1), distances.getCellColorDEP(mockRoot));
    }

    @Test
    void testGetCellColor_andTestSetDistance_whenFourCells_returnsWhiteForRoot(){
        distances.setDistance(mockCell1, 1);
        distances.setDistance(mockCell2, 2);
        distances.setDistance(mockCell3, 3);
        assertEquals(Color.WHITE, distances.getCellColorDEP(mockRoot));
    }

    @Test
    void testGetCellColor_andTestSetDistance_whenFourCells_returnsDarkerColorForEveryCellFurtherAwayFromRoot(){
        distances.setDistance(mockCell1, 1);
        distances.setDistance(mockCell2, 2);
        distances.setDistance(mockCell3, 3);

        final var path = Arrays.asList(mockRoot, mockCell1, mockCell2, mockCell3);
        for(int index = 1; index < path.size(); index++){
            assertDarkerColor(
                distances.getCellColorDEP(path.get(index)),    //Current cell 
                distances.getCellColorDEP(path.get(index-1)    //Previous cell
            ));
        }            
    }

    @Test
    void testGetCellColor_andTestSetDistance_whenCellIsUnset_returnsDefaultValueFromGridDataParentClass(){
        distances.setDistance(mockCell1, 10);
        assertEquals(getExpectedDefaultCellColor(), distances.getCellColorDEP(mockCell2));
    }
}
