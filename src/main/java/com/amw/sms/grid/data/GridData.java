package com.amw.sms.grid.data;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import com.amw.sms.grid.Cell;
import com.amw.sms.grid.Grid;

/**
 * Container and effectively manager of all of the data associated with the grid's cells. Contains any number of data layers
 * that may have data (such as string-contents and colors) associated with the different cells. Layers can be added, removed, enabled,
 * disabled, etc. via this class.
 */
public class GridData {
    private final List<GridDataLayer> dataLayers;
    private Optional<GridDataLayer> dataMaskLayer;

    //Associated grid
    private final Grid grid;

    /**
     * Constructs grid-data instance to be associated with the provided grid.
     * @param grid Grid for which this grid-data is for.
     */
    public GridData(final Grid grid){
        this.grid = grid;
        this.dataLayers = new ArrayList<>();
        this.dataMaskLayer = Optional.empty();
    }
    
    /**
     * Adds provided layer to the top of the layer-stack. Being at the top of the layer-stack
     * means that this layer's values (if they exist) are taken as the highest-priority. That is,
     * if multiple layers have a value and only one is requested, the front layer's will be used.
     * @param layer Layer to add.
     * @see GridData#addAtBottom(GridDataLayer)
     */
    public void addAtTop(final GridDataLayer layer){
        this.dataLayers.add(0, layer);
    }

    /**
     * Adds provided layer to the bottom of the layer-stack. Being at the bottom of the layer-stack
     * means that this layer's values (if they exist) are taken as the lowest-priority. That is,
     * if multiple layers have a value, and only one is requested, all of the other layers will
     * be checked prior to the back layer. Therefore, the bottom layer's value will only be used
     * if no other layer has such a value.
     * @param layer Layer to add.
     * @see GridData#addAtTop(GridDataLayer)
     */
    public void addAtBottom(final GridDataLayer layer){
        this.dataLayers.add(layer);
    }

    /**
     * Adds layer to the layer-stack above the existing layer. If the "existing-layer" does not exist within the
     * stack, the new layer is added to the top of the stack.
     * @param existingLayer Layer expected to be in the layer-stack already. While expected to be in the layer-stack,
     * the new layer is added even if it is not.
     * @param newLayer Layer to add.
     */
    public void addAbove(final GridDataLayer existingLayer, final GridDataLayer newLayer){
        this.addRelativeTo(existingLayer, newLayer, false);
    }

    /**
     * Adds layer to the layer-stack below the existing layer. If the "existing-layer" does not exist within the
     * stack, the new layer is added to the top of the stack.
     * @param existingLayer Layer expected to be in the layer-stack already. While expected to be in the layer-stack,
     * the new layer is added even if it is not.
     * @param newLayer Layer to add.
     */
    public void addBelow(final GridDataLayer existingLayer, final GridDataLayer newLayer){
        this.addRelativeTo(existingLayer, newLayer, true);
    }

    /**
     * Adds layer to the layer-stack relative to an existing layer. If the "existing-layer" does not exist within the
     * stack, the new layer is added to the top of the stack.
     * @param existingLayer Layer expected to be in the layer-stack already. While expected to be in the layer-stack,
     * the new layer is added even if it is not.
     * @param newLayer Layer to add.
     * @param addAbove True/false whether to add the new layer above or below the existing layer.
     */
    private void addRelativeTo(final GridDataLayer existingLayer, final GridDataLayer newLayer, boolean addAbove){
        if(this.dataLayers.contains(existingLayer)){
            final var newLayerIndex = this.dataLayers.indexOf(existingLayer) + (addAbove ? 1 : 0);
            this.dataLayers.add(newLayerIndex, newLayer);
        }else{
            this.addAtTop(newLayer);
        }
    }

    /**
     * Returns the active layer (if any) at the provided position within the layer stack. 
     * An active layer is defined as a layer that is currently enabled. Disabled layers are not checked.
     * Layer number is not the index within the layer stack, but rather the index of the layer if the stack
     * excluded disabled layers. As an example: if there are 3 layers, A, B and C, and B is disabled, 
     * providing 0 will return A and providing 1 will return C.
     * 
     * //TODO what is the appropriate way of handling bad layerNumber (i.e. > layer stack size, < 0)
     * //Can we recover from such an error? Should we handle it nicely?
     * @param layerNumber Number of the layer to return. 
     * @return The layer at the provided position within the active-layer stack. If no such layer exists, 
     * an empty optional is returned.
     */
    public Optional<GridDataLayer> getActiveLayer(int layerNumber){
        return this.getEnabledLayers()
            .skip(layerNumber)
            .findFirst();
    }

