package com.amw.sms.grid.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import com.amw.sms.grid.Cell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for SimpleGridDataLayer.
 */
@ExtendWith(MockitoExtension.class)
public class SimpleGridDataLayerTest {
    @Mock
    private Cell mockCell;

    private final String sampleLayerName = "simple";
    private final String sampleCellContents = "abc";
    private final Color sampleCellColor = Color.CYAN;

    //Object under test - using sample data from above
    private SimpleGridDataLayer layer;

    @BeforeEach
    void beforeEach(){
        layer = new SimpleGridDataLayer(sampleLayerName);
    }

    //Contents tests
    @Test
    void testDoGetCellContents_returnsEmptyOptionalIfCellContentsAreNotSet() {
        assertTrue(layer.getCellContents(mockCell).isEmpty());

    }

    @Test
    void testDoGetCellContents_andSetCellContents_returnsSetContents() {
        layer.setCellContents(mockCell, sampleCellContents);
        assertEquals(sampleCellContents, layer.doGetCellContents(mockCell).get());
    }

    //Color tests
    @Test
    void testDoGetCellColor_returnsEmptyOptionalWhenUnset() {
        assertTrue(layer.getCellColor(mockCell).isEmpty());
    }

    @Test
    void testDoGetCellColor_andSetCellColor_returnsSetColor() {
        layer.setCellColor(mockCell, sampleCellColor);
        assertEquals(sampleCellColor, layer.doGetCellColor(mockCell).get());
    }

    //isCellSet tests
    @Test
    void testIsCellSet_returnsFalseIfCellContentsAreNotSet() {
        assertFalse(layer.isCellSet(mockCell));
    }

    @Test
    void testIsCellSet_andSetCellContents_whenCellContentsAreSet_andCellColorIsNotSet_returnsTrue() {
        layer.setCellContents(mockCell, sampleCellContents);
        assertTrue(layer.isCellSet(mockCell));
    }

    @Test
    void testIsCellSet_andSetCellColor_whenCellColorIsSet_butCellContentsAreNotSet_returnsTrue() {
        layer.setCellColor(mockCell, sampleCellColor);
        assertTrue(layer.isCellSet(mockCell));
    }

    @Test
    void testIsCellSet_andSetCellContents_andSetCellColor_whenCellContentsAreSet_andCellColorIsSet_returnsTrue() {
        layer.setCellContents(mockCell, sampleCellContents);
        layer.setCellColor(mockCell, sampleCellColor);
        assertTrue(layer.isCellSet(mockCell));
    }
}
