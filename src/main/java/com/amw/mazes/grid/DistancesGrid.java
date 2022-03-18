package com.amw.mazes.grid;

import java.util.Optional;

public class DistancesGrid extends Grid{
    private Optional<Distances> distances;

    public DistancesGrid(final int rowCount, final int colCount){
        super(rowCount, colCount);
        this.distances = Optional.empty();
    }

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

    public final String toString(){
        return super.toString();
    }

    public final Optional<Distances> getDistances(){
        return this.distances;
    }

    public final void setDistances(final Distances distances){
        this.distances = Optional.of(distances);
    }
}
