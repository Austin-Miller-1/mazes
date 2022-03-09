package com.amw.mazes.grid;

/**
 * BitfieldGrid class. TODO
 * 
 * To be used with existing Grid algorithms, there would need to be a more generic 
 * interface defined such that the internal representation of the grid (i.e. a List
 * of List of Cells for Grid.java) does not need to be known.
 * Problematic methods:
 * - getCell -> Cell
 * - getRandomCell -> Cell
 * - getRows -> List<List<Cell>>
 * - getColumns -> List<List<Cell>>
 * - getCells -> List<Cell>
 * 
 * Ideas:
 * 
 * 1. Use a PseudoCell implementation of a Cell interface that wraps the integer representation of the cell. 
 * Instances of this class would be created at time of calling any method that returns cells. This could be
 * done by defining getCell to do this, then using this within all other methods.
 * Problems: undermines the purpose of using a multidimentional array of cells by relying on a large number of
 * object instances.
 * 
 * 2. This seems to be a problem that comes with defining our Grids to specifically return Lists and Cells, which
 * are both unabstracted parts of the Grid's implementation. For instance, we CANNOT have a array-based version of Grid since 
 * Lists would still need to be used within the methods, which defeats the purpose. Grid should be updated to abstract these details out.
 * The required changes for this seem to be to move away from returning data from these methods and instead offerring a lot of new methods
 * that'll replace the needs of these. So instead of Grid.getCell(1, 2).getNorth().isPresent(), there would have be a method that does 
 * all of that together.
 * Problem: THIS IS BAD DESIGN. Moving away from object-oriented to primitive-based and losing all of the benefits that OOP gives.  
 * 
 * 3. We implement each algorithm for each implementation type.
 * Problem: duplication of code, not relying on polymorphism via common interface. Not sure if there's a good alternative.
 * 
 */
public class BitfieldGrid {
    private final int rowCount, colCount;
    private final int[][] grid;

    /**
     * Constructs a grid with the provided number of rows and columns.
     * Initializes grid with all unlinked cells with the appropriate neighbors set. 
     * @param rowCount Number of rows in the grid.
     * @param colCount Number of columns in the grid.
     */
    public BitfieldGrid(int rowCount, int colCount){
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.grid = new int[rowCount][colCount];
    }
}
