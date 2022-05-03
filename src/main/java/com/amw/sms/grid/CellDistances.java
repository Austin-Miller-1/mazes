package com.amw.sms.grid;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.amw.sms.util.Pair;

/**
 * Contains the distances between the cells on the grid and a specified root-cell. 
 * Note that these distances do not have to be the same as the number of steps in a path from a cell to the root.
 * A step between any two cells may have different weights (e.g. Cell A and B may be connected but could have a distance of 10).
 * Distances may not be set for specific cells, such as those that do not have a path connecting them to the root cell.
 */
public class CellDistances extends GridData {
    private final Cell rootCell;
    private final Map<Cell, Integer> distances;

    //Cache to prevent repreated calls to algorithm
    private Pair<Cell, Integer> cachedMaxEntry;
    private boolean invalidCache = true;

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
        this.cachedMaxEntry = new Pair<Cell,Integer>(rootCell, 0);
    }

    /**
     * Set distances for specified cell.
     * @param cell Cell in the grid.
     * @param distance Distance between the provided cell and the set root-cell.
     */
    public void setDistance(Cell cell, int distance){
        this.invalidCache = true;
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
     * Get the entry that has the maximum distance. This entry will contain both the cell furthest away from the
     * root along with its distance.
     * @return Pair of the cell furthest from the root and its distance from the root.
     */
    public Pair<Cell, Integer> getMaxEntry(){
        //Refresh cache if invalid
        if(this.invalidCache){
            final var furthestEntry = this.distances.entrySet()
                .stream()
                .max((var entry1, var entry2) -> entry1.getValue() - entry2.getValue())
                .get();
            this.cachedMaxEntry = new Pair<Cell, Integer>(furthestEntry.getKey(), furthestEntry.getValue());
            this.invalidCache = false;
        }

        return this.cachedMaxEntry;
    }

    /**
     * Returns the cell with the largest distance from the root cell. 
     * This does not consider cells that are not connected to the root.
     * @return Cell furthest away from the root cell.
     */
    public Cell getFurthestCell(){
        return this.getMaxEntry().getFirst();
    }

    /**
     * Returns the max distance from the root cell. This will be the distance of the cell furthest from the root.
     * @return The max distance that has been set.
     */
    public int getMaxDistance(){
        return this.getMaxEntry().getSecond();
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
    @Deprecated
    public String getCellContents(Cell cell){
        return this.isDistanceSet(cell)
            ?   Integer.toString(this.distances.get(cell), 32).toUpperCase()
            :   super.getCellContents(cell);
    }

    /**
     * {@inheritDoc}
     * Color will be determined by the distance of the cell from the root cell. As cells get further away from root,
     *  the color will become darker and darker. If distances is unset, the cell's color will be determined by the 
     * {@link GridData} superclass.
     */
    @Override
    @Deprecated
    public Color getCellColor(Cell cell){
        //Unset => go to superclass
        if(!this.isDistanceSet(cell)){
            return super.getCellColor(cell);
        }

        //Root-cell case
        if(cell.equals(this.rootCell)){
            return Color.WHITE; 
        }

        //Color intensity will be fractional based on the max distance. This will
        //allow for the colors to transition nicely across the cells.
        final var distance = this.getDistance(cell);
        final var maxDistance = this.getMaxDistance();
        final var intensity = (float) (maxDistance-distance) / maxDistance;
        final var darkValue = Math.round(255*intensity);            //Secondary colors of the cell (creates a shade of primary)
        final var brightValue = 128 + Math.round(127*intensity);    //Primary color of the cell

        return new Color(darkValue, brightValue, darkValue);
    }
}
