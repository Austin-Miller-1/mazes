package com.amw.sms.mazes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;

import java.util.stream.Stream;

import com.amw.sms.algorithms.AlgorithmFactory;
import com.amw.sms.algorithms.generation.MazeGenAlgorithm;
import com.amw.sms.grid.Cell;
import com.amw.sms.grid.DistancesGrid;
import com.amw.sms.grid.GridFactory;
import com.amw.sms.mazes.goals.MazeGoal;
import com.amw.sms.mazes.goals.MazeGoalBuilder;
import com.amw.sms.mazes.goals.MazeGoalBuilderFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MazeBuilderTest {
    @Mock
    private GridFactory mockGridFactory;

    @Mock
    private DistancesGrid mockGrid;

    @Mock 
    private AlgorithmFactory mockAlgorithmFactory;

    @Mock private MazeGenAlgorithm mockGenAlgorithm1;
    @Mock private MazeGenAlgorithm mockGenAlgorithm2;

    @Mock
    private MazeGoalBuilderFactory mockGoalBuilderFactory;

    @Mock
    private MazeGoalBuilder mockGoalBuilder;

    @Mock private MazeGoal mockMazeGoal1;
    @Mock private MazeGoal mockMazeGoal2;

    @Mock private Cell mockCell1;
    @Mock private Cell mockCell2;
    @Mock private Cell mockCell3;
    @Mock private Cell mockCell4;

    /**
     * Creates MazeBuilder with dependency classes mocked out.
     * @return Mocked maze-builder.
     */
    private MazeBuilder newMockedMazeBuilder(){
        Mockito.when(mockAlgorithmFactory.getGenerationAlgorithm())
            .thenReturn(mockGenAlgorithm1);

        Mockito.when(mockGoalBuilderFactory.create(mockGrid))
            .thenReturn(mockGoalBuilder);

        return new MazeBuilder(mockGridFactory, mockGoalBuilderFactory, mockAlgorithmFactory);
    }

    private void mockGridFactory(){
        Mockito.when(mockGridFactory.createDistancesGrid(anyInt(), anyInt()))
            .thenReturn(mockGrid);
    }

    private void mockDefaultMazeGoals(){
        mockDefaultMazeGoals(true, true);
    }

    private void mockDefaultMazeGoals(boolean goalAtStart, boolean goalAtEnd){
        Mockito.when(mockGoalBuilder.entrance())
            .thenReturn(mockGoalBuilder);
        Mockito.when(mockGoalBuilder.exit())
            .thenReturn(mockGoalBuilder);

        //Conditional mocking of "atStart" (expected default behavior)
        if(goalAtStart){
            Mockito.when(mockGoalBuilder.atStart())
                .thenReturn(mockMazeGoal1);

            Mockito.when(mockMazeGoal1.getCell())
                .thenReturn(mockCell1);
        }

        //Conditional mocking of "atEnd" (expected default behavior)
        if(goalAtEnd){
            Mockito.when(mockGoalBuilder.atEnd())
                .thenReturn(mockMazeGoal2);

            Mockito.when(mockMazeGoal2.getCell())
                .thenReturn(mockCell2);
        }
    }

    /*Size tests */
    @Test
    void testWithSize_returnsMazeWithExpectedRowAndColumnCount() throws InvalidMazeException {
        mockDefaultMazeGoals();

        //Return mockGrid only when specific size is used
        Mockito.when(mockGridFactory.createDistancesGrid(5, 6))
            .thenReturn(mockGrid);

        final var maze = newMockedMazeBuilder()
            .withSize(5, 6)
            .build();

        //Assert correct size used
        assertEquals(mockGrid, maze.getGrid());
    }

    @ParameterizedTest
    @MethodSource("invalidRowAndColProvider")
    void testWithSize_whenZeroRows_throwsInvalidMazeException(int rows, int columns) throws InvalidMazeException {
        final var exception = assertThrows(InvalidMazeException.class, () -> {
            new MazeBuilder(mockGridFactory, mockGoalBuilderFactory, mockAlgorithmFactory)
                .withSize(rows, columns)
                .build();
        });
        assertNotNull(exception.getMessage());
    }

    //For invalid size tests
    static Stream<Arguments> invalidRowAndColProvider(){
        return Stream.of(
            arguments(0, 5),    //Zero rows
            arguments(-1, 5),   //Negative rows
            arguments(1, 0),    //Zero cols
            arguments(1, -4)    //Negative cols
        );
    }

    @Test
    void testBuild_whenNoSizeProvided_throwsInvalidMazeException() {
        final var exception = assertThrows(InvalidMazeException.class, () -> {
            new MazeBuilder(mockGridFactory, mockGoalBuilderFactory, mockAlgorithmFactory)
                .build();
        });
        assertNotNull(exception.getMessage());
    }

    /* Setting entrances and exits */
    @Test
    void testStartFrom_andTestWithSize_returnsMazeThatStartsWithSpecifiedCell() throws InvalidMazeException {
        //Test fixture
        mockGridFactory();
        mockDefaultMazeGoals(false, true);
        Mockito.when(mockGoalBuilder.atPosition(1, 3))
            .thenReturn(mockMazeGoal1);
        Mockito.when(mockMazeGoal1.getCell())
            .thenReturn(mockCell3);

        final var maze = newMockedMazeBuilder()
            .withSize(5, 5)
            .startFrom(1, 3)
            .build();

        assertTrue(maze.getStartCell().isPresent());
        assertEquals(mockCell3, maze.getStartCell().get());
    }

    @Test
    void testEndAt_andTestWithSize_returnsMazeThatEndsWithSpecifiedCell() throws InvalidMazeException {
        //Test fixture
        mockGridFactory();
        mockDefaultMazeGoals(true, false);
        Mockito.when(mockGoalBuilder.atPosition(1, 3))
            .thenReturn(mockMazeGoal2);
        Mockito.when(mockMazeGoal2.getCell())
            .thenReturn(mockCell3);

        final var maze = newMockedMazeBuilder()
            .withSize(5, 5)
            .endAt(1, 3)
            .build();

        assertTrue(maze.getEndCell().isPresent());
        assertEquals(mockCell3, maze.getEndCell().get());
    }

    /* Non-set entrances and exits */
    @Test
    void testBuild_whenNoStartProvided_returnsMazeThatStartsWithFirstCell() throws InvalidMazeException {
        //Test fixture
        mockGridFactory();
        mockDefaultMazeGoals(true, true);

        final var maze = newMockedMazeBuilder()
            .withSize(5, 5)
            .build();

        assertTrue(maze.getStartCell().isPresent());
        assertEquals(mockCell1, maze.getStartCell().get());
    }
    

    @Test
    void testBuild_whenNoEndProvided_returnsMazeWithLastCellAsExit() throws InvalidMazeException {
        //Test fixture
        mockGridFactory();
        mockDefaultMazeGoals(true, true);

        final var maze = newMockedMazeBuilder()
            .withSize(5, 5)
            .build();

        assertTrue(maze.getEndCell().isPresent());
        assertEquals(mockCell2, maze.getEndCell().get());
    }

    /* Random entrances and exits */
    @Test
    void testUsingRandomStart_returnsMazeWithRandomStartCell() throws InvalidMazeException {
        //Test fixture
        mockGridFactory();
        mockDefaultMazeGoals(false, true);
        Mockito.when(mockGoalBuilder.atRandom())
            .thenReturn(mockMazeGoal1);
        Mockito.when(mockMazeGoal1.getCell())
            .thenReturn(mockCell3);

        final var maze = newMockedMazeBuilder()
            .withSize(5, 5)
            .usingRandomStart()
            .build();

        assertTrue(maze.getStartCell().isPresent());
        assertEquals(mockCell3, maze.getStartCell().get());
    }

    @Test
    void testUsingRandomEnd_returnsMazeWithRandomEndCell() throws InvalidMazeException {
        //Test fixture
        mockGridFactory();
        mockDefaultMazeGoals(true, false);
        Mockito.when(mockGoalBuilder.atRandom())
            .thenReturn(mockMazeGoal2);
        Mockito.when(mockMazeGoal2.getCell())
            .thenReturn(mockCell3);

        final var maze = newMockedMazeBuilder()
            .withSize(5, 5)
            .usingRandomEnd()
            .build();

        assertTrue(maze.getEndCell().isPresent());
        assertEquals(mockCell3, maze.getEndCell().get());
    }

    //DO AFTER DISTANCES ARE REPLACED
    @Test
    void testShowDistances() {
        //TODO
    }

    @Test
    void testUsingAlgorithm_whenNoAlgorithmProvided_returnsMazeUsingDefaultAlgorithmFromFactory() throws InvalidMazeException {
        //Test fixture
        mockGridFactory();
        mockDefaultMazeGoals(true, true);

        newMockedMazeBuilder()
            .withSize(5, 5)
            .build();

        Mockito.verify(mockGenAlgorithm1, times(1))
            .apply(mockGrid);
    }

    @Test
    void testUsingAlgorithm_returnsMazeBuiltFromProvidedAlgorithm() throws InvalidMazeException {
        //Test fixture
        mockGridFactory();
        mockDefaultMazeGoals(true, true);

        newMockedMazeBuilder()
            .usingAlgorithm(mockGenAlgorithm2)
            .withSize(5, 5)
            .build();

        //Provided algorithm called
        Mockito.verify(mockGenAlgorithm2, times(1))
            .apply(mockGrid);

        //Default algorithm not called
        Mockito.verify(mockGenAlgorithm1, times(0))
            .apply(mockGrid);
    }

    @Test
    void testUsingLongestPath_returnsMazeWithStartAndEndAtCellsFurthestFromEachOther() throws InvalidMazeException {
        //Test fixture
        mockGridFactory();
        mockDefaultMazeGoals(false, false);

        //Mock results of the two calls to goal builder
        Mockito.when(mockGoalBuilder.farthestFrom(any()))
            .thenReturn(mockMazeGoal1)
            .thenReturn(mockMazeGoal2);

        //Mock goals
        Mockito.when(mockMazeGoal1.getCell()).thenReturn(mockCell1);
        Mockito.when(mockMazeGoal2.getCell()).thenReturn(mockCell2);

        //Test method
        final var maze = newMockedMazeBuilder()
            .usingLongestPath()
            .withSize(5, 5)
            .build();

        //Assert longest path used:
        //1. farthestFrom called twice
        ArgumentCaptor<Cell> cellCaptor = ArgumentCaptor.forClass(Cell.class);
        Mockito.verify(mockGoalBuilder, times(2))
            .farthestFrom(cellCaptor.capture());

        //2. Second call uses result of first call => furthest path used
        assertEquals(mockCell1, cellCaptor.getAllValues().get(1));

        //3. Resulting cells are used for start and finish
        assertTrue(maze.getStartCell().isPresent());
        assertEquals(mockCell1, maze.getStartCell().get());
        assertTrue(maze.getEndCell().isPresent());
        assertEquals(mockCell2, maze.getEndCell().get());
    }
}
