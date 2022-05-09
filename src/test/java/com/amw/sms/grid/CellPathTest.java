package com.amw.sms.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for CellPath.
 */
@ExtendWith(MockitoExtension.class)
public class CellPathTest {
    @Mock private Cell mockCell1;
    @Mock private Cell mockCell2;

    @Test
    void testGetCells_andGetName_returnsSetValues() {
        final var cells = new ArrayList<Cell>();
        cells.add(mockCell1);
        cells.add(mockCell2);

        final var path = new CellPath("path", cells);
        assertEquals(cells, path.getCells());
        assertEquals("path", path.getName());
    }
}
