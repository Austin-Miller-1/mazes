package com.amw.sms.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.amw.sms.algorithms.generation.Sidewinder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Tests for AlgorithmFactory class.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {AlgorithmFactory.class})
public class AlgorithmFactoryTest {
    @MockBean
    private Sidewinder mockSidewinder;

    @MockBean
    private Dijkstra mockDijkstra;

    @Autowired
    private AlgorithmFactory algorithmFactory;

    @Test
    void testGetDijkstra_returnsDijkstraBean() {
        assertEquals(mockDijkstra, algorithmFactory.getDijkstra());
    }

    @Test
    void testGetGenerationAlgorithm_returnsSomeGenerationAlgorithm() {
        assertNotNull(algorithmFactory.getGenerationAlgorithm());
    }

    @Test
    void testGetGenerationAlgorithm_returnsSidewinderBeanByDefault() {
        assertEquals(mockSidewinder, algorithmFactory.getGenerationAlgorithm());
    }

    @Test
    void testGetSolvingAlgorithm_returnsSomeSolvingAlgorithm() {
        assertNotNull(algorithmFactory.getSolvingAlgorithm());
    }

    @Test
    void testGetSolvingAlgorithm_returnsDijkstraBeanByDefault() {
        assertEquals(mockDijkstra, algorithmFactory.getSolvingAlgorithm());
    }
}
