package com.amw.sms.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Tests for GridFactory class.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {GridFactory.class})
public class GridFactoryTest {
    private final int sampleRowCount = 5;
    private final int sampleColCount = 10;

    @Autowired
    private GridFactory gridFactory;

    @Test
    void testCreateGrid_returnsNewInstanceEachTime() {
        assertNotEquals(
            gridFactory.createGrid(sampleRowCount, sampleColCount), 
            gridFactory.createGrid(sampleRowCount, sampleColCount)
        );
    }

    @Test
    void testCreateGrid_returnsInstanceThatUsesProvidedRowAndColumnCounts() {
        final var grid = gridFactory.createGrid(sampleRowCount, sampleColCount);
        assertEquals(sampleRowCount, grid.getRowCount());
        assertEquals(sampleColCount, grid.getColumnCount());
    }
}
