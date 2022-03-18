package com.amw.mazes.grid;

import java.util.ArrayList;
import java.util.List;

public class LongestPath {
    public LongestPath(){}

    public List<Cell> getPath(DistancesGrid grid){
        final var distances = grid.getDistances();
        if(distances.isEmpty()){
            return new ArrayList<Cell>();   //TODO is this good enough
        }

        final var endCell = distances.get().getMax();
        final var startCell = endCell.getKey().getDistances().getMax();
        return startCell.getKey().getDistances().pathToGoal(endCell.getKey());
    }
}
