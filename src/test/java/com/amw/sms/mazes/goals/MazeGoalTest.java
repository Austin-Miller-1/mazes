package com.amw.sms.mazes.goals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.amw.sms.grid.Cell;

import org.junit.jupiter.api.Test;

public class MazeGoalTest {
    @Test
    void testGetCell() {
        final var cell = new Cell(1, 1);
        final var goal = new MazeGoal(cell, MazeGoalType.ENTRANCE);
        assertEquals(cell, goal.getCell());
    }

    @Test
    void testGetType() {
        final var goal = new MazeGoal(new Cell(1, 1), MazeGoalType.EXIT);
        assertEquals(MazeGoalType.EXIT, goal.getType());
    }
}
