package com.amw.sms.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

/**
 * Grid class. Used to represent mazes as a group of cells connected to eachother.
 * Provides methods for traversing and manipulating the cells and getting different
 * representations of the grid.
 */
public class Grid {
    private final int rowCount, colCount;
    private final List<List<Cell>> grid;
    private final Random rng;

    private Optional<GridData> gridData;
    private boolean gridDataShown = true;

    private Optional<List<Cell>> path;
    private boolean limitShownDataToPath = false;

    /**
     * Constructs a grid with the provided number of rows and columns.
     * Initializes grid with all unlinked cells with the appropriate neighbors set. 
     * @param rowCount Number of rows in the grid.
     * @param colCount Number of columns in the grid.
     */
    public Grid(int rowCount, int colCount){
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.grid = this.createInitialGrid();
        this.configureCells();
        rng = new Random();

        this.gridData = Optional.empty();
        this.path = Optional.empty();
    }

    /**
     * Creates the initial grid. Neighbors are not set within this method.
     * @return List of List of Cells representing the grid. This is the list of 
     * rows, which in turn are lists of cells.
     */
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

    /**
     * Configures the cells in the initial grid to have the correct neighbors set.
     */
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

    /**
     * Returns the cell at the provided row and column. It is possible that
     * such a cell doesn't exist, in which case an empty Optional will be returned.
     * @param row Row that contains the cell
     * @param column Column that contains the cell
     * @return Optional containing the cell at the provided position if it exists. Returns
     * an empty Optional if no such cell exists. This can happen if the provided position is
     * outside of the grid.
     */
    public Optional<Cell> getCell(int row, int column){
        if(row < 0 || row >= this.rowCount)         return Optional.empty();
        if(column < 0 || column >= this.colCount)   return Optional.empty();
        return Optional.of(this.grid.get(row).get(column));
    }

    /**
     * Returns a random cell from the grid.
     * @return Random cell from the grid.
     */
    public Cell getRandomCell(){
        return this.grid
            .get(rng.nextInt(this.rowCount))
            .get(rng.nextInt(this.colCount));
    }

    public Cell getFirstCell(){
        return this.grid.get(0).get(0);
    }

    public Cell getLastCell(){
        return this.grid.get(this.rowCount-1).get(this.colCount-1);
    }

    /**
     * Returns the total number of cells within the grid.
     * @return The number of cells in the grid.
     */
    public int getCellCount(){
        return this.rowCount * this.colCount;
    }

    /**
     * Returns the grid's rows.
     * @return List of rows. Each row is itself a list of cells.
     */
    public List<List<Cell>> getRows(){
        return this.grid;
    }

