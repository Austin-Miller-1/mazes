package com.amw.sms.mazes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;

import java.util.stream.Stream;

import com.amw.sms.algorithms.AlgorithmFactory;
import com.amw.sms.algorithms.Dijkstra;
import com.amw.sms.algorithms.generation.MazeGenAlgorithm;
import com.amw.sms.grid.Cell;
import com.amw.sms.grid.CellDistances;
import com.amw.sms.grid.Grid;
import com.amw.sms.grid.GridAnimator;
import com.amw.sms.grid.GridAnimatorFactory;
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
    private Grid mockGrid;

    @Mock
    private CellDistances mockDistances;

    @Mock 
    private AlgorithmFactory mockAlgorithmFactory;

    @Mock private MazeGenAlgorithm mockGenAlgorithm1;
    @Mock private MazeGenAlgorithm mockGenAlgorithm2;
    
    @Mock
    private Dijkstra mockDijkstra;

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

    @Mock
    private GridAnimatorFactory mockGridAnimatorFactory;

    @Mock
    private GridAnimator mockGridAnimator;

    /**
     * Creates MazeBuilder with dependency classes mocked out.
     * @return Mocked maze-builder.
     */
    private MazeBuilder newMockedMazeBuilder(){
        Mockito.when(mockAlgorithmFactory.getGenerationAlgorithm())
            .thenReturn(mockGenAlgorithm1);

        //Dijktra should ALWAYS be mocked out in the test
        Mockito.lenient()
            .when(mockAlgorithmFactory.getDijkstra())
            .thenReturn(mockDijkstra);

        Mockito.when(mockGoalBuilderFactory.create(mockGrid))
            .thenReturn(mockGoalBuilder);

        Mockito.when(mockGridAnimatorFactory.create(any(), any()))
            .thenReturn(mockGridAnimator);

        return new MazeBuilder(mockGridFactory, mockGoalBuilderFactory, mockAlgorithmFactory, mockGridAnimatorFactory);
    }

    private void mockGridFactory(){
        Mockito.when(mockGridFactory.createGrid(anyInt(), anyInt()))
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

            //Not always called internally
            Mockito.lenient().when(mockMazeGoal2.getCell())
                .thenReturn(mockCell2);
        }
    }

    /*Size tests */
    @Test
    void testWithSize_returnsMazeWithExpectedRowAndColumnCount() throws InvalidMazeException {
        mockDefaultMazeGoals();

        //Return mockGrid only when specific size is used
        Mockito.when(mockGridFactory.createGrid(5, 6))
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
            new MazeBuilder(mockGridFactory, mockGoalBuilderFactory, mockAlgorithmFactory, mockGridAnimatorFactory)
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
            new MazeBuilder(mockGridFactory, mockGoalBuilderFactory, mockAlgorithmFactory, mockGridAnimatorFactory)
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

        assertEquals(mockCell3, maze.getStartCell());
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

        assertEquals(mockCell3, maze.getEndCell());
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

        assertEquals(mockCell1, maze.getStartCell());
    }
    

    @Test
    void testBuild_whenNoEndProvided_returnsMazeWithLastCellAsExit() throws InvalidMazeException {
        //Test fixture
        mockGridFactory();
        mockDefaultMazeGoals(true, true);

        final var maze = newMockedMazeBuilder()
            .withSize(5, 5)
            .build();

        assertEquals(mockCell2, maze.getEndCell());
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

        assertEquals(mockCell3, maze.getStartCell());
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

        assertEquals(mockCell3, maze.getEndCell());
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

        //Mock results of the two calls to goal buildr
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
        assertEquals(mockCell1, maze.getStartCell());
        assertEquals(mockCell2, maze.getEndCell());
    }
    
    //Commented while showDistances has been removed. Functionality will be readded when wanted.
    /* @Test
    void testShowDistances_whenNotCalled_gridDataIsNotCreatedAndSet() throws InvalidMazeException {
        mockGridFactory();
        mockDefaultMazeGoals();

        newMockedMazeBuilder()
            .withSize(5, 6)
            .build();
        
        Mockito.verify(mockDijkstra, times(0))
            .getDistances(any(), any());

        Mockito.verify(mockGrid, times(0))
            .setGridData(any());
    }
 */
    //Commented while showDistances has been removed. Functionality will be readded when wanted.
    /* 
    @Test
    void testShowDistances_gridDataSetToDistancesFromStartCell() throws InvalidMazeException {
        mockGridFactory();
        mockDefaultMazeGoals();

        Mockito.when(mockDijkstra.getDistances(mockGrid, mockCell1))
            .thenReturn(mockDistances);

        newMockedMazeBuilder()
            .withSize(5, 6)
            .showDistances()
            .build();
        

        Mockito.verify(mockGrid, times(1))
            .setGridData(mockDistances);
    }
    */
}
