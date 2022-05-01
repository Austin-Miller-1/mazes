package com.amw.sms.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.amw.sms.algorithms.ObservableMazeAlgorithm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Tests for GridAnimatorFactory class.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {GridAnimatorFactory.class})
public class GridAnimatorFactoryTest {
    @Mock
    private Grid mockGrid;

    @Mock
    private ObservableMazeAlgorithm mockAlgorithm;

    @Autowired
    private GridAnimatorFactory gridAnimatorFactory;

    @Test
    void testCreate_returnsNewInstanceEachTime() {
        assertNotEquals(
            gridAnimatorFactory.create(mockGrid, mockAlgorithm), 
            gridAnimatorFactory.create(mockGrid, mockAlgorithm)
        );
    }

    @Test
    void testCreate_returnsInstanceThatUsesProvidedGrid() {
        final var gridAnimator = gridAnimatorFactory.create(mockGrid, mockAlgorithm);
        assertEquals(mockGrid, gridAnimator.getGrid());
    }

    @Test
    void testCreate_returnsInstanceThatUsesProvidedAlgorithm() {
        final var gridAnimator = gridAnimatorFactory.create(mockGrid, mockAlgorithm);
        assertEquals(mockAlgorithm, gridAnimator.getObservedAlgorithm());
    }
}