    /**
     * Returns the grid's columns.
     * @return List of columns. Each column is itself a list of cells.
     */
    public List<List<Cell>> getColumns(){
        //Since the grid's implementation is row-based, returning the columns requires
        //recreating the grid using columns.
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

    /**
     * Returns a list of all of the grid's cells. No order is guaranteed.
     * @return List of the grid's cells.
     */
    public List<Cell> getCells(){
        return this
            .getRows()
            .stream()
            .flatMap(List::stream)
            .toList();
    }

    /**
     * Returns the number of rows in the grid.
     * @return The number of rows in the grid.
     */
    public int getRowCount(){
        return this.rowCount;
    }

    /**
     * Returns the number of columns in the grid.
     * @return The number of columns in the grid.
     */
    public int getColumnCount(){
        return this.colCount;
    }

    /**
     * Sets the grid's data.
     * @param gridData Data to associate with this grid and its cells.
     */
    public void setGridData(GridData gridData) {
        this.gridData = Optional.of(gridData);
    }

    /**
     * Clears the grid's data.
     */
    public void clearGridData(){
        this.gridData = Optional.empty();
    }

    /**
     * Get's the grid's data.
     * @return Optional containing the data associated with the grid, if any. Returns an 
     * empty optional if the grid data was never set or if it was cleared. 
     */
    public Optional<GridData> getGridData() {
        return this.gridData;
    }

     /**
     * Configures grid so that the associated grid-data (if any) is displayed in 
     * its visual representations.
     */
    public void showGridData(){
        this.gridDataShown = true;
    }

    /**
     * Configures grid so that the grid-data (if any) is hidden in its visual representations.
     */
    public void hideGridData(){
        this.gridDataShown = false;
    }

    /**
     * Sets the specific path of cells to display in the grid's visual representations. 
     * @param path
     */
    public void setPath(List<Cell> path){
        this.path = Optional.of(path);
    }

    /**
     * Returns the path of cells set in the grid to be displayed in the its visual representations. 
     * @return Optional containing the list of cells representing set as the path. Returns empty optional if 
     * no path is currently set.
     */
    public Optional<List<Cell>> getPath(){
        return this.path;
    }

    /**
     * Clears the path of cells to be displayed in the grid's visual representations.
     */
    public void clearPath(){
        this.path = Optional.empty();
    }

    /**
     * Restricts the data displayed within the grid's visual representations to only the cell's 
     * on the set path, if one is set. If no path is set, no data will be displayed.
     */
    public void displayPathExclusively(){
        this.limitShownDataToPath = true;
    }

    public void displayAllCells(){

    }

    /**
     * Returns boolean whether the set path contains the provided cell
     * @param cell Cell to check for.
     * @return True if the set path contains the cell. False if no path is set or the set path
     * does not contain the cell.
     */
    private boolean pathContainsCell(Cell cell){
        return this.path.isPresent() && this.path.get().contains(cell);
    }

    /**
     * Returns boolean whether the provided cell's data should be displayed in the 
     * grid's visual representations.
     * @param cell Cell to check for.
     * @return True if the cell's data should be displayed. False otherwise. Determined by grid
     * configuration.
     * @see Grid#showGridData
     * @see Grid#hideGridData
     * @see Grid#displayPathExclusively
     * @see Grid#displayAllCells
     */
    private boolean shouldDisplayCellData(Cell cell){
        return this.gridDataShown &&
            (!this.limitShownDataToPath || this.pathContainsCell(cell));
    }

    /**
     * Returns string representation of a cell's associated data. Used when displaying the grids cells in its
     * different representations. 
     * Default implementation returns a whitespace character.
     * @param cell Cell to get the contents of
     * @return The contents of the cell as a string.
     */
    public String getCellDataDisplayString(Cell cell){
        return this.gridData.isPresent() && this.shouldDisplayCellData(cell)
            ? this.gridData.get().getCellContents(cell)
            : " ";
    }
    
    /**
     * Returns string representation of the grid.
     * @return String representation of the grid.
     */
    public String toString(){
        //Top of the grid. The rest of the grid will be done row-by-row.
        final var output = "+" + "---+".repeat(this.colCount) + "\n";

        return this.getRows()
                .stream()
                .map(this::rowToString)
                .map((var line) -> line + "\n")
                .reduce(output, String::concat);
    }

    /**
     * TODO - move all string logic to separate class, then bring it in here
     * Returns the string representation of a given row of cells. This excludes the top of the row,
     * since each row's bottom will act as the top of the following row. 
     * @param row Row of cells.
     * @return String representation of the row, excluding the top of the row.
     */
    private String rowToString(List<Cell> row){
        final var MIDDLE_START = "|";
        final var MIDDLE_BODY = " %s ";
        final var BOTTOM_START = "+";

        //Each row will be 3 lines - top, middle and bottom. Since the bottom
        //of each row is the top of the following row, only the middle and bottom
        //of each row needs to be created. 

        //Create the string rep of a cell's middle section.
        final Function<Cell, String> fromCellToMiddleStr = (var cell) -> {
            final var eastChars = cell.getEast().isPresent() 
                && cell.getEast().get().isLinkedTo(cell)
                    ?   " "
                    :   "|";
            return MIDDLE_BODY.formatted(this.getCellDataDisplayString(cell)) + eastChars;
        };

        //Create the string rep of a cell's bottom section
        final Function<Cell, String> fromCellToBottomStr = (var cell) -> {
            final var southChars = cell.getSouth().isPresent() 
                && cell.getSouth().get().isLinkedTo(cell)
                    ?   "   "
                    :   "---";


            //Corner character logic
            final var eastCell = cell.getEast();
            final var southCell = cell.getSouth();
            final Optional<Cell> southEastCell = cell.getEast().isPresent()
                ?   cell.getEast().get().getSouth()
                :   Optional.empty();
            
            //Check walls surrounding corner
            var horizontalWalls = 0;
            var verticalWalls = 0;
            if(!eastCell.isPresent() || !cell.isLinkedTo(eastCell.get())) verticalWalls++;
            if(!southCell.isPresent() || !cell.isLinkedTo(southCell.get())) horizontalWalls++;
            if(eastCell.isPresent() && southEastCell.isPresent() && !eastCell.get().isLinkedTo(southEastCell.get())) horizontalWalls++;
            if(southCell.isPresent() && southEastCell.isPresent() && !southCell.get().isLinkedTo(southEastCell.get())) verticalWalls++;

            //Choose corner character based on what walls exist
            String cornerChar = "?";
            if(horizontalWalls + verticalWalls == 4)                cornerChar = "+";
            else if(horizontalWalls == 2)                           cornerChar = "-";
            else if(verticalWalls == 2)                             cornerChar = "|";
            else if(horizontalWalls == 1 && verticalWalls == 1)     cornerChar = "+";   //This can become mega complex if a better character is wanted
            else if(horizontalWalls > 0)                            cornerChar = "-";
            else if(verticalWalls > 0)                              cornerChar = "|";
             
            return  southChars + cornerChar;
        };

        final var middleLine = MIDDLE_START + row.stream()
            .map(fromCellToMiddleStr)
            .reduce(String::concat)
            .get();

        final var bottomLine = BOTTOM_START + row.stream()
            .map(fromCellToBottomStr)
            .reduce(String::concat)
            .get();

        return middleLine + "\n" + bottomLine;
    }

    /**
     * Returns string representation of a cell's contents. Used when displaying the grids cells in its
     * different representations. 
     * Default implementation returns a whitespace character.
     * @param cell Cell to get the contents of
     * @return The contents of the cell as a string.
     */
    @Deprecated
    public String contentsOf(Cell cell){
        return " ";
    }

    /**
     * Returns an image of the grid. Cell size will be 10 pixels.
     * @param title Title to be used by the image.
     * @return Image of the grid.
     */
    public ImagePlus toImage(String title){
        return this.toImage(title, 10);
    }

    /**
     * Returns an image of the grid.
     * @param title Title to be used by the image.
     * @param cellSize The number of pixels each cell will take up.
     * @return Image of the grid.
     */
    public ImagePlus toImage(String title, int cellSize){
        //Number of pixels of whitespace to place on each side of the grid
        //This is to show the full grid, including it's boundaries, in the image. 
        final var OFFSET = 30;  
        final var BACKGROUND_COLOR = 255;   //white
        final var WALL_COLOR = 0;           //black
        final var LINE_WIDTH = 3;

        final var imageWidth = (cellSize * this.colCount) + 2*OFFSET;
        final var imageHeight = (cellSize * this.rowCount) + 2*OFFSET;
        ImageProcessor ip = new ByteProcessor(imageWidth, imageHeight);

        //Background
        ip.setValue(BACKGROUND_COLOR);
        ip.fill();

        //Draw maze
        ip.setValue(WALL_COLOR);
        ip.setLineWidth(LINE_WIDTH);
        this.getCells()
            .stream()
            .forEach((var cell) -> {
                //Cell coords
                final var x1 = (cell.getColumnPosition() * cellSize) + OFFSET;
                final var y1 = (cell.getRowPosition() * cellSize) + OFFSET;
                final var x2 = ((cell.getColumnPosition()+1) * cellSize) + OFFSET;
                final var y2 = ((cell.getRowPosition()+1) * cellSize + OFFSET);

                //Northern & Western walls only if there are no neighbors
                if(cell.getNorth().isEmpty())   ip.drawLine(x1, y1, x2, y1);
                if(cell.getWest().isEmpty())    ip.drawLine(x1, y1, x1, y2);
                
                //Eastern wall if no neighbor or unlinked to cell
                if(cell.getEast().isEmpty() || !cell.getEast().get().isLinkedTo(cell)){
                    ip.drawLine(x2, y1, x2, y2);
                }

                //Southern wall if no neighbor or unlinked to cell
                if(cell.getSouth().isEmpty() || !cell.getSouth().get().isLinkedTo(cell)){
                    ip.drawLine(x1, y2, x2, y2);    
                }                
            });

        return new ImagePlus(title, ip);
    }

    /**
     * IDEAS, transposition & rotation
     */
    /* private Grid transpose(Grid grid){
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

    public void setCell(Cell cell, int row, int column){
        this.grid.get(row).set(column, cell);
    } */
}
