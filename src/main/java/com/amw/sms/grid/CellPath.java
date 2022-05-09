package com.amw.sms.grid;

import java.util.List;

/**
 * Data object representing a path of cells. Each cell path may represent different things 
 * depending on the client, and should be given a name that sufficiently explains its purpose.
 * 
 * Although effectively the same as a CellList, it's context is different in that a CellPath is 
 * a list of cells where each cell within the list connects in some way to the cell following it.
 */
public class CellPath extends CellList {
    /**
     * Constructs new cell-path with the provided name.
     * @param collectionName Name of the cell path.
     * @param cells List of the cells representing the path. The expectation is that each cell
     * within the list connects in some way to the cell following it, thus making it a path of cells. 
     * Whether the cells are actually linked internally is not relevant. 
     */
    public CellPath(String name, List<Cell> path){
        super(name, path);
    }
}
