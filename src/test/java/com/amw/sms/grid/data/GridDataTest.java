package com.amw.sms.grid.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.awt.Color;
import java.util.Arrays;
import java.util.Optional;

import com.amw.sms.grid.Cell;
import com.amw.sms.grid.Grid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for GridData abstract class. Uses SampleGridDataImpl to test.
 * TODO - Learn how to refactor to parameterized generic tests
 */
@ExtendWith(MockitoExtension.class)
public class GridDataTest {
    @Mock
    private Grid mockGrid;

    @Mock
    private Cell mockCell;

    @Mock private GridDataLayer mockLayer1;
    @Mock private GridDataLayer mockLayer2;
    @Mock private GridDataLayer mockLayer3;
    @Mock private GridDataLayer mockLayerEmpty;

    private final Optional<String> sampleContents1 = Optional.of("aaa");
    private final Optional<String> sampleContents2 = Optional.of("bbb");
    private final Optional<String> sampleContents3 = Optional.of("ccc");
    private final Optional<Color> sampleCellColor1 = Optional.of(Color.BLACK);
    private final Optional<Color> sampleCellColor2 = Optional.of(Color.WHITE);
    private final Optional<Color> sampleCellColor3 = Optional.of(Color.GRAY);
    private final Color sampleLayerColor1 = Color.BLUE;
    private final Color sampleLayerColor2 = Color.GREEN;
    private final Color sampleLayerColor3 = Color.RED;

    private GridData gridData;

    @BeforeEach
    public void beforeEach(){
        gridData = new GridData(mockGrid);

        mockGetCellContents(mockLayer1, sampleContents1);
        mockGetCellColor(mockLayer1, sampleCellColor1);
        mockGetLayerColor(mockLayer1, sampleLayerColor1);

        mockGetCellContents(mockLayer2, sampleContents2);
        mockGetCellColor(mockLayer2, sampleCellColor2);
        mockGetLayerColor(mockLayer2, sampleLayerColor2);

        mockGetCellContents(mockLayer3, sampleContents3);
        mockGetCellColor(mockLayer3, sampleCellColor3);
        mockGetLayerColor(mockLayer3, sampleLayerColor3);

        mockGetCellContents(mockLayerEmpty, Optional.empty());
        mockGetCellColor(mockLayerEmpty, Optional.empty());
    }

    //getGrid tests
    @Test
    public void testGetGrid_returnsProvidedGrid(){
        assertEquals(mockGrid, gridData.getGrid());
    }

    /**
     * Mock layer's getCellContents method
     * @param mockLayer
     * @param returnValue
     */
    private void mockGetCellContents(GridDataLayer mockLayer, Optional<String> returnValue){
        Mockito.lenient()
            .when(mockLayer.getCellContents(mockCell))
            .thenReturn(returnValue);
    }

    /**
     * Mock layer's getCellColor method
     * @param mockLayer
     * @param returnValue
     */
    private void mockGetCellColor(GridDataLayer mockLayer, Optional<Color> returnValue){
        Mockito.lenient()
            .when(mockLayer.getCellColor(mockCell))
            .thenReturn(returnValue);
    }

    /**
     * Mock layer's getLayerColor method
     * @param mockLayer
     * @param returnValue
     */
    private void mockGetLayerColor(GridDataLayer mockLayer, Color returnValue){
        Mockito.lenient()
            .when(mockLayer.getLayerColor())
            .thenReturn(returnValue);
    }

    /**
     * Mock layer's isLayerEnabled method
     * @param mockLayer
     * @param returnValue
     */
    private void mockIsLayerEnabled(GridDataLayer mockLayer, boolean returnValue){
        Mockito.lenient()
            .when(mockLayer.isEnabled())
            .thenReturn(returnValue);
    }

    /**
     * Mock layer's isCellHighlighted method
     * @param mockLayer
     * @param returnValue
     */
    private void mockIsCellHighlighted(GridDataLayer mockLayer, boolean returnValue){
        Mockito.lenient()
            .when(mockLayer.isCellHighlighted(any()))
            .thenReturn(returnValue);
    }

