package com.amw.mazes.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Class representing cell within a Grid.
 * Each cell can be linked to any number of other cells. These links 
 * indicate that there is a path between the cells allowing for traversal from one
 * to the other. A cell also can have neighbors in any of the cardinal directions, but
 * these may not exist, e.g. when the cell is at the edge of the grid.
 */
public class Cell {
    private final int rowPos, colPos;
    private final Set<Cell> links;
    private Optional<Cell> north, east, south, west;

    /**
     * Constructs new cell with the provided position in the containing grid.
     * @param row The row at which this cell is located at in the grid.
     * @param column The column at which this cell is located at in the grid.
     */
    public Cell(int row, int column){
        this.rowPos = row;
        this.colPos = column;
        this.links = new HashSet<Cell>();

        //Neighbors
        north = Optional.empty();
        east = Optional.empty();
        south = Optional.empty();
        west = Optional.empty();
    }

    /**
     * Links the provided cell to this one. Updates both cells such that they are linked bidirectionally.
     * @param cell Cell to link
     */
    public void link(Cell cell){
        this.link(cell, true);
    }

    /**
     * Links the provided cell to this one. Can be used to update only one of the two Cells 
     * or to update both to link bidirectionally.
     * @param cell Cell to link
     * @param bidi Flag that determines whether cells are linked bidirectionally or not. If true,
     * both Cells will be updated with links internally to each other. If false, only this cell will
     * be updated with a link. 
     * Method is needed since both cells must be updated separately.
     */
    private void link(Cell cell, boolean bidi){
        this.links.add(cell);

        //Make sure provided cell's links are also updated
        if(bidi){
            cell.link(this, false);
        }
    }

    /**
     * Unlinks the provided cell to this one. Removes links from both Cells. 
     * @param cell Cell to unlink.
     */
    public void unlink(Cell cell){
        this.unlink(cell, true);
    }

    /**
     * Unlinks the provided cell to this one. Can be used to update only one of the two Cells 
     * or to update both.
     * @param cell Cell to link
     * @param bidi Flag that determines whether cells are unlinked bidirectionally or not. If true,
     * both Cells will be updated with links removed from both. If false, only this cell will
     * be unlinked. 
     * Method is needed since both cells must be updated separately.
     */
    private void unlink(Cell cell, boolean bidi){
        this.links.remove(cell);

        //Make sure provided cell's links are also updated
        if(bidi){
            cell.unlink(this, false);
        }
    }

    /**
     * Returns set of all of the cells that this cell is linked to.
     * @return Set of linked Cells.
     */
    public Set<Cell> getLinks(){
        return this.links;
    }

    /**
     * Indicates whether a cell is linked to this one.
     * @param cell Cell to check.
     * @return true if the cell is linked to this one; false otherwise.
     */
    public boolean isLinkedTo(Cell cell){
        return this.links.contains(cell);
    }

    /**
     * Get Collection of all of the neighboring cells.
     * @return Collection of neighboring cells. Includes cells North, East, South and West to the cell,
     * given that they exist. Non-existant cells are not included. For instance, if there is no Northern cell,
     * no value will represent this in the Iterator.
     */
    public Collection<Cell> getNeighbors(){
        final var neighbors = new ArrayList<Cell>();

        //TODO - can I make a generic forEach method that can be used to shorten this? Maybe good/cool idea would be to create DirectionalCollection with direction enum maybe
        north.ifPresent(neighbors::add);
        east.ifPresent(neighbors::add);
        south.ifPresent(neighbors::add);
        west.ifPresent(neighbors::add);

        return neighbors;
    }

    /**
     * Set's northern neighbor.
     * @param cell Northern neighboring cell. Such a cell may not 
     * exist, in which case, an empty Optional should be provided.
     */
    void setNorth(Optional<Cell> cell){
        this.north = cell;
    }

    /**
     * Set's eastern neighbor.
     * @param cell Eastern neighboring cell. Such a cell may not 
     * exist, in which case, an empty Optional should be provided.
     */
    void setEast(Optional<Cell> cell){
        this.east = cell;
    }

    /**
     * Set's southern neighbor.
     * @param cell Southern neighboring cell. Such a cell may not 
     * exist, in which case, an empty Optional should be provided.
     */
    void setSouth(Optional<Cell> cell){
        this.south = cell;
    }

    /**
     * Set's western neighbor.
     * @param cell Western neighboring cell. Such a cell may not 
     * exist, in which case, an empty Optional should be provided.
     */
    void setWest(Optional<Cell> cell){
        this.west = cell;
    }

    /**
     * Returns northern neighbor.
     * @return Optional containing the northern neighboring cell 
     * if one exists. If no such neighbor exists, an empty optional is returned.
     */
    public Optional<Cell> getNorth(){
        return this.north;
    }

    /**
     * Returns eastern neighbor.
     * @return Optional containing the eastern neighboring cell 
     * if one exists. If no such neighbor exists, an empty optional is returned.
     */
    public Optional<Cell> getEast(){
        return this.east;
    }

    /**
     * Returns southern neighbor.
     * @return Optional containing the southern neighboring cell 
     * if one exists. If no such neighbor exists, an empty optional is returned.
     */
    public Optional<Cell> getSouth(){
        return this.south;
    }

    /**
     * Returns western neighbor.
     * @return Optional containing the western neighboring cell 
     * if one exists. If no such neighbor exists, an empty optional is returned.
     */
    public Optional<Cell> getWest(){
        return this.west;
    }

    /**
     * Returns the column position of the cell.
     * @return The column number at which this cell is located at within the grid.
     */
    public int getColumnPosition() {
        return colPos;
    }

    /**
     * Returns the row position of the cell.
     * @return The row number at which this cell is located at within the grid.
     */
    public int getRowPosition() {
        return rowPos;
    }

    /**
     * TODO - does it make more sense to have this outside of the implementation of Cell?
     * Seems like questionable design adding it to Cell directly when it's to be used by the
     * maze solving algorithms.
     *  
     * Returns distances between this cell and every other cell on the grid.
     * @return Distances between this cell and every other cell on the grid.
     */
    public Distances getDistances(){
        final var distances = new Distances(this);
        final var frontier = new LinkedList<Cell>();    //Frontier will contain all remaining cells to check
        frontier.add(this);                             //Start frontier with root

        //Go through all frontier cells. 
        //1. Index distances between root and all of the frontier cell's links.
        //2. Add linked cells to frontier if they have not been indexed yet.
        while(!frontier.isEmpty()){
            var frontierCell = frontier.remove();

            frontierCell.getLinks()
                .stream()
                .forEach((var linkedCell) -> {
                    //Skip if already indexed (imperfect mazes)
                    if(distances.getDistance(linkedCell) >= 0) return;
                    
                    distances.addCell(linkedCell, distances.getDistance(frontierCell)+1);
                    frontier.add(linkedCell);
                });
        }

        return distances;
    }
}
