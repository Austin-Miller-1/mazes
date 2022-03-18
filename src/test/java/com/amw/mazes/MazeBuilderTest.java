package com.amw.mazes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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

    @Test
    void testBuild_whenNoSizeProvided_throwsInvalidMazeException() {
        
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

    @Test
    void testStartFrom_andTestWithSize_returnsMazeThatEndsWithSpecifiedCell() throws InvalidMazeException {
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
    void testShowDistances() {
        
    }

    @Test
    void testUsingAlgorithm() {

    }

    @Test
    void testUsingLongestPath() {

    }

    @Test
    void testUsingRandomEnd() {

    }

    @Test
    void testUsingRandomStart() {

    }



    @Test
    void testBuild_whenNoValuesProvided_returnsInstanceOfMaze() {

    }
}
