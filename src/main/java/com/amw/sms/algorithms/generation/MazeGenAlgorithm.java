package com.amw.sms.algorithms.generation;

import com.amw.sms.grid.Grid;

/**
 * Interface for maze-generation algorithms. When applied to a grid, such an algorithm
 * will create paths in order to construct a maze.
 */
public interface MazeGenAlgorithm {
    /**
     * Apply the maze-generation algorithm to the grid.
     * @param grid Grid to update into a maze.
     */
    public void apply(Grid grid);
}
