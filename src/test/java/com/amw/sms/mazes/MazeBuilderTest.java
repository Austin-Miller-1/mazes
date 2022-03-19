package com.amw.sms.mazes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MazeBuilderTest {
    /*Size tests */
    @Test
    void testWithSize_returnsMazeWithExpectedRowAndColumnCount() throws InvalidMazeException {
        final var maze = new MazeBuilder()
            .withSize(5, 6)
            .build();

        final var grid = maze.getGrid();
        assertEquals(5, grid.getRowCount());
        assertEquals(6, grid.getColumnCount());
    }

    @ParameterizedTest
    @MethodSource("invalidRowAndColProvider")
    void testWithSize_whenZeroRows_throwsInvalidMazeException(int rows, int columns) throws InvalidMazeException {
        final var exception = assertThrows(InvalidMazeException.class, () -> {
            new MazeBuilder()
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
            new MazeBuilder()
                .build();
        });
        assertNotNull(exception.getMessage());
    }

    /* Setting entrances and exits */
    @Test
    void testStartFrom_andTestWithSize_returnsMazeThatStartsWithSpecifiedCell() throws InvalidMazeException {
        final var maze = new MazeBuilder()
            .withSize(5, 5)
            .startFrom(1, 3)
            .build();

        assertTrue(maze.getStartCell().isPresent());
        final var startCell = maze.getStartCell().get();
        assertEquals(1, startCell.getRowPosition());
        assertEquals(3, startCell.getColumnPosition());
    }

    @Test
    void testEndAt_andTestWithSize_returnsMazeThatEndsWithSpecifiedCell() throws InvalidMazeException {
        final var maze = new MazeBuilder()
            .withSize(5, 5)
            .endAt(1, 3)
            .build();

        assertTrue(maze.getEndCell().isPresent());
        final var endCell = maze.getEndCell().get();
        assertEquals(1, endCell.getRowPosition());
        assertEquals(3, endCell.getColumnPosition());
    }

    /* Non-set entrances and exits */
    @Test
    void testBuild_whenNoStartProvided_returnsMazeThatStartsWithFirstCell() throws InvalidMazeException {
        final var maze = new MazeBuilder()
            .withSize(5, 5)
            .build();

        assertTrue(maze.getStartCell().isPresent());
        final var startCell = maze.getStartCell().get();
        assertEquals(0, startCell.getRowPosition());
        assertEquals(0, startCell.getColumnPosition());
    }
    

    @Test
    void testBuild_whenNoEndProvided_returnsMazeWithLastCellAsExit() throws InvalidMazeException {
        final var rowCount = 4;
        final var colCount = 5;
        
        final var maze = new MazeBuilder()
            .withSize(rowCount, colCount)
            .build();

        assertTrue(maze.getEndCell().isPresent());
        final var endCell = maze.getEndCell().get();
        assertEquals(rowCount-1, endCell.getRowPosition());
        assertEquals(colCount-1, endCell.getColumnPosition());
    }

    /* Random entrances and exits */
    //Requires mocking of grid class or Random class? Both of which should be done when Spring is used.
    @Test
    void testUsingRandomStart() {
        //TODO
    }

    //Requires mocking of grid class or Random class? Both of which should be done when Spring is used.
    @Test
    void testUsingRandomEnd() {
        //TODO
    }

    //Requires mocking of grid class or Random class? Both of which should be done when Spring is used.
    @Test
    void testShowDistances() {
        //TODO
    }

    //Requires mocking of grid class and Dijkstra class
    @Test
    void testUsingAlgorithm() {
        //TODO
    }

    //Requires mocking of grid class and Dijkstra class
    @Test
    void testUsingLongestPath() {
        //TODO
    }
}
