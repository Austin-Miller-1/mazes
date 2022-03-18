package com.amw.mazes;

import java.util.AbstractMap;
import java.util.Map.Entry;

import com.amw.mazes.algorithms.Dijkstra;
import com.amw.mazes.algorithms.generation.MazeGenAlgorithm;
import com.amw.mazes.algorithms.generation.Sidewinder;
import com.amw.mazes.grid.Cell;
import com.amw.mazes.grid.DistancesGrid;
import com.amw.mazes.grid.Grid;

public class MazeBuilder {
    private final Dijkstra dijk;    //Used for maze generation
    private int rowCount = 0, colCount = 0;
    private int startRow = 0, startColumn = 0; //TODO do we use defaults still? We shouldn't leave them uninitialized but we also will never use the default values...
    private int endRow = 0, endColumn = 0;
    private MazeGenAlgorithm genAlgorithm;
    private boolean startAtFirst = true;
    private boolean endAtLast = true;
    private boolean useLongestPath = false;
    private boolean useRandomStart = false;
    private boolean useRandomEnd = false;
    private boolean showDistances = false;

    public MazeBuilder(){
        dijk = new Dijkstra();
        genAlgorithm = new Sidewinder();
    }

    public MazeBuilder withSize(int rowCount, int colCount){
        this.rowCount = rowCount;
        this.colCount = colCount;
        return this;
    }

    public MazeBuilder usingAlgorithm(MazeGenAlgorithm genAlgorithm){
        this.genAlgorithm = genAlgorithm;
        return this;
    }

    public MazeBuilder startFrom(int row, int column){
        this.startAtFirst = false;

        this.startRow = row;
        this.startColumn = column;
        return this;
    }

    public MazeBuilder endAt(int row, int column){
        this.endAtLast = false;

        this.endRow = row;
        this.endColumn = column;
        return this;
    }

    public MazeBuilder usingLongestPath(){
        this.startAtFirst = false;
        this.endAtLast = false;

        this.useLongestPath = true;
        return this;
    }

    public MazeBuilder usingRandomStart(){
        this.startAtFirst = false;
        this.useRandomStart = true;
        return this;
    }

    public MazeBuilder usingRandomEnd(){
        this.endAtLast = false;
        this.useRandomEnd = true;
        return this;
    }

    public MazeBuilder showDistances(){
        this.showDistances = true;
        return this;
    }

    public Maze build() throws InvalidMazeException{
        final var grid = new DistancesGrid(this.rowCount, this.colCount);
        System.out.println("-- START MAZE BUILDER -- \n Empty grid"); //todo Temp
        System.out.println(grid); //todo Temp

        this.genAlgorithm.apply(grid);
        final Entry<Cell, Cell> entrances = this.getEntrances(grid);

        if(showDistances){
            grid.setDistances(dijk.getDistances(grid, entrances.getKey()));
        }

        return new Maze(grid, entrances);
    }

    private Entry<Cell, Cell> getEntrances(Grid grid) throws InvalidMazeException{
        Cell startCell, endCell;

        //Longest path requires we get end first, then start
        if(this.useLongestPath){
            endCell = this.dijk.getDistances(grid, grid.getRandomCell()).getMax().getKey();
            startCell = this.dijk.getDistances(grid, endCell).getMax().getKey();
            return new AbstractMap.SimpleEntry<Cell,Cell>(startCell, endCell);
        }
        
        //If not longest path, get start and end individually
        //Start cell
        if(this.useRandomStart)     startCell = grid.getRandomCell();
        else if(this.startAtFirst)  startCell = grid.getCell(0, 0).get();
        else {
            final var chosenCell = grid.getCell(this.startRow, this.startColumn);
            if(chosenCell.isEmpty()){
                final var message = "Starting cell at row %s, column %s does not exist within grid of size %sx%s"
                    .formatted(this.startRow, this.startColumn, this.rowCount, this.colCount);
                throw new InvalidMazeException(message);
            }
    
            startCell = chosenCell.get();
        }

        //End cell
        if(this.useRandomEnd)   endCell = grid.getRandomCell();
        else if(this.endAtLast) endCell = grid.getCell(grid.getRowCount()-1, grid.getColumnCount()-1).get();
        else {
            final var chosenCell = grid.getCell(this.endRow, this.endColumn);
            if(chosenCell.isEmpty()){
                final var message = "Ending cell at row %s, column %s does not exist within grid of size %sx%s"
                    .formatted(this.endRow, this.endColumn, this.rowCount, this.colCount);
                throw new InvalidMazeException(message);
            }
    
            endCell = chosenCell.get();
        }

        return new AbstractMap.SimpleEntry<Cell,Cell>(startCell, endCell);
    }
}