    /**
     * Removes the provided layer from the layer-stack. If the layer does not exist within the stack
     * already, this method has no effect.
     * @param layer Layer to remove.
     */
    public void remove(final GridDataLayer layer){
        this.dataLayers.remove(layer);
    }

    /**
     * Sets the provided layer as a mask for all of the grid data. What this means is that the values of the
     * other layers will only be returned if the mask's data indicates that the cell to check is set. For
     * example, if a layer has data for every single cell, but there a mask layer has been set and this mask
     * only has data for several of te cells, then when retrieving values for the other cells not set in the mask,
     * the other layer's values are ignored. Only values for cells set within the mask are retrieved from other layers.
     *   
     * @param layer Layer to use as a mask.
     * @see GridData#removeMask()
     */
    public void useAsMask(final GridDataLayer layer){
        this.dataMaskLayer = Optional.of(layer);
    }

    /**
     * Removes the currently set mask (if any) of the grid-data.
     * @see GridData#useAsMask(GridDataLayer)
     */
    public void removeMask(){
        this.dataMaskLayer = Optional.empty();
    }

    /**
     * Returns stream of all of the enabled grid-data layers.
     * @return Stream of enabled grid-data layers.
     */
    private Stream<GridDataLayer> getEnabledLayers(){
        return this.dataLayers
            .stream()
            .filter(GridDataLayer::isEnabled);
    }

    /**
     * Returns the primary value of the provided optional stream associated with the data layers. The definition of "primary value" 
     * is value from the highest active layer that has a value set. For example, if the top layer has a value, it will be returned. 
     * If the top layer is inactive OR if it is active but does not have a value, then the following layers will be checked.
     * @param <T> The type of the value within the Optionals. Not importatnt to this method's functionality.
     * @param stream Stream of values to get the primary value from.
     * @return Primary value.
     */
    private <T> Optional<T> primaryValueOf(final Stream<Optional<T>> stream){
        return stream
            .filter(Optional::isPresent)
            .findFirst()
            .orElse(Optional.empty());
    }

    /**
     * Returns true/false whether data for the provided cell is considered active or not.
     * All cells are considered active unless they are not within the set data-layer mask, if one
     * has been set.
     * @param cell Cell to check for.
     * @return True if the cell is within the data-layer mask OR if no such mask has been set.
     */
    private boolean isCellActive(final Cell cell){
        return this.dataMaskLayer.isEmpty()
        || this.dataMaskLayer.get().isCellSet(cell);
    }

    /**
     * Maps the provided cell to some optional using provided function if it's active. Otherwise, it
     * maps the cell to an empty optional/  
     * @param <T> Type of the value within the Optional that the mapper returns.
     * @param cell Cell to map from.
     * @param mapper Mapping function to apply to the cell.
     * @return The return value of the mapper applied to the cell if the cell is active. Otherwise, returns an empty optional.
     */
    private <T> Optional<T> toFunctionIfActiveCell(final Cell cell, final Function<Cell, Optional<T>> mapper){
        return this.isCellActive(cell) 
            ? mapper.apply(cell)
            : Optional.empty();
    }

    /**
     * Returns stream of all of the active layers' cell contents. 
     * If a layer does not have contents for the cell, an empty optional will be found in the stream at the 
     * layer's postion. Only returns layer values for active cells.
     * @param cell Cell.
     * @return Stream of optionals containing each layers' cell contents, if any. The order of the values in the stream 
     * will match the order of the active layers (e.g. the first value will be from the top layer).
     */
    private Stream<Optional<String>> cellContentsStreamFor(final Cell cell){
        return this.getEnabledLayers()
            .map(layer -> this.toFunctionIfActiveCell(cell, layer::getCellContents));
    }

    /**
     * Get a list of all of the contents (if any) associated with the cell. The list will contain entries for every 
     * data-layer, even if a data layer does not have a value for that particular cell. 
     * 
     * Only returns layer values for active cells. If a cell is considered inactive, an empty optional WILL ALWAYS be returned for this method.
     * A cell is considered "active" if it is within the data-layer mask OR if there is no data-layer mask set. See 
     * {@link GridData#useAsMask(GridDataLayer)} for info on the data-layer mask functionality.
     * 
     * @param cell Cell.
     * @return List of optionals containing each layers' cell contents, if any. The order of the values in the list 
     * will match the order of the active layers (e.g. the first value will be from the top layer).
     */
    public List<Optional<String>> getCellContents(final Cell cell){
        return this.cellContentsStreamFor(cell).toList();
    }

