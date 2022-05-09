package com.amw.sms.grid.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import com.amw.sms.grid.CellPath;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CellPathGridDataLayerTest extends AbstractCellListGridDataLayerTest {
    @Mock
    private CellPath mockPath;

    //Object under test
    private CellPath path;
    private CellPathGridDataLayer pathLayer;

    /**
     * Returns the layer instance to test on. 
     * @return CellListGridDataLayer to test.
     */
    @Override
    void setupLayerUnderTest(){
        this.innerList = new ArrayList<>();
        this.path = new CellPath("list", this.innerList);
        this.pathLayer = new CellPathGridDataLayer(this.path, "layer", this.sampleColor);

        //For inherited tests
        this.cellList = this.path;
        this.layer = this.pathLayer;
    }

    //Path tests
    @Test
    void testGetPath_returnsSetCellPath() {
        assertEquals(path, layer.getPath().get());
    }

    @Test
    void testSetPath_andGetPath_returnsNewlySetCellPath(){
        pathLayer.setPath(mockPath);
        assertEquals(mockPath, pathLayer.getPath().get());
    }
}
