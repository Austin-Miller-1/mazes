package com.amw.sms.mazes.goals;

import com.amw.sms.algorithms.Dijkstra;
import com.amw.sms.grid.Cell;
import com.amw.sms.grid.Grid;
import com.amw.sms.mazes.InvalidMazeException;

public class MazeGoalBuilder {
    private final Grid grid;
    private MazeGoalType type = MazeGoalType.GENERIC;

    public MazeGoalBuilder(Grid grid){
        this.grid = grid;
    }

    public MazeGoalBuilder exit(){
        this.type = MazeGoalType.EXIT;
        return this;
    }

    public MazeGoalBuilder entrance(){
        this.type = MazeGoalType.ENTRANCE;
        return this;
    }

    public MazeGoal atStart(){
        return this.build(this.grid.getFirstCell());
    }

    public MazeGoal atEnd(){
        return this.build(this.grid.getLastCell());
    }

    public MazeGoal atRandom(){
        return this.build(this.grid.getRandomCell());
    }

    public MazeGoal atPosition(int row, int column) throws InvalidMazeException{
        final var chosenCell = this.grid.getCell(row, column);
        if(chosenCell.isEmpty()){
            final var message = "Cell at row %s, column %s does not exist within grid of size %sx%s"
                .formatted(row, column, this.grid.getRowCount(), this.grid.getColumnCount());
            throw new InvalidMazeException(message);
        }

        return this.build(chosenCell.get());
    }

    public MazeGoal farthestFrom(Cell cell){
        final var furthestCell = new Dijkstra()
            .getDistances(this.grid, cell)
            .getMax()
            .getKey();
        
        return this.build(furthestCell);
    }

    private MazeGoal build(Cell cell){
        return new MazeGoal(cell, this.type);
    }
}
