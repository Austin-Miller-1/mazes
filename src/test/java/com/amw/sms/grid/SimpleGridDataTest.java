package com.amw.sms.grid;

import java.awt.Color;

import com.amw.sms.grid.griddata.SampleGridDataImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test CellDistances class.
 */
@ExtendWith(MockitoExtension.class)
public class SimpleGridDataTest {
    @Mock
    private Grid mockGrid;

    //Cell mocks
    @Mock private Cell mockCell1;
    @Mock private Cell mockCell2;
    @Mock private Cell mockCell3;
    @Mock private Cell mockRoot;

    private SimpleGridData gridData;

    private final String sampleContents1 = "test";
    private final String sampleContents2 = "A";
    private final Color sampleColor1 =  new Color(0x00FF00);
    private final Color sampleColor2 = new Color(0xAABBCC);

    @BeforeEach
    void beforeEach(){
        gridData = new SimpleGridData(mockGrid);
    }
  
    @Test
    void testGetCellContents_andSetCellContents_returnsSetCellContents(){
        gridData.setCellContents(mockCell1, sampleContents1);
        gridData.setCellContents(mockCell2, sampleContents2);

        assertEquals(sampleContents1, gridData.getCellContentsDEP(mockCell1));
    }

    @Test
    void testGetCellContents_andSetCellContents_whenCellIsUnset_returnsDefaultValueFromGridDataParentClass(){
        gridData.setCellContents(mockCell1, sampleContents1);

        final var expectedDefaultContents = new SampleGridDataImpl(mockGrid).getCellContentsDEP(mockCell2); 
        assertEquals(expectedDefaultContents, gridData.getCellContentsDEP(mockCell2));
    }

    @Test
    void testGetCellColor_andTestSetCellColor_returnsSetCellContents(){
        gridData.setCellColor(mockCell1, sampleColor1);
        gridData.setCellColor(mockCell2, sampleColor2);

        assertEquals(sampleColor1, gridData.getCellColorDEP(mockCell1));
    }

    @Test
    void testGetCellColor_andTestSetCellColor_whenCellIsUnset_returnsDefaultValueFromGridDataParentClass(){
        gridData.setCellColor(mockCell1, sampleColor1);

        final var expectedDefaultContents = new SampleGridDataImpl(mockGrid).getCellColorDEP(mockCell2); 
        assertEquals(expectedDefaultContents, gridData.getCellColorDEP(mockCell2));
    }
}
