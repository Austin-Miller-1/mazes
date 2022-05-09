package com.amw.sms.grid;

import java.util.List;

/**
 * Data object representing a list of cells. Each cell list may represent different things 
 * depending on the client, and should be given a name that sufficiently explains its purpose.
 */
public class CellList {
    private final String name;
    final List<Cell> cells; //List of cells provided by client

    /**
     * Constructs new cell-list with the provided name.
     * @param collectionName Name of the cell group.
     * @param cells Collection of the cells within the group.
     */
    public CellList(String collectionName, List<Cell> cells){
        this.name = collectionName;
        this.cells = cells;
    }

    /**
     * Returns the name of the cell group.
     * @return Name given to the cell group.
     */
    public String getName(){
        return this.name;
    }

    /**
     * Returns the group's cells as a collection.
     * @return The group's cells as a collection.
     */
    public List<Cell> getCells(){
        return this.cells;
    }
}
