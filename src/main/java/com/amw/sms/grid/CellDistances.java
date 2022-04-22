package com.amw.sms.grid;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Contains the distances between the cells on the grid and a specified root-cell. 
 * Note that these distances do not have to be the same as the number of steps in a path from a cell to the root.
 * A step between any two cells may have different weights (e.g. Cell A and B may be connected but could have a distance of 10).
 * Distances may not be set for specific cells, such as those that do not have a path connecting them to the root cell.
 */
public class CellDistances extends GridData {
    private final Cell rootCell;
    private final Map<Cell, Integer> distances;

    /**
     * Constructs CellDistances instance for specified grid and root-cell.
     * @param grid Grid for which this data is for.
     * @param rootCell Root cell that these distances are all relative to.
     */
    public CellDistances(Grid grid, Cell rootCell){
        super(grid);
        this.rootCell = rootCell;
        this.distances = new HashMap<Cell, Integer>();
        this.distances.put(rootCell, 0);
    }

    /**
     * Set distances for specified cell.
     * @param cell Cell in the grid.
     * @param distance Distance between the provided cell and the set root-cell.
     */
    public void setDistance(Cell cell, int distance){
        this.distances.put(cell, distance);
    }

    /**
     * Get distance set for specified cell.
     * @param cell Cell in the grid.
     * @return Distance between the specified cell and the root cell. If unset, returns -1.
     * An unset cell means that the cell has no path connecting it to the root cell. 
     * Use {@link CellDistances#isDistanceSet} when checking to see if a cell is unset or not.
     */
    public int getDistance(Cell cell){
        return this.isDistanceSet(cell)
            ?   this.distances.get(cell)
            :   -1;
    }

    /**
     * Returns true/false whether the cell's distance from the root cell is set or not.
     * @param cell Cell in the grid.
     * @return True if a distance has been set for the provided cell. False otherwise. 
     * An unset cell means that the cell has no path connecting it to the root cell.
     */
    public boolean isDistanceSet(Cell cell){
        return this.distances.containsKey(cell);
    }

    /**
     * Get all Cells whose distances from the root are recorded.
     * @return Cells whose distances are set.
     */
    public Set<Cell> getCells(){
        return this.distances.keySet();
    }

    /**
     * Returns the cell with the largest distance from the root cell. 
     * This does not consider cells that are not connected to the root.
     * @return Cell furthest away from the root cell.
     */
    public Cell getFurthestCell(){
        return this.distances.entrySet()
            .stream()
            .max((var entry1, var entry2) -> entry1.getValue() - entry2.getValue())
            .get()
            .getKey();
    }

    /**
     * Get the root cell that these distances all correspond to.
     * @return The root cell.
     */
    public Cell getRootCell() {
        return this.rootCell;
    }

    /**
     * {@inheritDoc}
     * If distance is unset, the cell's contents will be determined by the 
     * {@link GridData} superclass.
     */
    @Override
    public String getCellContents(Cell cell){
        return this.isDistanceSet(cell)
            ?   Integer.toString(this.distances.get(cell), 32).toUpperCase()
            :   super.getCellContents(cell);
    }
}
