package com.amw.sms.grid;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Distances is used to manage the distances between a specific root cell
 * and all other cells on a grid.
 */
public class Distances {
    private final Map<Cell, Integer> distanceMap;
    private final Cell rootCell;

    /**
     * Constructs a new distances instance using the provided cell as the root.
     * @param rootCell The root cell. Distances between this cell and all other cells
     * within the grid will be put inside this instance.
     */
    public Distances(Cell rootCell){
        this.rootCell = rootCell;
        this.distanceMap = new HashMap<>();
        this.distanceMap.put(rootCell, 0);
    }

    //TODO refactor to GridContent interface (or something similar to handle displaying cell contents)

    public Distances(List<Cell> path, Optional<Distances> knownDistances){
        //TODO - do we care if path is empty? .. probably right? This means we need to take an optional....
        //Or what if path doesn't start with root cell?
        if(path.isEmpty()){
            throw new RuntimeException(); //TODO careless exception handling
        }

        this.rootCell = path.get(0);
        this.distanceMap = new HashMap<>();
        this.distanceMap.put(rootCell, 0);

        //Use all known distances (if any) for path cells
        path.stream()
            .forEach((var cell) -> {
                final var distance = knownDistances.isPresent()
                    ? knownDistances.get().getDistance(cell)
                    : 1;  
                this.distanceMap.put(cell, distance);
            });
    }

    //TODO refactor to GridContent interface (or something similar to handle displaying cell contents)
    //Or update Distances with new "displayPath" or "usePath" method (would require multiple maps maintained in this instance)
    /**
     * Create new distances based on existing distances instance
     * @deprecated
     * @param rootCell
     * @param endCell
     * @param distances
     */
    public Distances(Cell rootCell, Cell endCell, Distances distances){
        this(rootCell);

        final var path = distances.pathToGoal(endCell);
        path.stream()
            .forEach((var cell) -> {
                this.distanceMap.put(cell, distances.getDistance(cell));
            });
    }

    /**
     * Returns the distance between the provided cell and the root cell. If there is no distance
     * defined between these cells, which indicates that these two cells do not yet have known path between them,
     * -1 is returned. 
     * @param cell Cell to get distance from.
     * @return The distance between the provided cell and the root cell. -1 if no such distance has been defined yet.
     */
    public int getDistance(Cell cell){
        final var distance = Optional.ofNullable(this.distanceMap.get(cell));
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
     * Returns the root cell used by this Distances instance. All distances set 
     * within this instance are from this cell.
     * @return Root cell.
     */
    public Cell getRootCell(){
        return this.rootCell;
    }

    /**
     * Returns all of the cells that have been indexed within this distances index.
     * @return Set of all of the cells that have a known distance from the root cell.
     */
    public Set<Cell> getCells(){
        return this.distanceMap.keySet();
    }

    /**
     * Returns a path from the root node to the provided cell.
     * @param cell Cell to get the path to.
     * @return Ordered list of cells representing the path between the root node and the provided cell.
     * Contains both the root node as the first element and the provided cell as the last element, given that
     * a path exists. If not path between the two cells exists, an empty list is returned. 
     * There is no guarantee that this path is the shortest. 
     */
    public List<Cell> pathToGoal(final Cell cell){
        final var path = new LinkedList<Cell>();
        path.add(cell);

        var currentCell = cell;
        while(currentCell != this.rootCell){
            final var cellDistance = this.getDistance(currentCell);
            final var nextCell = currentCell.getLinks()
                .stream()
                .filter((var neighbor) -> this.getDistance(neighbor) < cellDistance)
                .findFirst();

            //NO PATH FROM ROOT TO CELL
            if(nextCell.isEmpty()){
                return new LinkedList<Cell>();
            }

            path.push(currentCell = nextCell.get());
        }

        return path;
    }

    //TODO - move from Map specific Entry to some other generic Pair class
    public Entry<Cell, Integer> getMax(){
        return this.distanceMap.entrySet()
            .stream()
            .max((var entry1, var entry2) -> entry1.getValue() - entry2.getValue())
            .get();
    }
}
