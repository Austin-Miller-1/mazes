package com.amw.sms.grid.data;

import java.awt.Color;
import java.util.Optional;

import com.amw.sms.grid.Cell;
import com.amw.sms.grid.CellPath;

/**
 * Instances of this class contain the data for a single layer of the complete GridData. 
 * Each layer has context-specific data, however, is generic enough to be represented using this class 
 * and subclasses to it. For instance, the different data used by different maze-algorithms, the data produced by
 * such algorithms, any specific areas of interest in the maze, paths, etc. Grid-data layers are to be used within
 * a Grid's GridData instance, which is effectively the manager of the layers.
 */
public abstract class GridDataLayer {
    protected final static Color DEFAULT_LAYER_COLOR = Color.BLACK;

    private final String name;
    private Color layerBaseColor;
    private boolean isEnabled;
    private boolean areCellContentsEnabled;
    private boolean areCellColorsEnabled;
    private boolean areCellHighlightsEnabled;

    /**
     * Constructs new layer with the provided name and color.
     * @param name Layer name.
     * @param layerColor Layer color.
     */
    protected GridDataLayer(final String name, final Color layerColor){
        this.name = name;
        this.layerBaseColor = layerColor;
        this.isEnabled = true;
        this.areCellContentsEnabled = true;
        this.areCellColorsEnabled = true;
        this.areCellHighlightsEnabled = true;
    }

    /**
     * Constructs new layer with the provided name. The color to be used by the layer will be set to black. 
     * @param name Layer name.
     */
    protected GridDataLayer(final String name){
        this(name, DEFAULT_LAYER_COLOR);
    }

    /**
     * Returns the name of the layer.
     * @return The layer's name.
     */
    public final String getName(){
        return this.name;
    }

    /**
     * Sets the layer's main color.
     * @param layerColor New layer color.
     */
    public final void setLayerColor(Color layerColor){
        this.layerBaseColor = layerColor;
    }

    /**
     * Returns the main color of the layer.
     * @return The layer's color.
     */
    public final Color getLayerColor(){
        return this.layerBaseColor;
    }

    /**
     * Returns true/false whether the layer has been enabled or disabled.
     * Note that enabled/disabled layers do not act differently on their own. It is up to the 
     * user of the layers to determine how to handle enabled/disabled layers differently.
     * @see GridDataLayer#enable()
     * @see GridDataLayer#disable()
     * @return True if the layer is enabled. False otherwise.
     */
    public final boolean isEnabled(){
        return this.isEnabled;
    }

    /**
     * Enables the layer.
     * Note that enabled/disabled layers do not act differently on their own. It is up to the 
     * user of the layers to determine how to handle enabled/disabled layers differently.
     * @see GridDataLayer#isEnabled()
     * @see GridDataLayer#disable()
     */
    public final void enable(){
        this.isEnabled = true;
    }

    /**
     * Disables the layer.
     * Note that enabled/disabled layers do not act differently on their own. It is up to the 
     * user of the layers to determine how to handle enabled/disabled layers differently.
     * @see GridDataLayer#isEnabled()
     * @see GridDataLayer#enable()
     */
    public final void disable(){
        this.isEnabled = false;
    }

    /**
     * Enables cell-contents for this layer. 
     * When enabled, the {@link GridDataLayer#getCellContents(Cell)} method will
     * return any contents associated with the cell within the data-layer.
     * @see GridDataLayer#disableCellContents()
     * @see GridDataLayer#getCellContents(Cell)
     */
    public final void enableCellContents(){
        this.areCellContentsEnabled = true;
    }

    /**
     * Disables cell-contents for this layer. 
     * When disabled, the {@link GridDataLayer#getCellContents(Cell)} method will
     * return always return an empty Optional, even if this layer has data associated
     * with the provided cell.
     * @see GridDataLayer#disableCellContents()
     * @see GridDataLayer#getCellContents(Cell)
     */
    public final void disableCellContents(){
        this.areCellContentsEnabled = false;
    }

    /**
     * Returns the cell-contents, if any, that this layer has associated with the provided
     * cell. If no such data has been associated, or if cell-contents are disabled for the layer, 
     * then an empty optional will be returned.
     * 
     * This is a template method. The doGetCellContents hook must be implemented for this method.
     * 
     * @param cell Cell to get contents for.
     * @return Optional containing cell-contents from the layer. If no such data exists or cell-contents
     * are disabled, returns an empty optional.
     * @see GridDataLayer#enableCellContents()
     * @see GridDataLayer#disableCellContents()
     * @see GridDataLayer#doGetCellContents(Cell)
     */
    public final Optional<String> getCellContents(final Cell cell){
        return this.areCellContentsEnabled
            ? this.doGetCellContents(cell)
            : Optional.empty();
    }