    /**
     * Mock layer's isCellSet method
     * @param mockLayer
     * @param returnValue
     */
    private void mockIsCellSet(GridDataLayer mockLayer, boolean returnValue){
        Mockito.lenient()
            .when(mockLayer.isCellSet(any()))
            .thenReturn(returnValue);
    }

    //Contents tests
    @Test
    public void testGetCellContents_whenNoLayers_returnsEmptyList(){
        assertTrue(gridData.getCellContents(mockCell).isEmpty());
    }

    @Test
    public void testGetPrimaryCellContents_whenNoLayers_returnsEmptyOptional(){
        assertTrue(gridData.getPrimaryCellContents(mockCell).isEmpty());
    }

    @Test
    public void testGetCellContents_andAddAtFront_whenOneLayer_returnsListContainingLayerValue(){
        mockIsLayerEnabled(mockLayer1, true);
        gridData.addAtTop(mockLayer1);

        final var contents = gridData.getCellContents(mockCell);
        assertEquals(1, contents.size());
        assertEquals(sampleContents1, contents.get(0));
    }

    @Test
    public void testGetPrimaryCellContents_andAddAtFront_whenOneLayer_returnsLayerValue(){
        mockIsLayerEnabled(mockLayer1, true);
        gridData.addAtTop(mockLayer1);

        assertEquals(sampleContents1, gridData.getPrimaryCellContents(mockCell));
    }

    @Test
    public void testGetCellContents_andAddAtFront_whenMultipleLayers_returnsListContainingLayerValuesInExpectedOrder(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);
        mockIsLayerEnabled(mockLayer3, true);

        gridData.addAtTop(mockLayer3);
        gridData.addAtTop(mockLayer2);
        gridData.addAtTop(mockLayer1);
        final var expectedValues = Arrays.asList(sampleContents1, sampleContents2, sampleContents3);

