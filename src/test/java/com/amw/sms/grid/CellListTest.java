package com.amw.sms.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for CellList.
 */
@ExtendWith(MockitoExtension.class)
public class CellListTest {
    @Mock
    List<Cell> mockList;

    private String sampleListName = "group1";

    @Test
    void testGetName_returnsTheNameOfTheList() {
        assertEquals(sampleListName, new CellList(sampleListName, mockList).getName());
    }

    @Test
    void testGetCells_returnsTheListsCells() {
        assertEquals(mockList, new CellList(sampleListName, mockList).getCells());
    }
}
