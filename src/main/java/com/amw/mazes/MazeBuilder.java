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
    private int startRow = 0, startColumn = 0;
    private int endRow = 0, endColumn = 0;
    private MazeGenAlgorithm genAlgorithm;
    private boolean useLongestPath = false;
    private boolean useRandomStart = false;
    private boolean useRandomEnd = false;
    private boolean showDistances = false;
    

    private Grid grid; //Temporary.

    public MazeBuilder(){
        dijk = new Dijkstra();
        genAlgorithm = new Sidewinder();
    }

    public MazeBuilder withSize(int rowCount, int colCount){
        this.rowCount = rowCount;
        this.colCount = colCount;

        //todo hmmm how to implement this in builder... can't do this because other method can be called prior
        //this.endRow = rowCount-1;
        //this.endColumn = colCount-1;
        return this;
    }

    public MazeBuilder usingAlgorithm(MazeGenAlgorithm genAlgorithm){
        this.genAlgorithm = genAlgorithm;
        return this;
    }

    public MazeBuilder startFrom(int row, int column){
        this.startRow = row;
        this.startColumn = column;
        return this;
    }

    public MazeBuilder endAt(int row, int column){
        this.endRow = row;
        this.endColumn = column;
        return this;
    }

    public MazeBuilder usingLongestPath(){
        this.useLongestPath = true;
        return this;
    }

    public MazeBuilder usingRandomStart(){
        this.useRandomStart = true;
        return this;
    }

    public MazeBuilder usingRandomEnd(){
        this.useRandomEnd = true;
        return this;
    }

    public MazeBuilder showDistances(){
        this.showDistances = true;
        return this;
    }

    public Maze build(){
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

    private Entry<Cell, Cell> getEntrances(Grid grid){
        Cell startCell, endCell;
        if(this.useLongestPath){
            endCell = this.dijk.getDistances(grid, grid.getRandomCell()).getMax().getKey();
            startCell = this.dijk.getDistances(grid, endCell).getMax().getKey();

        }else{
            startCell = this.useRandomStart
                ?   grid.getRandomCell()
                :   grid.getCell(this.startRow, this.startColumn).get(); //Todo need to check if valid

            endCell = this.useRandomEnd
                ?   grid.getRandomCell()
                :   grid.getCell(this.endRow, this.endColumn).get(); //Todo need to check if valid

        }
        return new AbstractMap.SimpleEntry<Cell,Cell>(startCell, endCell);
    }
}
