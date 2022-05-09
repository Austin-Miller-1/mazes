package com.amw.sms.grid.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.Arrays;

import com.amw.sms.grid.Cell;
import com.amw.sms.grid.CellDistances;
import com.amw.sms.grid.Grid;
import com.amw.sms.testutil.ColorTestUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for CellDistancesDataLayer. 
 * Does not mock out CellDistances since the class is simple enough and relevant enough to 
 * the data-layer class. This makes these tests integration unit-tests.
 */
@ExtendWith(MockitoExtension.class)
public class CellDistancesDataLayerTest {
    @Mock
    private Grid mockGrid;

    @Mock private Cell mockRootCell;
    @Mock private Cell mockCell1;
    @Mock private Cell mockCell2;
    @Mock private Cell mockCell3;

    private final String sampleLayerName = "simple";
    private final int sampleDistance = 10;

    //Object under test - using sample data from above
    private CellDistancesDataLayer layer;
    
    //Unmocked celldistances
    private CellDistances distances;
    
    //Test helpers
    private ColorTestUtils testUtils;

    @BeforeEach
    void beforeEach(){
        distances = new CellDistances(mockGrid, mockRootCell);
        layer = new CellDistancesDataLayer(distances, sampleLayerName);

        testUtils = new ColorTestUtils();
    }

    //Contents tests
    @Test
    void testDoGetCellContents__whenDistanceIsNotSet_returnsEmptyOptional() {
        assertTrue(layer.getCellContents(mockCell1).isEmpty());
    }

    @Test
    void testDoGetCellContents_whenDistanceIsSet_returnsDistanceAsString() {
        distances.setDistance(mockCell1, sampleDistance);
        assertEquals(Integer.toString(sampleDistance), layer.doGetCellContents(mockCell1).get());
    }

    //Color tests
    @Test
    void testGetCellColor_andTestSetDistance_whenOnlyRoot_returnsWhiteForRoot(){
        assertEquals(Color.WHITE, layer.getCellColor(mockRootCell).get());
    }

    @Test
    void testGetCellColor_andTestSetDistance_whenTwoCells_returnsWhiteForRoot(){
        distances.setDistance(mockCell1, 1);
        assertEquals(Color.WHITE, layer.getCellColor(mockRootCell).get());
    }

    @Test
    void testGetCellColor_andTestSetDistance_whenTwoCells_returnsDarkerColorForSecondCell(){
        distances.setDistance(mockCell1, 1);
        testUtils.assertDarkerColor(layer.getCellColor(mockCell1).get(), layer.getCellColor(mockRootCell).get());
    }

    @Test
    void testGetCellColor_andTestSetDistance_whenFourCells_returnsWhiteForRoot(){
        distances.setDistance(mockCell1, 1);
        distances.setDistance(mockCell2, 2);
        distances.setDistance(mockCell3, 3);
        assertEquals(Color.WHITE, layer.getCellColor(mockRootCell).get());
    }

    @Test
    void testGetCellColor_andTestSetDistance_whenFourCells_returnsDarkerColorForEveryCellFurtherAwayFromRoot(){
        distances.setDistance(mockCell1, 1);
        distances.setDistance(mockCell2, 2);
        distances.setDistance(mockCell3, 3);

        final var path = Arrays.asList(mockRootCell, mockCell1, mockCell2, mockCell3);
        for(int index = 1; index < path.size(); index++){
            testUtils.assertDarkerColor(
                layer.getCellColor(path.get(index)).get(),    //Current cell 
                layer.getCellColor(path.get(index-1)).get()    //Previous cell
            );
        }            
    }

    @Test
    void testGetCellColor_andTestSetDistance_whenCellIsUnset_returnsEmptyOptional(){
        distances.setDistance(mockCell1, 10);
        assertTrue(layer.getCellColor(mockCell2).isEmpty());
    }

    //isCellSet tests
    @Test
    void testIsCellSet_whenDistanceIsNotSet_returnsFalse() {
        assertFalse(layer.isCellSet(mockCell1));
    }

    @Test
    void testIsCellSet_whenDistanceIsSet_returnsTrue() {
        distances.setDistance(mockCell1, sampleDistance);
        assertTrue(layer.isCellSet(mockCell1));
    }
}
