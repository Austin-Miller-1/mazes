package com.amw.sms.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for GridData abstract class. Uses SampleGridDataImpl to test.
 */
@ExtendWith(MockitoExtension.class)
public class GridDataTest {
    @Mock
    Grid mockGrid;

    @Mock
    Cell mockCell;

    private GridData gridData;

    @BeforeEach
    public void beforeEach(){
        gridData = new SampleGridDataImpl(mockGrid);
    }

    @Test
    public void testGetGrid_returnsProvidedGrid(){
        assertEquals(mockGrid, gridData.getGrid());
    }

    @Test
    public void testGetCellContents_returnsStringWithOneWhiteSpaceChar(){
        assertEquals(" ", gridData.getCellContents(mockCell));
    }

    @Test
    public void testGetCellColor_returnsWhiteAsDefaultValue(){
        assertEquals(Color.WHITE, gridData.getCellColor(mockCell));
    }
}
