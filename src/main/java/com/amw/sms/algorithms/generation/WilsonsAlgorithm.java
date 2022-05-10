package com.amw.sms.algorithms.generation;

import java.util.ArrayList;

import com.amw.sms.grid.Cell;
import com.amw.sms.grid.Grid;
import com.amw.sms.util.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Wilson's random-walk maze generation algorithm.
 */
@Component
public class WilsonsAlgorithm extends MazeGenAlgorithm{
    @Autowired
    private Utilities utils;

    /**
     * Constructs new instance of Wilson's algorithm.
     */
    public WilsonsAlgorithm(){}

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(Grid grid) {
        this.started();

        //ASSUMPTION - Grid is completely unvisited. TO REMOVE: Create getUnvisitedCells method or getUnlinkedCells method
        final var unvisitedCells = new ArrayList<Cell>(grid.getCells()); 
        unvisitedCells.remove(utils.randomElementFrom(unvisitedCells));

        while(!unvisitedCells.isEmpty()){
            //1. Start path from unvisted cell
            var cell = utils.randomElementFrom(unvisitedCells);
            var newPath = new ArrayList<Cell>();
            newPath.add(cell);
            
            //2. Generate path from start until reaching vistited cell
            while(unvisitedCells.contains(cell)){
                cell = cell.getRandomNeighbor().get();

                //If loop, remove it
                if(newPath.contains(cell)){
                    newPath = new ArrayList<>(newPath.subList(0, newPath.indexOf(cell)));
                }

                newPath.add(cell);
            }

            //3. Link all cells in completed path and mark each as visited
            for(int index = 0; index < newPath.size()-1; index++){
                newPath.get(index).link(newPath.get(index+1));
                unvisitedCells.remove(newPath.get(index));

                this.completedStep();
            }
        }
        
        this.finished();
    }
}
