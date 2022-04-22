package com.amw.sms.mazes;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;

import com.amw.sms.grid.Cell;
import com.amw.sms.grid.Grid;
import com.amw.sms.mazes.goals.MazeGoal;
import com.amw.sms.util.Pair;

/**
 * High-level class representing a maze. Mazes consist of a grid of cells and 
 * optionally an entrance and exit. This means that mazes can be built as solvable or
 * unsolvable. 
 * A solvable maze is one with an entrance and exit such that at least one path exists between
 * the entrance and exit.
 * An unsolvable maze is one without entrances OR one that has entrances for which no path exists between them.
 * A solvable maze can be made insolvable and vice versa by setting its entrances at runtime.
 * 
 * Mazes should not be created directly using this class and should instead be built using the MazeBuilder class. 
 * TODO - if that's the case, then Maze should be made an innerclass and follow the builder pattern more standardly, right?
 * @see MazeBuilder
 */
public class Maze {
    private Grid grid;
    private Pair<MazeGoal, MazeGoal> entrances;

    /**
     * Construct maze using provided grid and the specified start and end.
     * @param grid Grid of cells representing maze.
     * @param startAndEnd Pair of cells representing the entrance and exit of the maze.
     */
    public Maze(Grid grid, Pair<MazeGoal, MazeGoal> startAndEnd){
        this.grid = grid;
        this.entrances = startAndEnd;
    }

    /**
     * TODO - REFACTOR
     * Returns the maze's entrances, if any.
     * @return Optional containing the entrances if any.
     */
    public Optional<Entry<Cell, Cell>> getEntrances(){
        return Optional.of(
            new AbstractMap.SimpleEntry<Cell, Cell>(
                entrances.getFirst().getCell(), 
                entrances.getSecond().getCell()
            )
        );
    }

    /**
     * TODO - REFACTOR
     * Return the maze's start cell, if one has been set.
     * @return Optional containing the start cell, if it exists.
     */
    public Optional<Cell> getStartCell(){
        return Optional.of(this.entrances.getFirst().getCell());
    }

    /**
     * TODO - REFACTOR
     * Return the maze's exit cell, if one has been set.
     * @return Optional containing the exit cell, if it exists.
     */
    public Optional<Cell> getEndCell(){
        return Optional.of(this.entrances.getSecond().getCell());
    }

    /**
     * Returns the grid used internally by the maze. This should be used only when classes need to work
     * with the maze a low level, such as for solving the maze.
     * @return Grid used to represent the maze internally.
     */
    public Grid getGrid(){
        return this.grid;
    }

    /**
     * TODO - "display" is not very accurate.. It's more like applying a path? Or setting a path? But both of those are 
     * also strange...
     * 
     * Displays the provided path in maze.
     * @param path List of cells representing a path between a start and a finish. Path should be between the start and exit
     * cells of the maze.
     */
    public void displayPath(List<Cell> path){
        this.grid.setPath(path);
        this.grid.displayPathExclusively();
    }

    /**
     * Returns string representation of the grid.
     */
    public String toString(){
        return this.grid.toString();
    }
}
