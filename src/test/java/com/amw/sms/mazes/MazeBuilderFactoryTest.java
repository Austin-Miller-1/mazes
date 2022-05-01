package com.amw.sms.mazes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.amw.sms.algorithms.AlgorithmFactory;
import com.amw.sms.grid.GridAnimatorFactory;
import com.amw.sms.grid.GridFactory;
import com.amw.sms.mazes.goals.MazeGoalBuilderFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Tests for MazeBuilderFactory.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MazeBuilderFactory.class})
public class MazeBuilderFactoryTest {
    @MockBean
    private GridFactory mockGridFactory;

    @MockBean
    private MazeGoalBuilderFactory mockGoalBuilderFactory;

    @MockBean
    private AlgorithmFactory mockAlgorithmFactory;

    @MockBean
    private GridAnimatorFactory mockGridAnimatorFactory;

    @Autowired
    private MazeBuilderFactory mazeBuilderFactory;

    @Test
    void testCreate_returnsBuilderUsingExpectedBeans() {
        final var mazeBuilder = mazeBuilderFactory.create();
        
        assertEquals(mockGridFactory, mazeBuilder.getGridFactory());
        assertEquals(mockGoalBuilderFactory, mazeBuilder.getGoalBuilderFactory());
        assertEquals(mockAlgorithmFactory, mazeBuilder.getAlgorithmFactory());
        assertEquals(mockGridAnimatorFactory, mazeBuilder.getGridAnimatorFactory());
    }
}
