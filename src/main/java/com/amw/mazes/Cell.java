package com.amw.mazes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Cell {
    private final int rowPos, colPos;
    private final Set<Cell> links;
    private Optional<Cell> north, east, south, west;

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
     * Link the provided cell to this one. Updates both Cells such that they are linked bidirectionally.
     * @param cell Cell to link
     */
    public void link(Cell cell){
        this.link(cell, true);
    }

    /**
     * Link the provided cell to this one. Can be used to update only one of the two Cells 
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
     * Unlink the provided cell to this one. Removes links from both Cells. 
     * @param cell Cell to unlink.
     */
    public void unlink(Cell cell){
        this.unlink(cell, true);
    }

    /**
     * Unlink the provided cell to this one. Can be used to update only one of the two Cells 
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
     * Get Collection of all of the Cells that this Cell is linked to.
     * @return Collection of linked Cells.
     */
    public Collection<Cell> getLinks(){
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

    void setNorth(Optional<Cell> cell){
        this.north = cell;
    }

    void setEast(Optional<Cell> cell){
        this.east = cell;
    }

    void setSouth(Optional<Cell> cell){
        this.south = cell;
    }

    void setWest(Optional<Cell> cell){
        this.west = cell;
    }

    Optional<Cell> getNorth(){
        return this.north;
    }

    Optional<Cell> getEast(){
        return this.east;
    }

    Optional<Cell> getSouth(){
        return this.south;
    }

    Optional<Cell> getWest(){
        return this.west;
    }

    public int getColumnPosition() {
        return colPos;
    }

    public int getRowPosition() {
        return rowPos;
    }
}
