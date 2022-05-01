package com.amw.sms.mazes.goals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.amw.sms.algorithms.AlgorithmFactory;
import com.amw.sms.algorithms.Dijkstra;
import com.amw.sms.grid.Cell;
import com.amw.sms.grid.Grid;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Tests for MazeGoalBuilderFactory.
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {MazeGoalBuilderFactory.class})
public class MazeGoalBuilderFactoryTest {
    @MockBean
    private AlgorithmFactory mockAlgorithmFactory;

    @MockBean
    private Dijkstra mockDijkstra;

    @Mock
    private Grid mockGrid;

    @Mock
    private Cell mockCell;

    @Autowired
    private MazeGoalBuilderFactory builderFactory;

    @Test
    void testCreate_returnsNewBuilderInstanceEachTime() {
        assertNotEquals(builderFactory.create(mockGrid), builderFactory.create(mockGrid));
    }

    @Test
    void testCreate_returnsBuilderInstanceThatUsesProvidedGrid() {
        final var goalBuilder = builderFactory.create(mockGrid);
        assertEquals(mockGrid, goalBuilder.getGrid());
    }

    @Test
    void testCreate_returnsBuilderInstanceThatUsesAlgorithmFactoryBean() {
        final var goalBuilder = builderFactory.create(mockGrid);
        assertEquals(mockAlgorithmFactory, goalBuilder.getAlgorithmFactory());
    }
}
