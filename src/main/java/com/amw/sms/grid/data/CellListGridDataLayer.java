package com.amw.sms.grid.data;

import java.awt.Color;
import com.amw.sms.grid.CellList;

/**
 * Cell-list based grid-data layer.
 */
public class CellListGridDataLayer extends AbstractCellListGridDataLayer {
    /**
     * Constructs new cell-list grid-data layer.
     * @param cells Cells.
     * @param name Name of layer.
     */
    public CellListGridDataLayer(CellList cells, String name){
        super(cells, name);
    }

    /**
     * Constructs new cell-list grid-data layer.
     * @param cells Cells.
     * @param name Name of layer.
     * @param layerColor Layer color.
     */
    public CellListGridDataLayer(CellList cells, String name, Color layerColor){
        super(cells, name, layerColor);
    }

    /**
     * Sets the cells of the grid-data layer.
     * @param cells Cells.
     */
    public void setCells(CellList cells){
        this.cells = cells;
    }
}
