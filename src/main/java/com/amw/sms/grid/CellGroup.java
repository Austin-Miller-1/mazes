package com.amw.sms.grid;

import java.util.Collection;

/**
 * Data object representing a group of cells. Each cell group may represent different things 
 * depending on the client, and should be given a name that sufficiently explains its purpose.
 */
public class CellGroup {
    private final String name;
    final Collection<Cell> cells; //Collection of cells provided by client. Unchanged.

    /**
     * Constructs new cell-group with the provided name.
     * @param collectionName Name of the cell group.
     * @param cells Collection of the cells within the group.
     */
    public CellGroup(String collectionName, Collection<Cell> cells){
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
    public Collection<Cell> getCells(){
        return this.cells;
    }
}
