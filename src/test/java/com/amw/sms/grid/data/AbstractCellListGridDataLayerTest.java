package com.amw.sms.grid.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.amw.sms.grid.Cell;
import com.amw.sms.grid.CellList;
import com.amw.sms.testutil.ColorTestUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for AbstractCellListGridDataLayer subclasses.
 */
@ExtendWith(MockitoExtension.class)
public abstract class AbstractCellListGridDataLayerTest {
    @Mock Cell mockCell1;
    @Mock Cell mockCell2;

    Color sampleColor = Color.BLUE;
    String sampleContentsString = "AA";

    //Objects under test
    List<Cell> innerList;
    CellList cellList;
    AbstractCellListGridDataLayer layer;

    //Test helpers
    ColorTestUtils testUtils;

    @BeforeEach
    void beforeEach(){
        this.setupLayerUnderTest();

        testUtils = new ColorTestUtils();
    }

    /**
     * Returns the layer instance to test on. 
     * Purpose is to be overridden by subclasses.
     * @return AbstractCellListGridDataLayer to test.
     */
    abstract void setupLayerUnderTest();

    //Is set test
    @Test
    void testIsCellSet_whenCellIsNotInCellList_returnsFalse() {
        assertFalse(layer.isCellSet(mockCell1));
    }

    @Test
    void testIsCellSet_whenCellIsInCellList_returnsTrue() {
        innerList.add(mockCell1);
        assertTrue(layer.isCellSet(mockCell1));        
    }

    //Contents tests
    @Test
    void testDoGetCellContents_whenCellIsNotInCellList_returnsEmptyOptional() {
        assertTrue(layer.doGetCellContents(mockCell1).isEmpty());        
    }

    @Test
    void testDoGetCellContents_whenCellIsInCellList_andModeIsntSetManually_returnsDefaultStringContents() {
        innerList.addAll(Arrays.asList(mockCell1, mockCell2));
        assertEquals(CellListGridDataLayer.DEFAULT_CELL_CONTENTS_STRING, layer.doGetCellContents(mockCell2).get());
    }

    @Test
    void testUseAscendingNumbers_andDoGetCellContents_whenCellIsInCellList_returnsIndexOfCellInList() {
        innerList.addAll(Arrays.asList(mockCell1, mockCell2));
    
        layer.useAscendingNumericals();

        assertEquals("0", layer.doGetCellContents(mockCell1).get());
        assertEquals("1", layer.doGetCellContents(mockCell2).get());
    }

    @Test
    void testUseSingleString_andDoGetCellContents_whenCellIsInCellList_returnsSetString() {
        innerList.addAll(Arrays.asList(mockCell1, mockCell2));
    
        layer.useSingleString(sampleContentsString);

        assertEquals(sampleContentsString, layer.doGetCellContents(mockCell1).get());
        assertEquals(sampleContentsString, layer.doGetCellContents(mockCell2).get());
    }

    //Color test
    @Test
    void testDoGetCellColor_whenCellIsNotInCellList_returnsEmptyOptional() {
        layer.enableCellColors();

        assertTrue(layer.doGetCellColor(mockCell1).isEmpty());        
    }

    @Test
    void testDoGetCellColor_whenCellIsInCellList_returnsLighterVersionOfLayerColor() {
        innerList.addAll(Arrays.asList(mockCell1, mockCell2));
        layer.enableCellColors();
    
        testUtils.assertLighterColor(layer.getCellColor(mockCell1).get(), layer.getLayerColor());
    }
}
