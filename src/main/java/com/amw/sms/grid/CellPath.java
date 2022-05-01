package com.amw.sms.grid;

import java.util.List;

/**
 * Data object representing a path of cells. Each cell path may represent different things 
 * depending on the client, and should be given a name that sufficiently explains its purpose.
 */
public class CellPath extends CellGroup {
    /**
     * Constructs new cell-path with the provided name.
     * @param collectionName Name of the cell path.
     * @param cells List of the cells representing the path. The expectation is that each cell
     * within the list connects (in some way) specifically to the cell following it within the list,
     * thus making it a path of cells. Whether the cells are actually linked internally is not
     * necessary. 
     */
    public CellPath(String name, List<Cell> path){
        super(name, path);
    }

    /**
     * Returns the path's cells as a list.
     * @return The path's cells as a list.
     */
    public List<Cell> getPath(){
        return (List<Cell>) this.cells;
    }
}
