package com.amw.sms.grid.griddata;

import java.awt.Color;
import java.util.Optional;

import com.amw.sms.grid.Cell;
import com.amw.sms.grid.CellDistances;

/**
 * Grid-data layer that contains the distances between the cells on the grid and a specified root-cell. 
 */
public class CellDistancesDataLayer extends GridDataLayer {
    private final CellDistances distances;

    /**
     * Constructs new cell-distances grid-data layer with the provided distances, name and layer color.
     * @param distances Cell distances.
     * @param name Layer name.
     * @param layerColor Layer color.
     */
    public CellDistancesDataLayer(final CellDistances distances, final String name, final Color layerColor){
        super(name, layerColor);
        this.distances = distances;
    }

    /**
     * Constructs new cell-distances grid-data layer with the provided distances and name. Layer color is set 
     * to super-class's default.
     * @param distances Cell distances.
     * @param name Layer name.
     */
    public CellDistancesDataLayer(final CellDistances distances, final String name){
        super(name);
        this.distances = distances;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<String> doGetCellContents(final Cell cell) {
        return this.isCellSet(cell)
        ?   Optional.of(Integer.toString(this.distances.getDistance(cell)))
        :   Optional.empty();
    }

    /**
     * {@inheritDoc}
     */    
    @Override
    Optional<Color> doGetCellColor(final Cell cell) {
        //Unset => go to superclass
        if(!this.isCellSet(cell)){
            return Optional.empty();
        }

        //Root-cell case
        if(cell.equals(this.distances.getRootCell())){
            return Optional.of(Color.WHITE); 
        }

        //Color intensity will be fractional based on the max distance. This will
        //allow for the colors to transition nicely across the cells.
        final var distance = this.distances.getDistance(cell);
        final var maxDistance = this.distances.getMaxDistance();
        final var intensity = (float) (maxDistance-distance) / maxDistance;
        final var darkValue = Math.round(255*intensity);            //Secondary colors of the cell (creates a shade of primary)
        final var brightValue = 128 + Math.round(127*intensity);    //Primary color of the cell

        return Optional.of(new Color(darkValue, brightValue, darkValue));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isCellSet(final Cell cell) {
        return this.distances.isDistanceSet(cell);
    }
}
