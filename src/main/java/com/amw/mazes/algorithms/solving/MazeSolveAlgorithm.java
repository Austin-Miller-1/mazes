package com.amw.mazes.algorithms.solving;

import java.util.List;

import com.amw.mazes.grid.Cell;
import com.amw.mazes.grid.Maze;

/**
 * Interface for all maze-solving algorithms.
 */
public abstract class MazeSolveAlgorithm {
    /**
     * Returns the path of cells between the start and end entrances of the maze. 
     * This path includes both entrance cells. 
     * @param maze Maze to solve.
     * @return List of cells representing the path from the start entrance to the end entrance. This list
     * will start with the with start-entrance cell and ends with the end-entrance (the exit) cell. 
     * If the maze has no entrances, an empty list is returned. 
     * If there exists no path between the two cells, then an empty list is returned.
     */
    public abstract List<Cell> getSolution(Maze maze);

    /**
     * Solves the maze. This maze will be updated internally with this solution such that, for instance,
     * it is displayed when the maze is rendered.
     * @param maze Maze to solve.
     */
    public void solve(Maze maze){
        maze.displayPath(this.getSolution(maze));
    }
}
