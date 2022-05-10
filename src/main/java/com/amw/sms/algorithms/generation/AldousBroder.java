package com.amw.sms.algorithms.generation;

import java.util.HashSet;

import com.amw.sms.grid.Cell;
import com.amw.sms.grid.Grid;

import org.springframework.stereotype.Component;

/**
 * The Aldous-Broder maze generation algorithm. Uses random walks to generate unbiased mazes uniformly.
 */
@Component
public class AldousBroder extends MazeGenAlgorithm{

    /**
     * Constructs new instance of Aldous-Broder algorithm.
     */
    public AldousBroder(){}

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(Grid grid) {
        this.started();
        
        var currentCell = grid.getRandomCell();
        final var visitedCells = new HashSet<Cell>();
        visitedCells.add(currentCell);

        while(visitedCells.size() != grid.getCellCount()){
            var neighbor = currentCell.getRandomNeighbor().get(); 

            if(!visitedCells.contains(neighbor)){
                neighbor.link(currentCell);
                visitedCells.add(neighbor);
                this.completedStep();
            }

            currentCell = neighbor;
        }

        this.finished();
    }
}
