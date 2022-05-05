package com.amw.sms.grid.data;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.amw.sms.grid.Cell;

/**
 * Simple implmentation of the GridDataLayer abstract class. Allows for direct setting
 * of cell's contents and colors within the layer. This is as opposed to other implementations whose
 * cell's contents and colors would be derived from other information.
 */
public class SimpleGridDataLayer extends GridDataLayer {
    private final Map<Cell, String> gridContents;
    private final Map<Cell, Color> gridColors;

    /**
     * Constructs new simple grid-data layer with the provided name and layer color.
     * @param name Layer name.
     * @param layerColor Layer color.
     */
    public SimpleGridDataLayer(final String name, final Color layerColor){
        super(name, layerColor);

        this.gridContents = new HashMap<Cell, String>();
        this.gridColors = new HashMap<Cell, Color>();
    }

    /**
     * Constructs new simple grid-data layer with the provided name. Layer color is set to 
     * super-class's default.
     * @param name Layer name.
     */
    public SimpleGridDataLayer(final String name){
        super(name);

        this.gridContents = new HashMap<Cell, String>();
        this.gridColors = new HashMap<Cell, Color>();
    }

     /**
     * Set the cell's contents within the layer.
     * @param cell Cell.
     * @param contents Contents to be associated with the cell in the layer.
     */
    public void setCellContents(final Cell cell, final String contents){
        this.gridContents.put(cell, contents);
    }

    /**
     * Set the cell's color within the layer.
     * @param cell Cell
     * @param color Color to be associated with the cell in the layer.
     */
    public void setCellColor(final Cell cell, final Color color){
        this.gridColors.put(cell, color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<String> doGetCellContents(final Cell cell){
        return this.gridContents.containsKey(cell)
            ? Optional.of(this.gridContents.get(cell))
            : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<Color> doGetCellColor(final Cell cell){
        return this.gridColors.containsKey(cell)
            ? Optional.of(this.gridColors.get(cell))
            : Optional.empty();
    }

    /**
     * {@inheritDoc}
     * 
     * Cells that have contents set within the layer, but no color, are considered to be set.
     * Likewise, cells that have a color set, but no contents, are considered to be set.
     */
    @Override
    boolean isCellSet(final Cell cell){
        return this.gridContents.containsKey(cell) 
            || this.gridColors.containsKey(cell);
    }
}
