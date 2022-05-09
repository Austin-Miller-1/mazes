package com.amw.sms.mazes;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

import com.amw.sms.grid.Cell;
import com.amw.sms.grid.CellPath;
import com.amw.sms.grid.Grid;
import com.amw.sms.grid.data.CellPathGridDataLayer;
import com.amw.sms.grid.data.GridDataLayer;
import com.amw.sms.grid.data.SimpleGridDataLayer;
import com.amw.sms.mazes.goals.MazeGoal;
import com.amw.sms.util.Pair;

/**
 * High-level class representing a maze. Mazes consist of a grid of cells and 
 * an entrance and exit. 
 * 
 * Mazes should not be created directly using this class and should instead be built using the MazeBuilder class. 
 * TODO - if that's the case, then Maze should be made an innerclass and follow the builder pattern more standardly, right?
 * @see MazeBuilder
 */
public class Maze {
    final static String ENTRANCE_DATA_LAYER_NAME = "Start and exit";
    final static String ENTRANCE_START_CONTENTS = "S";
    final static String ENTRANCE_EXIT_CONTENTS = "E";

    final static String SOLUTION_DATA_LAYER_NAME = "Solution";
    final static Color SOLUTION_DATA_LAYER_COLOR = Color.RED;

    private final Grid grid;
    private final Pair<MazeGoal, MazeGoal> entrances;
    private final GridDataLayer entranceLayer;

    private Optional<CellPathGridDataLayer> solutionLayer;

    /**
     * Construct maze using provided grid and the specified start and end.
     * @param grid Grid of cells representing maze.
     * @param startAndEnd Pair of cells representing the entrance and exit of the maze.
     */
    public Maze(Grid grid, Pair<MazeGoal, MazeGoal> startAndEnd){
        this.grid = grid;
        this.entrances = startAndEnd;
        this.solutionLayer = Optional.empty();

        //Display start and finish on grid
        this.entranceLayer = this.getEntranceLayer();
        this.grid.getGridData().addAtTop(this.entranceLayer);
    }

    /**
     * Constructs new grid-data layer showing the start and end of the maze.
     * @return
     */
    private GridDataLayer getEntranceLayer(){
        final var layer = new SimpleGridDataLayer(ENTRANCE_DATA_LAYER_NAME);
        layer.setCellContents(this.entrances.getFirst().getCell(), ENTRANCE_START_CONTENTS);
        layer.setCellContents(this.entrances.getSecond().getCell(), ENTRANCE_EXIT_CONTENTS);
        return layer;
    }

    /**
     * Return the maze's starting cell.
     * @return Starting cell of the maze.
     */
    public Cell getStartCell(){
        return this.entrances.getFirst().getCell();
    }

    /**
     * Return the maze's exit cell, if one has been set.
     * @return Optional containing the exit cell, if it exists.
     */
    public Cell getEndCell(){
        return this.entrances.getSecond().getCell();
    }

    /**
     * Returns the grid used internally by the maze. This should be used only when classes need to work
     * with the maze a low level, such as for solving the maze.
     * @return Grid used to represent the maze internally.
     */
    public Grid getGrid(){
        return this.grid;
    }

    public void applySolution(final CellPath solution){
        final var gridData = this.grid.getGridData();

        if(this.solutionLayer.isPresent()){
            this.solutionLayer.get().setPath(solution);
        }
        else{
            final var solutionLayer = new CellPathGridDataLayer(solution, SOLUTION_DATA_LAYER_NAME, SOLUTION_DATA_LAYER_COLOR);
            solutionLayer.enableCellColors();
            gridData.addBelow(this.entranceLayer, solutionLayer);
            gridData.useAsMask(solutionLayer);
            this.solutionLayer = Optional.of(solutionLayer);    
        }
    }

    /**
     * TODO - "display" is not very accurate.. It's more like applying a path? Or setting a path? But both of those are 
     * also strange...
     * 
     * Displays the provided path in maze.
     * @param path List of cells representing a path between a start and a finish. Path should be between the start and exit
     * cells of the maze.
     */
    @Deprecated
    public void displaySolution(List<Cell> path){
        final var gridData = this.grid.getGridData();
        final var cellPath = new CellPath("path", path);
        final var solutionLayer = new CellPathGridDataLayer(cellPath, SOLUTION_DATA_LAYER_NAME, SOLUTION_DATA_LAYER_COLOR);
        solutionLayer.enableCellColors();
        gridData.addAtTop(solutionLayer);
        gridData.useAsMask(solutionLayer);
        
        gridData.remove(this.entranceLayer);
        gridData.addAtTop(this.entranceLayer);
    }

    /**
     * Returns string representation of the grid.
     */
    public String toString(){
        return this.grid.toString();
    }
}
