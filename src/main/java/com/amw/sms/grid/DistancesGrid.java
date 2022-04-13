package com.amw.sms.grid;

import java.util.Optional;

/**
 * Grid class with distances assigned to each cell.
 */
public class DistancesGrid extends Grid{
    private Optional<Distances> distances;

    public DistancesGrid(final int rowCount, final int colCount){
        super(rowCount, colCount);
        this.distances = Optional.empty();
    }

    /**
     * Gets contents of Cell. Uses the distance from the root cell as the contents.
     * If no distances are set, then the default cell-contents are used.
     * @param cell Cell to get contents of
     * @return Cell contents
     */
    public final String contentsOf(final Cell cell){
        //No distances set -> use default
        if(this.distances.isEmpty()){
            return super.contentsOf(cell); 
        }

        //No distance set for specific cell -> use defualt
        final var distanceFromRoot = this.distances.get().getDistance(cell);
        return distanceFromRoot >= 0
            ? Integer.toString(distanceFromRoot, 32)
            : super.contentsOf(cell);
    }

    /**
     * Get distances set for this grid.
     * @return Optional containing distances set for this grid. If they haven't been set,
     * the optional will be empty.
     */
    public final Optional<Distances> getDistances(){
        return this.distances;
    }

    /**
     * Set the distances for this grid.
     * @param distances Distances to use for this grid.
     */
    public final void setDistances(final Distances distances){
        this.distances = Optional.of(distances);
    }
}
