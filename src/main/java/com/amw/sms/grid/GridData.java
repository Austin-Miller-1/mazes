package com.amw.sms.grid;

import java.awt.Color;

/**
 * Container of the grid's data. The expectations is that there this class contains data associated with the individual grid cells. 
 * It is not required that all cells have data. The data may be simple or complex and may differ depending on the algorithms that 
 * are used, and because of that, is contained separately from the Grid itself. 
 * GridData is not intended to be used on its own since it does not contain any data elements itself. As such, it is made abstract.
 */
public abstract class GridData {
    private final static String DEFAULT_CELL_CONTENTS_STRING = " ";
    private final static Color DEFAULT_CELL_COLOR = Color.WHITE;

    //Associated grid
    private final Grid grid;

    /**
     * Constructs grid-data instance to be associated with the provided grid.
     * @param grid Grid for which this grid-data is for.
     */
    public GridData(final Grid grid){
        this.grid = grid;
    }

    /**
     * Get the grid that this grid-data is for.
     * @return The grid that this data is for.
     */
    final public Grid getGrid() {
        return this.grid;
    }

    /**
     * Get the contents of the cell to be displayed in the grid's string representation.
     * @param cell The cell whose contents is to be returned
     * @return The contents of the cell as a string. This representation of the cell's data is specifically intended
     * to be used when displaying the grid as a string. If there is no data for the provided cell, an empty string will be returned.
     */
    @Deprecated
    public String getCellContents(final Cell cell){
        return DEFAULT_CELL_CONTENTS_STRING;
    }

    /**
     * Get the color of the cell to be displayed in the grid's image representations. 
     * @param cell The cell whose color is to be returned.
     * @return The color of the cell. If there is no color set for the provided cell, an int representing white will be returned.
     */
    @Deprecated
    public Color getCellColor(final Cell cell){
        return DEFAULT_CELL_COLOR;
    }
}