    /**
     * Hook for {@link GridDataLayer#getCellContents(Cell)} method. 
     * Returns the cell-contents (if any) that the layer has associated with the provided cell.
     * @param cell Cell to get contents for.
     * @return Optional containing the cell contents (if any). Empty optional if no such content exists.
     */
    abstract Optional<String> doGetCellContents(final Cell cell);

    /**
     * Enables cell colors for this layer. 
     * When enabled, the {@link GridDataLayer#getCellColors(Cell)} method will
     * return any color associated with the cell within the data-layer.
     * @see GridDataLayer#disableCellColors()
     * @see GridDataLayer#getCellColor(Cell)
     */
    public final void enableCellColors(){
        this.areCellColorsEnabled = true;
    }

    /**
     * Disables cell colors for this layer. 
     * When disabled, the {@link GridDataLayer#getCellColor(Cell)} method will
     * return always return an empty Optional, even if this layer has a color associated
     * with the provided cell.
     * @see GridDataLayer#disableCellColors()
     * @see GridDataLayer#getCellColor(Cell)
     */
    public final void disableCellColors(){
        this.areCellColorsEnabled = false;
    }

    /**
     * Returns the cell color, if any, that this layer has associated with the provided
     * cell. If no such color has been associated, or if cell colors are disabled for the layer, 
     * then an empty optional will be returned.
     * 
     * This is a template method. The doGetCellColor hook must be implemented for this method.
     * 
     * @param cell Cell to get color for.
     * @return Optional containing the cell color from the layer. If no such data exists or cell colors
     * are disabled, returns an empty optional.
     * @see GridDataLayer#enableCellColors()
     * @see GridDataLayer#disableCellColors()
     * @see GridDataLayer#doGetCellColor(Cell)
     */
    public final Optional<Color> getCellColor(final Cell cell){
        return this.areCellColorsEnabled
            ? this.doGetCellColor(cell)
            : Optional.empty();
    }

    /**
     * Hook for {@link GridDataLayer#getCellColor(Cell)} method. 
     * Returns the color (if any) that the layer has associated with provided cell.
     * @param cell Cell to get the color for.
     * @return Optional containing the cell color (if any). Empty optional if no such color exists.
     */
    abstract Optional<Color> doGetCellColor(Cell cell);

    /**
     * Enables cell highlights for this layer. 
     * When enabled, the {@link GridDataLayer#isCellHighlighted(Cell)} method will
     * return true if the cell is set within the data-layer.
     * @see GridDataLayer#disableCellHighlights()
     * @see GridDataLayer#isCellHighlighted(Cell)
     */
    public final void enableCellHighlights(){
        this.areCellHighlightsEnabled = true;
    }

    /**
     * Disables cell highlights for this layer. 
     * When disabled, the {@link GridDataLayer#isCellHighlighted(Cell)} method will
     * return always return false, even if the cell is set for the layer.
     * with the provided cell.
     * @see GridDataLayer#disableCellHighlights()
     * @see GridDataLayer#isCellHighlighted(Cell)
     */
    public final void disableCellHighlights(){
        this.areCellHighlightsEnabled = false;
    }

    /**
     * Returns true or false whether the cell is to be highlighted or not. The criteria for
     * if a cell should be highlighted or not is simply if it is or isn't set within the layer.
     * Set cells should be highlighted, unset cells should not be. 
     * If cell highlights are disabled for the layer, the method will always return false.
     * 
     * This is a template method. The isCellSet hook must be implemented for this method.
     * 
     * @param cell Cell to check for.
     * @return True if the cell should be highlighted (i.e. it is set and highlights are enabled). 
     * False otherwise.
     * @see GridDataLayer#enableCellHighlights()
     * @see GridDataLayer#disableCellHighlights()
     * @see GridDataLayer#isCellSet(Cell)
     */
    public final boolean isCellHighlighted(final Cell cell){
        return this.areCellHighlightsEnabled & this.isCellSet(cell);
    }

    /**
     * Returns true/false whether the provided cell has data set within the layer or not.
     * @param cell Cell check for.
     * @return Returns true if the cell has data set within the layer, false otherwise.
     */
    abstract boolean isCellSet(final Cell cell);

    /**
     * Returns the path associated with the grid-data layer, if any.
     * 
     * Within the GridDataLayer abstract class, this method provides a common default
     * behavior of the method. For subclasses that work with paths, it should be overridden. 
     * @return Optional containing the path associated with the layer, if any. If no
     * such path exists, an empty optional is returned.
     */
    public Optional<CellPath> getPath(){
        return Optional.empty();
    }
}
