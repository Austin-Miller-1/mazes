package com.amw.sms.grid.data;

import com.amw.sms.grid.CellPath;

import java.awt.Color;
import java.util.Optional;

/**
 * Cell-path based grid-data layer.
 */
public class CellPathGridDataLayer extends AbstractCellListGridDataLayer{
    private CellPath cellPath;

    /**
     * Constructs new cell-path grid-data layer.
     * @param cells Cell path.
     * @param name Name of layer.
     */
    public CellPathGridDataLayer(final CellPath cells, final String name) {
        this(cells, name, GridDataLayer.DEFAULT_LAYER_COLOR);
    }

    /**
     * Constructs new cell-path grid-data layer.
     * @param cells Cell path.
     * @param name Name of layer.
     * @param layerColor Layer color.
     */
    public CellPathGridDataLayer(final CellPath cells, final String name, final Color layerColor) {
        super(cells, name, layerColor);
        this.cellPath = cells;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CellPath> getPath(){
        return Optional.of(this.cellPath);
    }

    /**
     * Sets the path of the grid-data layer.
     * @param path Cell path.
     */
    public void setPath(final CellPath path){
        this.cellPath = path;
        this.cells = path;
    }
}