    /**
     * Get the cell's primary contents. The definition of "primary value" is value from the highest active layer
     * that has a value set. For example, if the top layer has a value, it will be returned. If the top layer is inactive OR if it
     * is active but does not have a value, then the following layers will be checked. 
     * 
     * Only returns layer values for active cells. See {@link GridData#getCellContents(Cell)} for more details.
     * 
     * @param cell Cell
     * @return Primary cell-contents, if any. Empty optional if no active layer has a value.
     */
    public Optional<String> getPrimaryCellContents(final Cell cell){
        return this.primaryValueOf(this.cellContentsStreamFor(cell));
    }

    /**
     * Returns stream of all of the active layers' cell contents.
     * If a layer does not have color for the cell, an empty optional will be found in the stream at the 
     * layer's postion.
     * 
     * Only returns layer values for active cells. See {@link GridData#getCellContents(Cell)} for more details.
     * 
     * @param cell Cell.
     * @return Stream of optionals containing each layers' cell color, if any. The order of the values in the stream 
     * will match the order of the active layers (e.g. the first value will be from the top layer).
     */
    private Stream<Optional<Color>> cellColorsStreamFor(final Cell cell){
        return this.getEnabledLayers()
            .map(layer -> this.toFunctionIfActiveCell(cell, layer::getCellColor));
    }

    /**
     * Get a list of all of the colors (if any) associated with the cell. The list will contain entries for every 
     * data-layer, even if a data layer does not have a value for that particular cell.
     * 
     * Only returns layer values for active cells. See {@link GridData#getCellContents(Cell)} for more details.
     * 
     * @param cell Cell.
     * @return List of optionals containing each layers' cell color, if any. The order of the values in the list 
     * will match the order of the active layers (e.g. the first value will be from the top layer).
     */
    public List<Optional<Color>> getCellColors(final Cell cell){
        return this.cellColorsStreamFor(cell).toList();
    }

    /**
     * Get the cell's primary color. See {@link GridData#getPrimaryCellContents(Cell)} for definition of "primary value".
     * 
     * Only returns layer values for active cells. See {@link GridData#getCellContents(Cell)} for more details.
     * 
     * @param cell Cell
     * @return Primary cell-color, if any. Empty optional if no active layer has a value.
     */
    public Optional<Color> getPrimaryCellColor(final Cell cell){
        return this.primaryValueOf(this.cellColorsStreamFor(cell));
    }

    /**
     * Returns stream of all of the active layers' cell-highlight colors.
     * If a layer does not have highliights for the cell, an empty optional will be found in the stream at the 
     * layer's postion.
     * 
     * Only returns layer values for active cells. See {@link GridData#getCellContents(Cell)} for more details.
     * 
     * @param cell Cell.
     * @return Stream of optionals containing each layers' cell-highlight color, if any. The order of the values in the stream 
     * will match the order of the active layers (e.g. the first value will be from the top layer).
     */
    private Stream<Optional<Color>> cellHighlightsStreamFor(final Cell cell){
        return this.getEnabledLayers()
            .map(layer -> {
                return this.isCellActive(cell) && layer.isCellHighlighted(cell)
                    ?   Optional.of(layer.getLayerColor())
                    :   Optional.empty();
            });
    }

    /**
     * Get a list of all of the highlight colors (if any) associated with the cell. The list will contain entries for every 
     * data-layer, even if a data layer does not have highlgihts for that particular cell.
     * 
     * Only returns layer values for active cells. See {@link GridData#getCellContents(Cell)} for more details.
     * 
     * @param cell Cell.
     * @return List of optionals containing each layers' cell highlight-color, if any. The order of the values in the list 
     * will match the order of the active layers (e.g. the first value will be from the top layer).
     */
    public List<Optional<Color>> getCellHighlights(final Cell cell){
        return this.cellHighlightsStreamFor(cell).toList();
    }

    /**
     * Get the cell's primary highlight color. See {@link GridData#getPrimaryCellContents(Cell)} for definition of "primary value".
     * 
     * Only returns layer values for active cells. See {@link GridData#getCellContents(Cell)} for more details.
     * @param cell Cell
     * @return Primary cell-highlight color, if any. Empty optional if no active layer has a value.
     */
    public Optional<Color> getPrimaryCellHighlight(final Cell cell){
        return this.primaryValueOf(this.cellHighlightsStreamFor(cell));
    }

    /**
     * Get the grid that this grid-data is for.
     * @return The grid that this data is for.
     */
    final public Grid getGrid() {
        return this.grid;
    }
}