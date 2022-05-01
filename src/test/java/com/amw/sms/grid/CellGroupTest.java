package com.amw.sms.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for CellGroup.
 */
@ExtendWith(MockitoExtension.class)
public class CellGroupTest {
    @Mock
    Collection<Cell> mockCollection;

    private String sampleGroupName = "group1";

    @Test
    void testGetName_returnsTheNameOfTheGroup() {
        assertEquals(sampleGroupName, new CellGroup(sampleGroupName, mockCollection).getName());
    }

    @Test
    void testGetCells_returnsTheGroupsCells() {
        assertEquals(mockCollection, new CellGroup(sampleGroupName, mockCollection).getCells());
    }
}
