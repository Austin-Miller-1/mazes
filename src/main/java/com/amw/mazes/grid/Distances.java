package com.amw.mazes.grid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Distances is used to manage the distances between a specific root cell
 * and all other cells on a grid.
 */
public class Distances {
    private final Map<Cell, Integer> distanceMap;

    /**
     * Constructs a new distances instance using the provided cell as the root.
     * @param rootCell The root cell. Distances between this cell and all other cells
     * within the grid will be put inside this instance.
     */
    public Distances(Cell rootCell){
        distanceMap = new HashMap<>();
        distanceMap.put(rootCell, 0);
    }

    /**
     * Returns the distance between the provided cell and the root cell. If there is no distance
     * defined between these cells, which indicates that these two cells do not yet have known path between them,
     * -1 is returned. 
     * @param cell Cell to get distance from.
     * @return The distance between the provided cell and the root cell. -1 if no such distance has been defined yet.
     */
    public int getDistance(Cell cell){
        final var distance = Optional.ofNullable(distanceMap.get(cell));
        return distance.isPresent()
            ? distance.get()
            : -1;
    }

    /**
     * Adds the distance between the provided cell and the root cell to this distances instance.
     * @param cell Cell whose distance from the root cell is being added
     * @param distance Distance between provided cell and root cell to be set.
     */
    public void addCell(Cell cell, int distance){
        distanceMap.put(cell, distance);
    }

    /**
     * Returns all of the cells that have been indexed within this distances index.
     * @return Set of all of the cells that have a known distance from the root cell.
     */
    public Set<Cell> getCells(){
        return distanceMap.keySet();
    }
}
