package com.amw.sms.mazes.goals;

import com.amw.sms.algorithms.AlgorithmFactory;
import com.amw.sms.algorithms.Dijkstra;
import com.amw.sms.grid.Cell;
import com.amw.sms.grid.Grid;
import com.amw.sms.mazes.InvalidMazeException;

/**
 * Builder for MazeGoals. Simplifies the goal creation process by
 * providing common goal functionality.
 */
public class MazeGoalBuilder {
    private final Grid grid;
    private final AlgorithmFactory algorithmFactory;
    private MazeGoalType type = MazeGoalType.GENERIC;

    /**
     * Constructs MazeGoalBuilder for the provided grid using the provided AlgorithmFactory.
     * @param algorithmFactory The algorithm factory that will be used during the maze-goal's build process.
     * @param grid Grid for which this builder is for.
     */
    public MazeGoalBuilder(AlgorithmFactory algorithmFactory, Grid grid){
        this.algorithmFactory = algorithmFactory;
        this.grid = grid;
    }

    /**
     * Sets the goal type to "exit".
     * @return The builder
     */
    public MazeGoalBuilder exit(){
        this.type = MazeGoalType.EXIT;
        return this;
    }

    /**
     * Sets the goal type to "entrance".
     * @return The builder
     */
    public MazeGoalBuilder entrance(){
        this.type = MazeGoalType.ENTRANCE;
        return this;
    }

    /**
     * Builds the maze goal at the start of the maze, i.e. the cell deemed by the grid to be first cell.
     * @return The built maze-goal.
     */
    public MazeGoal atStart(){
        return this.build(this.grid.getFirstCell());
    }

    /**
     * Builds the maze goal at the end of the maze, i.e. the cell deemed by the grid to be last cell.
     * @return The built maze-goal.
     */
    public MazeGoal atEnd(){
        return this.build(this.grid.getLastCell());
    }

    /**
     * Builds the maze goal at a random cell in the grid.
     * @return The built maze-goal.
     */
    public MazeGoal atRandom(){
        return this.build(this.grid.getRandomCell());
    }

    /**
     * Builds the maze goal at the specified cell-location in the grid.
     * @param row The row number of the cell where the goal is located. 
     * @param column The column number of the cell where the goal is located.
     * @return The built maze-goal.
     * @throws InvalidMazeException if there is no cell at the provided location.
     */
    public MazeGoal atPosition(int row, int column) throws InvalidMazeException{
        final var chosenCell = this.grid.getCell(row, column);
        if(chosenCell.isEmpty()){
            final var message = "Cell at row %s, column %s does not exist within grid of size %sx%s"
                .formatted(row, column, this.grid.getRowCount(), this.grid.getColumnCount());
            throw new InvalidMazeException(message);
        }

        return this.build(chosenCell.get());
    }

    /**
     * Builds the maze goal at the cell furthest from the provided Cell.
     * @param cell Cell for which this maze goal is built relative to. The maze goal will be set at the 
     * cell furthest from this provided cell.
     * @return The built maze-goal.
     */
    public MazeGoal farthestFrom(Cell cell){
        final var furthestCell = this.algorithmFactory
            .getDijkstra()
            .getDistances(this.grid, cell)
            .getMax()
            .getKey();
        
        return this.build(furthestCell);
    }

    /**
     * Build the maze goal. Used internally within all other build methods. 
     * Privated as it should not be used by external classes. Maze goals must be built using one
     * of the other build methods.
     * @param cell Cell to built the maze goal at.
     * @return The built maze-goal.
     */
    private MazeGoal build(Cell cell){
        return new MazeGoal(cell, this.type);
    }

    /**
     * Get the grid that the goal will be for.
     * @return The grid.
     */
    Grid getGrid(){
        return this.grid;
    }

    /**
     * Get the algorithm factory to be used by the builder. 
     * @return The algorithm factory.
     */
    AlgorithmFactory getAlgorithmFactory(){
        return this.algorithmFactory;
    }
}
