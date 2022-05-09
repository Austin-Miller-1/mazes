package com.amw.sms.grid.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.Optional;

import com.amw.sms.grid.Cell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GridDataLayerTest {
    @Mock 
    private Cell mockCell;

    private final String sampleName = "layer1";
    private final Color sampleLayerColor = Color.RED;
    private final Optional<String> sampleCellContents = Optional.of("AAA");
    private final Optional<Color> sampleCellColor = Optional.of(Color.GREEN);

    //Object under test
    //Layer will use above sample data
    private SampleGridDataLayerImpl sampleLayer;

    @BeforeEach
    void beforeEach(){
        sampleLayer = new SampleGridDataLayerImpl(sampleName, sampleLayerColor);
    }

    @Test
    void testGetName_returnsSetName() {
        assertEquals(sampleName, sampleLayer.getName());
    }

    @Test
    void testGetLayerColor_returnsSetColor() {
        assertEquals(sampleLayerColor, sampleLayer.getLayerColor());
    }

    @Test
    void testSetLayerColor_andGetLayerColor_returnsSetColor(){
        final var newColor = Color.GRAY;
        sampleLayer.setLayerColor(newColor);
        assertEquals(newColor, sampleLayer.getLayerColor());
    }

    @Test
    void testGridDataLayer_andGetLayerColor_whenNoColorProvided_usesBlack(){
        final var layer = new SampleGridDataLayerImpl(sampleName);
        assertEquals(Color.BLACK, layer.getLayerColor());
    }

    //Enable/disable tests
    @Test
    void testIsEnabled_layerIsEnabledByDefault(){
        assertTrue(sampleLayer.isEnabled());
    }

    @Test
    void testEnable_andIsEnabled_returnsTrue(){
        sampleLayer.enable();
        assertTrue(sampleLayer.isEnabled());
    }

    @Test
    void testDisable_andIsEnabled_returnsFalse(){
        sampleLayer.disable();
        assertFalse(sampleLayer.isEnabled());
    }

    @Test
    void testEnable_andDisable_andIsEnabled_whenEnabledAfterDisabled_returnsTrue(){
        sampleLayer.disable();
        sampleLayer.enable();
        assertTrue(sampleLayer.isEnabled());
    }

    //Cell-contents tests
    @Test
    void testGetCellContents_templateMethodTest_isEnabledByDefault_andReturnsValueFromDoGetCellContentsAbstractMethod(){
        sampleLayer.setReturnForDoGetCellContents(sampleCellContents);
        assertEquals(sampleCellContents, sampleLayer.getCellContents(mockCell));
    }

    @Test
    void testEnableCellContents_andGetCellContents_returnsCellContentsWhenEnabled(){
        sampleLayer.setReturnForDoGetCellContents(sampleCellContents);
        sampleLayer.enableCellContents();
        assertEquals(sampleCellContents, sampleLayer.getCellContents(mockCell));
    }

    @Test
    void testDisableCellContents_andGetCellContents_returnsEmptyOptionalWhenDisabled(){
        sampleLayer.setReturnForDoGetCellContents(sampleCellContents);
        sampleLayer.disableCellContents();
        assertTrue(sampleLayer.getCellContents(mockCell).isEmpty());
    }

    @Test
    void testEnableCellContents_andDisableCellContents_andGetCellContents_whenEnabledAfterDisabled_returnsCellContents(){
        sampleLayer.setReturnForDoGetCellContents(sampleCellContents);
        sampleLayer.disableCellContents();
        sampleLayer.enableCellContents();
        assertEquals(sampleCellContents, sampleLayer.getCellContents(mockCell));
    }

    //Cell-color tests
    @Test
    void testGetCellColor_templateMethodTest_isEnabledByDefault_andReturnsValueFromDoGetCellColorAbstractMethod(){
        sampleLayer.setReturnForDoGetCellColors(sampleCellColor);
        assertEquals(sampleCellColor, sampleLayer.getCellColor(mockCell));
    }

    @Test
    void testEnableCellColors_andGetCellColor_returnsCellColorWhenEnabled(){
        sampleLayer.setReturnForDoGetCellColors(sampleCellColor);
        sampleLayer.enableCellColors();
        assertEquals(sampleCellColor, sampleLayer.getCellColor(mockCell));
    }

    @Test
    void testDisableCellColors_andGetCellColor_returnsEmptyOptionalWhenDisabled(){
        sampleLayer.setReturnForDoGetCellColors(sampleCellColor);
        sampleLayer.disableCellColors();
        assertTrue(sampleLayer.getCellColor(mockCell).isEmpty());
    }

    @Test
    void testEnableCellColors_andDisableCellColors_andGetCellColor_whenEnabledAfterDisabled_returnsCellColor(){
        sampleLayer.setReturnForDoGetCellColors(sampleCellColor);
        sampleLayer.disableCellColors();
        sampleLayer.enableCellColors();
        assertEquals(sampleCellColor, sampleLayer.getCellColor(mockCell));
    }

    //Highlights tests
    //isCellHighlighted should always return true when a cell is set, false otherwise
    @Test
    void testIsCellHighlighted_templateMethodTest_isEnabledByDefault_andReturnsValueFromIsCellSetAbstractMethod(){
        sampleLayer.setReturnForIsCellSet(true);
        assertTrue(sampleLayer.isCellHighlighted(mockCell));

        sampleLayer.setReturnForIsCellSet(false);
        assertFalse(sampleLayer.isCellHighlighted(mockCell));
    }

    @Test
    void testEnableCellHighlights_andIsCellHighlighted_returnsExpectedValueWhenEnabled(){
        sampleLayer.setReturnForIsCellSet(true);
        sampleLayer.enableCellHighlights();
        assertTrue(sampleLayer.isCellHighlighted(mockCell));
    }

    @Test
    void testDisableCellHighlights_andIsCellHighlighted_alwaysReturnsFalseWhenDisabled(){
        sampleLayer.setReturnForIsCellSet(true);
        sampleLayer.disableCellHighlights();
        assertFalse(sampleLayer.isCellHighlighted(mockCell));
    }

    @Test
    void testEnableCellHighlights_andDisableCellHighlights_andIsCellHighlighted_whenEnabledAfterDisabled_returnsExpectedValue(){
        sampleLayer.setReturnForIsCellSet(true);
        sampleLayer.disableCellHighlights();
        sampleLayer.enableCellHighlights();
        assertTrue(sampleLayer.isCellHighlighted(mockCell));
    }

    //Path tests
    @Test
    void testGetPath_returnsEmptyOptionalAsDefaultImplementation(){
        assertTrue(sampleLayer.getPath().isEmpty());
    }
}
