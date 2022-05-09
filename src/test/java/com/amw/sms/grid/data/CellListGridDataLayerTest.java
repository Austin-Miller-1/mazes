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
 * Tests for CellListGridDataLayer.
 */
@ExtendWith(MockitoExtension.class)
public class CellListGridDataLayerTest extends AbstractCellListGridDataLayerTest {
    @Mock Cell mockCell1;
    @Mock Cell mockCell2;

    Color sampleColor = Color.BLUE;
    String sampleContentsString = "AA";

    /**
     * Returns the layer instance to test on. 
     * Purpose is to be overridden by subclasses.
     * @return CellListGridDataLayer to test.
     */
    void setupLayerUnderTest(){
        this.innerList = new ArrayList<>();
        this.cellList = new CellList("list", this.innerList);
        this.layer = new CellListGridDataLayer(this.cellList, "layer", this.sampleColor);
    }
}
