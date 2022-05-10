package com.amw.sms.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import com.amw.sms.algorithms.generation.AldousBroder;
import com.amw.sms.algorithms.generation.BinaryTree;
import com.amw.sms.algorithms.generation.MazeGenAlgorithmType;
import com.amw.sms.algorithms.generation.Sidewinder;
import com.amw.sms.algorithms.generation.WilsonsAlgorithm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
    private BinaryTree mockBinaryTree;

    @MockBean
    private Sidewinder mockSidewinder;

    @MockBean
    private AldousBroder mockAldousBroder;

    @MockBean
    private WilsonsAlgorithm mockWilsonsAlgorithm;

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

    @ParameterizedTest
    @MethodSource("enumToExpectedAlgorithm")
    void testGetGenerationAlgorithm_returnsExpectedAlgorithmBasedOnEnum(MazeGenAlgorithmType type, Class expectedAlgorithmClass) {
        assertTrue(expectedAlgorithmClass.isInstance(algorithmFactory.getGenerationAlgorithm(type)));
    }

    static Stream<Arguments> enumToExpectedAlgorithm(){
        return Stream.of(
            arguments(MazeGenAlgorithmType.BINARY_TREE, BinaryTree.class),
            arguments(MazeGenAlgorithmType.SIDEWINDER, Sidewinder.class),
            arguments(MazeGenAlgorithmType.ALDOUS_BRODER, AldousBroder.class),
            arguments(MazeGenAlgorithmType.WILSONS_ALGORITHM, WilsonsAlgorithm.class)

        );
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