        assertEquals(expectedValues, gridData.getCellContents(mockCell));
    }

    @Test
    public void testGetPrimaryCellContents_andAddAtFront_whenMultipleLayers_returnsTopLayerCellContents(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);
        mockIsLayerEnabled(mockLayer3, true);

        gridData.addAtTop(mockLayer3);
        gridData.addAtTop(mockLayer2);
        gridData.addAtTop(mockLayer1);

        assertEquals(sampleContents1, gridData.getPrimaryCellContents(mockCell));
    }

    @Test
    public void testGetCellContents_andAddAtFront_whenLayerIsDisabled_doesNotReturnItsValueInTheList(){
        mockIsLayerEnabled(mockLayer1, false);
        gridData.addAtTop(mockLayer1);

        assertTrue(gridData.getCellContents(mockCell).isEmpty());
    }

    @Test
    public void testGetPrimaryCellContents_andAddAtFront_whenLayerIsDisabled_doesNotReturnItsValue(){
        mockIsLayerEnabled(mockLayer1, false);
        gridData.addAtTop(mockLayer1);

        assertTrue(gridData.getPrimaryCellContents(mockCell).isEmpty());
    }

    @Test
    public void testGetPrimaryCellContents_andAddAtFront_whenTopLayerIsDisabled_andSecondIsEnabled_returnsSecondLayersValue(){
        mockIsLayerEnabled(mockLayer1, false); //Disabled top layer
        mockIsLayerEnabled(mockLayer2, true);

        gridData.addAtTop(mockLayer2);
        gridData.addAtTop(mockLayer1);

        assertEquals(sampleContents2, gridData.getPrimaryCellContents(mockCell));
    }

    @Test
    public void testGetPrimaryCellContents_andAddAtFront_whenTopLayerDoesNotHaveValue_andSecondDoes_returnsSecondLayersValue(){
        mockIsLayerEnabled(mockLayerEmpty, true);
        mockIsLayerEnabled(mockLayer2, true);

        gridData.addAtTop(mockLayer2);
        gridData.addAtTop(mockLayerEmpty);

        assertEquals(sampleContents2, gridData.getPrimaryCellContents(mockCell));
    }

    //Color tests
    @Test
    public void testGetCellColors_whenNoLayers_returnsEmptyList(){
        assertTrue(gridData.getCellColors(mockCell).isEmpty());
    }

    @Test
    public void testGetPrimaryCellColor_whenNoLayers_returnsEmptyOptional(){
        assertTrue(gridData.getPrimaryCellColor(mockCell).isEmpty());
    }

    @Test
    public void testGetCellColors_andAddAtFront_whenOneLayer_returnsListContainingLayerValue(){
        mockIsLayerEnabled(mockLayer1, true);
        gridData.addAtTop(mockLayer1);

        final var colors = gridData.getCellColors(mockCell);
        assertEquals(1, colors.size());
        assertEquals(sampleCellColor1, colors.get(0));
    }

    @Test
    public void testGetPrimaryCellColor_andAddAtFront_whenOneLayer_returnsLayerValue(){
        mockIsLayerEnabled(mockLayer1, true);
        gridData.addAtTop(mockLayer1);

        assertEquals(sampleCellColor1, gridData.getPrimaryCellColor(mockCell));
    }

    @Test
    public void testGetCellColors_andAddAtFront_whenMultipleLayers_returnsListContainingLayerValuesInExpectedOrder(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);
        mockIsLayerEnabled(mockLayer3, true);

        gridData.addAtTop(mockLayer3);
        gridData.addAtTop(mockLayer2);
        gridData.addAtTop(mockLayer1);
        final var expectedValues = Arrays.asList(sampleCellColor1, sampleCellColor2, sampleCellColor3);

        assertEquals(expectedValues, gridData.getCellColors(mockCell));
    }

    @Test
    public void testGetPrimaryCellColor_andAddAtFront_whenMultipleLayers_returnsTopLayerCellColor(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);
        mockIsLayerEnabled(mockLayer3, true);

        gridData.addAtTop(mockLayer3);
        gridData.addAtTop(mockLayer2);
        gridData.addAtTop(mockLayer1);

        assertEquals(sampleCellColor1, gridData.getPrimaryCellColor(mockCell));
    }

    @Test
    public void testGetCellColors_andAddAtFront_whenLayerIsDisabled_doesNotReturnItsValueInTheList(){
        mockIsLayerEnabled(mockLayer1, false);
        gridData.addAtTop(mockLayer1);

        assertTrue(gridData.getCellColors(mockCell).isEmpty());
    }

    @Test
    public void testGetPrimaryCellColor_andAddAtFront_whenLayerIsDisabled_doesNotReturnItsValue(){
        mockIsLayerEnabled(mockLayer1, false);
        gridData.addAtTop(mockLayer1);

        assertTrue(gridData.getPrimaryCellColor(mockCell).isEmpty());
    }

    @Test
    public void testGetPrimaryCellColor_andAddAtFront_whenTopLayerIsDisabled_andSecondIsEnabled_returnsSecondLayersValue(){
        mockIsLayerEnabled(mockLayer1, false); //Disabled top layer
        mockIsLayerEnabled(mockLayer2, true);

        gridData.addAtTop(mockLayer2);
        gridData.addAtTop(mockLayer1);

        assertEquals(sampleCellColor2, gridData.getPrimaryCellColor(mockCell));
    }

    @Test
    public void testGetPrimaryCellColor_andAddAtFront_whenTopLayerDoesNotHaveValue_andSecondDoes_returnsSecondLayersValue(){
        mockIsLayerEnabled(mockLayerEmpty, true);
        mockIsLayerEnabled(mockLayer2, true);

        gridData.addAtTop(mockLayer2);
        gridData.addAtTop(mockLayerEmpty);

        assertEquals(sampleCellColor2, gridData.getPrimaryCellColor(mockCell));
    }

    //Highlights tests
    @Test
    public void testGetCellHighlights_whenNoLayers_returnsEmptyList(){
        assertTrue(gridData.getCellHighlights(mockCell).isEmpty());
    }

    @Test
    public void testGetPrimaryCellHighlight_whenNoLayers_returnsEmptyOptional(){
        assertTrue(gridData.getPrimaryCellHighlight(mockCell).isEmpty());
    }

    @Test
    public void testGetCellHighlights_andAddAtFront_whenOneLayer_andLayerHasCellHighlighted_returnsListContainingLayerColor(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsCellHighlighted(mockLayer1, true);
        gridData.addAtTop(mockLayer1);

        final var highlightColors = gridData.getCellHighlights(mockCell);
        assertEquals(1, highlightColors.size());
        assertEquals(Optional.of(sampleLayerColor1), highlightColors.get(0));
    }

    @Test
    public void testGetPrimaryCellHighlight_andAddAtFront_whenOneLayer_andLayerHasCellHighlighted_returnsLayerColor(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsCellHighlighted(mockLayer1, true);
        gridData.addAtTop(mockLayer1);

        assertEquals(Optional.of(sampleLayerColor1), gridData.getPrimaryCellHighlight(mockCell));
    }

    @Test
    public void testGetCellHighlights_andAddAtFront_whenOneLayer_andLayerDoesNotHaveCellHighlighted_returnsListContainingEmptyOptional(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsCellHighlighted(mockLayer1, false);
        gridData.addAtTop(mockLayer1);

        final var highlightColors = gridData.getCellHighlights(mockCell);
        assertEquals(1, highlightColors.size());
        assertTrue(highlightColors.get(0).isEmpty());
    }

    @Test
    public void testGetPrimaryCellHighlight_andAddAtFront_whenOneLayer_andLayerDoesNotHaveCellHighlighted_returnsEmptyOptional(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsCellHighlighted(mockLayer1, false);
        gridData.addAtTop(mockLayer1);

        assertTrue(gridData.getPrimaryCellHighlight(mockCell).isEmpty());
    }

    @Test
    public void testGetCellHighlights_andAddAtFront_whenMultipleLayers_returnsListContainingLayerColorsInExpectedOrder(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);
        mockIsLayerEnabled(mockLayer3, true);

        mockIsCellHighlighted(mockLayer1, true);
        mockIsCellHighlighted(mockLayer2, true);
        mockIsCellHighlighted(mockLayer3, true);

        gridData.addAtTop(mockLayer3);
        gridData.addAtTop(mockLayer2);
        gridData.addAtTop(mockLayer1);
        final var expectedValues = Arrays.asList(Optional.of(sampleLayerColor1), Optional.of(sampleLayerColor2), Optional.of(sampleLayerColor3));

        assertEquals(expectedValues, gridData.getCellHighlights(mockCell));
    }

    @Test
    public void testGetPrimaryCellHighlight_andAddAtFront_whenMultipleLayers_returnsTopLayerColor(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);
        mockIsLayerEnabled(mockLayer3, true);

        mockIsCellHighlighted(mockLayer1, true);
        mockIsCellHighlighted(mockLayer2, true);
        mockIsCellHighlighted(mockLayer3, true);

        gridData.addAtTop(mockLayer3);
        gridData.addAtTop(mockLayer2);
        gridData.addAtTop(mockLayer1);

        assertEquals(Optional.of(sampleLayerColor1), gridData.getPrimaryCellHighlight(mockCell));
    }

    @Test
    public void testGetCellHighlights_andAddAtFront_whenLayerIsDisabled_doesNotReturnItsLayerColorInTheList(){
        mockIsLayerEnabled(mockLayer1, false);
        gridData.addAtTop(mockLayer1);

        assertTrue(gridData.getCellHighlights(mockCell).isEmpty());
    }

    @Test
    public void testGetPrimaryCellHighlight_andAddAtFront_whenLayerIsDisabled_andLayerHasCellHighlighted_doesNotReturnItsLayerColor(){
        mockIsLayerEnabled(mockLayer1, false);
        mockIsCellHighlighted(mockLayer1, true);
        gridData.addAtTop(mockLayer1);

        assertTrue(gridData.getPrimaryCellHighlight(mockCell).isEmpty());
    }

    @Test
    public void testGetPrimaryCellHighlight_andAddAtFront_whenTopLayerIsDisabled_andSecondIsEnabled_returnsSecondLayersValue(){
        mockIsLayerEnabled(mockLayer1, false); //Disabled top layer
        mockIsLayerEnabled(mockLayer2, true);
        mockIsCellHighlighted(mockLayer1, true);
        mockIsCellHighlighted(mockLayer2, true);

        gridData.addAtTop(mockLayer2);
        gridData.addAtTop(mockLayer1);

        assertEquals(Optional.of(sampleLayerColor2), gridData.getPrimaryCellHighlight(mockCell));
    }

    @Test
    public void testGetPrimaryCellHighlight_andAddAtFront_whenTopLayerDoesNotHaveCellHighlight_andSecondDoes_returnsSecondLayersValue(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);

        mockIsCellHighlighted(mockLayer1, false);
        mockIsCellHighlighted(mockLayer2, true);

        gridData.addAtTop(mockLayer2);
        gridData.addAtTop(mockLayer1);

        assertEquals(Optional.of(sampleLayerColor2), gridData.getPrimaryCellHighlight(mockCell));
    }

    //remove() tests
    @Test
    public void testRemove_andAddAtFront_andGetCellContents_whenLayerIsRemoved_itsDataIsNoLongerReturned(){
        mockIsLayerEnabled(mockLayer1, true);

        gridData.addAtTop(mockLayer1);
        gridData.remove(mockLayer1);

        assertTrue(gridData.getCellContents(mockCell).isEmpty());
    }

    @Test
    public void testRemove_andAddAtFront_andGetCellColors_whenLayerIsRemoved_itsDataIsNoLongerReturned(){
        mockIsLayerEnabled(mockLayer1, true);

        gridData.addAtTop(mockLayer1);
        gridData.remove(mockLayer1);

        assertTrue(gridData.getCellColors(mockCell).isEmpty());
    }

    @Test
    public void testRemove_andAddAtFront_andGetCellHighlights_whenLayerIsRemoved_itsDataIsNoLongerReturned(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsCellHighlighted(mockLayer1, true);

        gridData.addAtTop(mockLayer1);
        gridData.remove(mockLayer1);

        assertTrue(gridData.getCellHighlights(mockCell).isEmpty());
    }

    //addAtBack tests
    @Test
    public void testAddAtBack_andGetCellContents_returnsListContainingLayerValuesInExpectedOrder(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);

        gridData.addAtBottom(mockLayer1);
        gridData.addAtBottom(mockLayer2);

        final var expectedValues = Arrays.asList(sampleContents1, sampleContents2);
        assertEquals(expectedValues, gridData.getCellContents(mockCell));
    }

    @Test
    public void testAddAtBack_andAddAtFront_andGetCellColors_returnsColorsInExpectedOrder(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);

        gridData.addAtBottom(mockLayer1);
        gridData.addAtBottom(mockLayer2);

        final var expectedValues = Arrays.asList(sampleCellColor1, sampleCellColor2);
        assertEquals(expectedValues, gridData.getCellColors(mockCell));
    }

    @Test
    public void testAddAtBack_andAddAtFront_andGetCellHighlights_returnsHighlightsInExpectedOrder(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);
        mockIsCellHighlighted(mockLayer1, true);
        mockIsCellHighlighted(mockLayer2, true);

        gridData.addAtBottom(mockLayer1);
        gridData.addAtBottom(mockLayer2);

        final var expectedValues = Arrays.asList(Optional.of(sampleLayerColor1), Optional.of(sampleLayerColor2));
        assertEquals(expectedValues, gridData.getCellHighlights(mockCell));
    }

    //After above & below tests
    @Test
    public void testAddAbove_andAddAtFront_andGetCellContents_whenExistingLayerIsOnlyOneInGridData_returnsDataFromLayersInExpectedOrder(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);

        gridData.addAtTop(mockLayer1);
        gridData.addAbove(mockLayer1, mockLayer2);

        final var expectedValues = Arrays.asList(sampleContents2, sampleContents1);
        assertEquals(expectedValues, gridData.getCellContents(mockCell));
    }

    @Test
    public void testAddAbove_andAddAtFront_andGetCellContents_whenExistingLayerIsLastOneInGridData_returnsDataFromLayersInExpectedOrder(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);
        mockIsLayerEnabled(mockLayer3, true);

        gridData.addAtTop(mockLayer1);
        gridData.addAtTop(mockLayer2);
        gridData.addAbove(mockLayer1, mockLayer3);

        final var expectedValues = Arrays.asList(sampleContents2, sampleContents3, sampleContents1);
        assertEquals(expectedValues, gridData.getCellContents(mockCell));
    }

    @Test
    public void testAddAbove_andAddAtFront_andGetCellContents_whenProvidedLayerNotInGridData_addsLayerToTop_andReturnsDataFromLayersInExpectedOrder(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);

        gridData.addAtTop(mockLayer1);
        gridData.addAbove(mockLayer3, mockLayer2);

        final var expectedValues = Arrays.asList(sampleContents2, sampleContents1);
        assertEquals(expectedValues, gridData.getCellContents(mockCell));
    }

    @Test
    public void testAddBelow_andAddAtFront_andGetCellContents_whenExistingLayerIsOnlyOneInGridData_returnsDataFromLayersInExpectedOrder(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);

        gridData.addAtTop(mockLayer1);
        gridData.addBelow(mockLayer1, mockLayer2);

        final var expectedValues = Arrays.asList(sampleContents1, sampleContents2);
        assertEquals(expectedValues, gridData.getCellContents(mockCell));
    }

    @Test
    public void testAddBelow_andAddAtFront_andGetCellContents_whenExistingLayerIsFirstOneInGridData_returnsDataFromLayersInExpectedOrder(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);
        mockIsLayerEnabled(mockLayer3, true);

        gridData.addAtTop(mockLayer1);
        gridData.addAtTop(mockLayer2);
        gridData.addBelow(mockLayer2, mockLayer3);

        final var expectedValues = Arrays.asList(sampleContents2, sampleContents3, sampleContents1);
        assertEquals(expectedValues, gridData.getCellContents(mockCell));
    }

    @Test
    public void testAddBelow_andAddAtFront_andGetCellContents_whenProvidedLayerNotInGridData_addsLayerToTop_andReturnsDataFromLayersInExpectedOrder(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);

        gridData.addAtTop(mockLayer1);
        gridData.addBelow(mockLayer3, mockLayer2);

        final var expectedValues = Arrays.asList(sampleContents2, sampleContents1);
        assertEquals(expectedValues, gridData.getCellContents(mockCell));
    }

    //Mask tests
    @Test
    public void testUseAsMask_andAddAtFront_andGettters_whenMaskLayerIsSet_andMaskHasCell_allLayersValuesAreReturned(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);
        mockIsCellHighlighted(mockLayer1, true);
        mockIsCellHighlighted(mockLayer2, true);

        mockIsCellSet(mockLayer2, true); //Cell is in mask layer

        gridData.addAtBottom(mockLayer1);
        gridData.addAtBottom(mockLayer2);

        //Method under test
        gridData.useAsMask(mockLayer2);

        assertEquals(Arrays.asList(sampleContents1, sampleContents2), gridData.getCellContents(mockCell));
        assertEquals(Arrays.asList(sampleCellColor1, sampleCellColor2), gridData.getCellColors(mockCell));
        assertEquals(Arrays.asList(Optional.of(sampleLayerColor1), Optional.of(sampleLayerColor2)), gridData.getCellHighlights(mockCell));
    }

    @Test
    public void testUseAsMask_andAddAtFront_andGetCellContents_whenMaskLayerIsSet_andMaskDoesNotHaveCell_noCellContentsAreReturned(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);
        mockIsCellHighlighted(mockLayer1, true);
        mockIsCellHighlighted(mockLayer2, true);
        mockIsCellSet(mockLayer2, false); //Cell is not in mask layer

        gridData.addAtBottom(mockLayer1);
        gridData.addAtBottom(mockLayer2);

        //Method under test
        gridData.useAsMask(mockLayer2);

        final var expectedEmptyList = Arrays.asList(Optional.empty(), Optional.empty());
        assertEquals(expectedEmptyList, gridData.getCellContents(mockCell));
        assertEquals(expectedEmptyList, gridData.getCellColors(mockCell));
        assertEquals(expectedEmptyList, gridData.getCellHighlights(mockCell));
    }

    @Test
    public void testRemoveMask_andUseAsMask_andAddAtFront_andGetCellContents_whenMaskLayerIsSetAndThenRemoved_andMaskDoesNotHaveCell_allLayersCellContentsAreReturned(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);
        mockIsCellHighlighted(mockLayer1, true);
        mockIsCellHighlighted(mockLayer2, true);
        mockIsCellSet(mockLayer2, false); //Cell is not in mask layer

        gridData.addAtBottom(mockLayer1);
        gridData.addAtBottom(mockLayer2);

        //Method under test
        gridData.useAsMask(mockLayer2);
        gridData.removeMask();

        assertEquals(Arrays.asList(sampleContents1, sampleContents2), gridData.getCellContents(mockCell));
        assertEquals(Arrays.asList(sampleCellColor1, sampleCellColor2), gridData.getCellColors(mockCell));
        assertEquals(Arrays.asList(Optional.of(sampleLayerColor1), Optional.of(sampleLayerColor2)), gridData.getCellHighlights(mockCell));
    }

    //Get layer tests
    @Test
    public void testGetActiveLayer_andAddAtFront_forEachIndex_returnsExpectedLayer(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);
        mockIsLayerEnabled(mockLayer3, true);
        gridData.addAtTop(mockLayer3);
        gridData.addAtTop(mockLayer2);
        gridData.addAtTop(mockLayer1);

       assertEquals(mockLayer1, gridData.getActiveLayer(0).get());
       assertEquals(mockLayer2, gridData.getActiveLayer(1).get());
       assertEquals(mockLayer3, gridData.getActiveLayer(2).get());
    }


    @Test
    public void testGetActiveLayer_andAddAtFront_whenLayerIsDisabled_returnsOnlyEnabledLayers(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, false);
        mockIsLayerEnabled(mockLayer3, true);
        gridData.addAtTop(mockLayer3);
        gridData.addAtTop(mockLayer2);
        gridData.addAtTop(mockLayer1);

       assertEquals(mockLayer1, gridData.getActiveLayer(0).get());
       assertEquals(mockLayer3, gridData.getActiveLayer(1).get());
    }

    @Test
    public void testGetActiveLayer_whenNoLayers_returnsEmptyOptional(){
        assertTrue(gridData.getActiveLayer(0).isEmpty());
    }

    @Test
    public void testGetActiveLayer_whenNoEnabledLayers_returnsEmptyOptional(){
        mockIsLayerEnabled(mockLayer1, false);
        gridData.addAtTop(mockLayer1);

        assertTrue(gridData.getActiveLayer(0).isEmpty());        
    }

    @Test
    public void testGetActiveLayer_andAddAtFront_whenNoAccessingNonexistantLayer_returnsEmptyOptional(){
        mockIsLayerEnabled(mockLayer1, true);
        mockIsLayerEnabled(mockLayer2, true);
        mockIsLayerEnabled(mockLayer3, true);
        gridData.addAtTop(mockLayer3);
        gridData.addAtTop(mockLayer2);
        gridData.addAtTop(mockLayer1);

        assertTrue(gridData.getActiveLayer(3).isEmpty());
    }
}
