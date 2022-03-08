package com.amw.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import ij.ImagePlus;
import ij.gui.Line;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

public class Grid {
    private final int rowCount, colCount;
    private final List<List<Cell>> grid;

    public Grid(int rowCount, int colCount){
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.grid = this.createInitialGrid();
        this.configureCells();
    }

    private List<List<Cell>> createInitialGrid(){
        final var grid = new ArrayList<List<Cell>>();

        //Create initial rows, add each to grid
        for(var rowIndex = 0; rowIndex < this.rowCount; rowIndex++){
            final var row = new ArrayList<Cell>();

            //Initialize each row with cells
            for(var colIndex = 0; colIndex < this.colCount; colIndex++){
                row.add(new Cell(rowIndex, colIndex));
            }

            grid.add(row);
        }
        
        return grid;
    }

    private void configureCells(){
        this.getCells().forEach((final var currentCell) -> {
            final var rowIndex = currentCell.getRowPosition();
            final var colIndex = currentCell.getColumnPosition();

            currentCell.setNorth(this.getCell(rowIndex-1, colIndex));                
            currentCell.setSouth(this.getCell(rowIndex+1, colIndex));
            currentCell.setWest(this.getCell(rowIndex, colIndex-1));
            currentCell.setEast(this.getCell(rowIndex, colIndex+1));
        });
    }

    public Optional<Cell> getCell(int row, int column){
        if(row < 0 || row >= this.rowCount)         return Optional.empty();
        if(column < 0 || column >= this.colCount)   return Optional.empty();
        return Optional.of(this.grid.get(row).get(column));
    }

    public void setCell(Cell cell, int row, int column){
        this.grid.get(row).set(column, cell);
    }

    public Cell getRandomCell(){
        final var rng = new Random();
        return this.grid
            .get(rng.nextInt(this.rowCount))
            .get(rng.nextInt(this.colCount));
    }

    public int getCellCount(){
        return this.rowCount * this.colCount;
    }

    public List<List<Cell>> getRows(){
        return this.grid;
    }

    public List<List<Cell>> getColumns(){
        final var columns = new ArrayList<List<Cell>>();

        for(var colIndex = 0; colIndex < this.colCount; colIndex++){
            final var column = new ArrayList<Cell>();
            columns.add(column);
            
            for(var rowIndex = 0; rowIndex < this.rowCount; rowIndex++){
                column.add(this.getCell(rowIndex, colIndex).get());
            }    
        }

        return columns;
     }

    public List<Cell> getCells(){
        return this
            .getRows()
            .stream()
            .flatMap(List::stream)
            .toList();
    }

    public int getRowCount(){
        return this.rowCount;
    }

    public int getColumnCount(){
        return this.colCount;
    }
    
    public String toString(){
        final var output = "+" + "---+".repeat(this.colCount) + "\n";

        return this.getRows()
                .stream()
                .map(this::rowToString)
                .map((var line) -> line + "\n")
                .reduce(output, String::concat);
    }

    private String rowToString(List<Cell> row){
        final var LINE_ONE_START = "|";
        final var LINE_ONE_BODY = "   ";
        final var LINE_TWO_START = "+";
        final var LINE_TWO_CORNER = "+";

        final Function<Cell, String> fromCellToLineOneText = (var cell) -> {
            final var eastBound = cell.getEast().isPresent() 
                && cell.getEast().get().isLinkedTo(cell)
                    ?   " "
                    :   "|";
            return LINE_ONE_BODY + eastBound;
        };

        final Function<Cell, String> fromCellToLineTwoText = (var cell) -> {
            final var southBound = cell.getSouth().isPresent() 
                && cell.getSouth().get().isLinkedTo(cell)
                    ?   "   "
                    :   "---";
            return  southBound + LINE_TWO_CORNER;
        };

        final var lineOne = LINE_ONE_START + row.stream()
            .map(fromCellToLineOneText)
            .reduce(String::concat)
            .get();

        final var lineTwo = LINE_TWO_START + row.stream()
            .map(fromCellToLineTwoText)
            .reduce(String::concat)
            .get();

        return lineOne + "\n" + lineTwo;
    }

    public ImagePlus toImage(String title){
        return this.toImage(title, 10);
    }

    public ImagePlus toImage(String title, int cellSize){
        final var OFFSET = 30;
        final var imageWidth = (cellSize * this.colCount) + 2*OFFSET;
        final var imageHeight = (cellSize * this.rowCount) + 2*OFFSET;
        ImageProcessor ip = new ByteProcessor(imageWidth, imageHeight);

        //Background
        ip.setValue(255);
        ip.fill();

        //Draw maze
        ip.setValue(0);
        this.getCells()
            .stream()
            .forEach((var cell) -> {
                final var x1 = (cell.getColumnPosition() * cellSize) + OFFSET;
                final var y1 = (cell.getRowPosition() * cellSize) + OFFSET;
                final var x2 = ((cell.getColumnPosition()+1) * cellSize) + OFFSET;
                final var y2 = ((cell.getRowPosition()+1) * cellSize + OFFSET);

                //Northern & Western walls only if there are no neighbors
                if(cell.getNorth().isEmpty())   ip.draw(new Line(x1, y1, x2, y1));
                if(cell.getWest().isEmpty())    ip.draw(new Line(x1, y1, x1, y2));
                
                //Eastern wall if no neighbor or unlinked to cell
                if(cell.getEast().isEmpty() || !cell.getEast().get().isLinkedTo(cell)){
                    ip.draw(new Line(x2, y1, x2, y2));
                }

                //Southern wall if no neighbor or unlinked to cell
                if(cell.getSouth().isEmpty() || !cell.getSouth().get().isLinkedTo(cell)){
                    ip.draw(new Line(x1, y2, x2, y2));    
                }                
            });

        return new ImagePlus(title, ip);
    }

    /**
     * IDEAS, transposition & rotation
     */
    private Grid transpose(Grid grid){
        //Would need to also update all of the neighbors appropriately.... That's why it doesn't work as is
        final var transposedGrid = new Grid(grid.getColumnCount(), grid.getRowCount());

        for(var x = 0; x < grid.getRowCount(); x++){
            for(var y = 0; y < grid.getColumnCount(); y++){
                transposedGrid.setCell(grid.getCell(x, y).get(), y, x);
            }
        }
        

        //Maybe like this?
        this.getCells().forEach((final var currentCell) -> {
            final var rowIndex = currentCell.getRowPosition();
            final var colIndex = currentCell.getColumnPosition();

            currentCell.setNorth(this.getCell(rowIndex-1, colIndex));                
            currentCell.setSouth(this.getCell(rowIndex+1, colIndex));
            currentCell.setWest(this.getCell(rowIndex, colIndex-1));
            currentCell.setEast(this.getCell(rowIndex, colIndex+1));
        });

        return transposedGrid;
    }
}
