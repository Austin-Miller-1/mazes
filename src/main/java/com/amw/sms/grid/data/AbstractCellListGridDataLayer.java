package com.amw.sms.grid.data;

import java.awt.Color;
import java.util.Optional;

import com.amw.sms.grid.Cell;
import com.amw.sms.grid.CellList;
import com.amw.sms.util.ColorUtils;

/**
 * Abstract class for all cell-list based grid-data layer subclasses. 
 * Provides common functionality for any such layer. Does not exist as concrete class since internal 
 * cell-list may be of different types and cannot be set via a singular method, i.e. "setCells". Requires
 * class specific setters (see subclasses).
 */
public abstract class AbstractCellListGridDataLayer extends GridDataLayer {
    public static final String DEFAULT_CELL_CONTENTS_STRING = "•";

    private final ColorUtils colorUtils;
    private CellContentsMode cellContentsMode;
    private String cellContentsString;
    CellList cells;

    /**
     * Constructs new abstract cell-list grid-data layer.
     * @param cells Cells.
     * @param name Name of layer.
     */
    public AbstractCellListGridDataLayer(CellList cells, String name){
        this(cells, name, GridDataLayer.DEFAULT_LAYER_COLOR);
    }

    /**
     * Constructs new abstract cell-list grid-data layer.
     * @param cells Cells.
     * @param name Name of layer.
     * @param layerColor Layer color.
     */
    public AbstractCellListGridDataLayer(CellList cells, String name, Color layerColor){
        super(name, layerColor);
        this.cells = cells;
        this.cellContentsMode = CellContentsMode.SINGLE_STRING_MODE;
        this.cellContentsString = DEFAULT_CELL_CONTENTS_STRING;

        this.colorUtils = new ColorUtils();
        this.disableCellColors();
    }

    /**
     * Sets the data-layer contents mode to "single-string". This means that the contents of every cells within
     * the data layer will share the same string contents, set via this method. 
     * @param string The single string to use for all cell contents.
     */
    public void useSingleString(String string){
        this.cellContentsMode = CellContentsMode.SINGLE_STRING_MODE;
        this.cellContentsString = string;
    }

    /**
     * Sets the data-layer contents mode to "ascending numerics". This means that the contents of the cells within the 
     * data layer will be increasing numbers that reflect their position within the cell list. Therefore, the first
     * cell would have the contents of "0", the next in the list would have "1", and so on.
     */
    public void useAscendingNumericals(){
        this.cellContentsMode = CellContentsMode.ASCENDING_NUMERICAL_MODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<String> doGetCellContents(Cell cell) {
        return this.isCellSet(cell)
            ? Optional.of(this.contentsFor(cell))
            : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<Color> doGetCellColor(Cell cell) {
        return this.isCellSet(cell)
            ? Optional.of(this.colorUtils.getLighterColor(this.getLayerColor()))
            : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isCellSet(Cell cell) {
        return this.cells.getCells().contains(cell);
    }
    
    /**
     * Get the string contents for the provided cell. This cell must have already been confirmed by the caller to be within the
     * cell list. 
     * @param cell Cell.
     * @return String contents for cell.
     */
    private String contentsFor(Cell cell){
        return switch(this.cellContentsMode){
            case SINGLE_STRING_MODE -> this.cellContentsString;
            case ASCENDING_NUMERICAL_MODE -> Integer.toString(this.cells.getCells().indexOf(cell));
        };
    }

    /**
     * Enum containing the different modes the cell-contents may be in for the data layer.
     */
    private enum CellContentsMode {
        SINGLE_STRING_MODE,
        ASCENDING_NUMERICAL_MODE
    };
}
