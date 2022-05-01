package com.amw.sms.algorithms.generation;

import com.amw.sms.grid.Grid;
import com.amw.sms.algorithms.ObservableMazeAlgorithm;

/**
 * Interface for maze-generation algorithms. When applied to a grid, such an algorithm
 * will create paths in order to construct a maze.
 */
public abstract class MazeGenAlgorithm extends ObservableMazeAlgorithm{
    /**
     * Apply the maze-generation algorithm to the grid.
     * @param grid Grid to update into a maze.
     */
    public abstract void apply(Grid grid);
}
