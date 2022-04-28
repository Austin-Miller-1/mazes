package com.amw.sms.grid;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple implmentation of the GridData abstract class. Allows for direct setting
 * of cell's contents and colors. This is as opposed to other implementations whose
 * cell's contents and colors would be derived from other information about the grid.
 */
public class SimpleGridData extends GridData {
    private final Map<Cell, String> gridContents;
    private final Map<Cell, Color> gridColors;

    /**
     * Constructs new SimpleGridData instance.
     * @param grid Grid for which the data is for.
     */
    public SimpleGridData(Grid grid) {
        super(grid);

        this.gridContents = new HashMap<Cell, String>();
        this.gridColors = new HashMap<Cell, Color>();
    }

    /**
     * Set the cell's string contents.
     * @param cell Cell
     * @param contents Contents of the cell to be displayed within the grid's string representation.
     */
    public void setCellContents(Cell cell, String contents){
        this.gridContents.put(cell, contents);
    }

    /**
     * Set the cell's color.
     * @param cell Cell
     * @param color Color of the cell to be used within the grid's image representations.
     */
    public void setCellColor(Cell cell, Color color){
        this.gridColors.put(cell, color);
    }

    /**
     * {@inheritDoc}
     * If distance is unset, the cell's contents will be determined by the 
     * {@link GridData} superclass.
     */
    @Override
    public String getCellContents(Cell cell){
        return this.gridContents.containsKey(cell)
            ? this.gridContents.get(cell)
            : super.getCellContents(cell);
    }

    /**
     * {@inheritDoc}
     * If color is unset, the cell's color will be determined by the 
     * {@link GridData} superclass.
     */
    @Override
    public Color getCellColor(Cell cell){
        return this.gridColors.containsKey(cell)
            ? this.gridColors.get(cell)
            : super.getCellColor(cell);
    }
}
