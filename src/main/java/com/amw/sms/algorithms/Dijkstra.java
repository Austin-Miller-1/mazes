package com.amw.sms.algorithms;

import java.util.LinkedList;
import java.util.List;

import com.amw.sms.algorithms.solving.MazeSolveAlgorithm;
import com.amw.sms.grid.Cell;
import com.amw.sms.grid.CellDistances;
import com.amw.sms.grid.Grid;
import com.amw.sms.grid.griddata.CellDistancesDataLayer;
import com.amw.sms.grid.griddata.GridData;
import com.amw.sms.mazes.Maze;

import org.springframework.stereotype.Component;

//TODO - upgrade with caching mode to reduce number of calls to algorithm.
@Component
public class Dijkstra extends MazeSolveAlgorithm {
    /**
     * Constructs new Dijkstra algorithm.
     */
    public Dijkstra(){}

    @Override
    public List<Cell> getSolution(final Maze maze){
        final var distancesFromStart = this.getDistances(maze.getGrid(), maze.getStartCell(), true);
        return this.getPathTo(maze.getEndCell(), distancesFromStart);
    }

    /**
     * Get the distances from the provided root cell and every other cell on the grid
     * that is connected to the root by some path.
     * @param grid Grid to use.
     * @param rootCell Cell to get distances relative to.
     * @return Distances between the root cell and every other cell. If there is no
     * path connecting the root and a cell on the grid, the cell will not have a distance
     * set.
     */
    public CellDistances getDistances(final Grid grid, final Cell rootCell){
      return this.getDistances(grid, rootCell, false);
    }

     /**
     * Get the distances from the provided root cell and every other cell on the grid
     * that is connected to the root by some path.
     * @param grid Grid to use.
     * @param rootCell Cell to get distances relative to.
     * @param isFullAlgorithmExecution Flag that indicates whether or not the call to this method is part of the 
     * Dijktra algorithm's complete execution, or if it is being called separate from the algorithm. This will determine
     * how the algorithm's execution steps are indicated to its observers. If false, the algorithm will be considered
     * finished at the end of this method's execution. If true, the algorithm will only be considered finished when the
     * full algorithm is complete. 
     * @return Distances between the root cell and every other cell. If there is no
     * path connecting the root and a cell on the grid, the cell will not have a distance
     * set.
     */
    private CellDistances getDistances(final Grid grid, final Cell rootCell, final boolean isFullAlgorithmExecution){
        final var distances = new CellDistances(grid, rootCell);

        final var dijkstraGridData = new GridData(grid);
        dijkstraGridData.addAtFront(new CellDistancesDataLayer(distances, "dijk-distances"));
        this.executionState.setAlgorithmGridData(dijkstraGridData);
        this.started();

        final var frontier = new LinkedList<Cell>();
        frontier.add(rootCell);

        while(!frontier.isEmpty()){
            final var frontierCell = frontier.remove();

            frontierCell.getLinks()
                .stream()
                .forEach((var linkedCell) -> {
                    //Already visited cells should not be visited again (only happens in imperfect mazes)
                    if(distances.isDistanceSet(linkedCell)){return;}

                    distances.setDistance(linkedCell, distances.getDistance(frontierCell)+1);
                    frontier.add(linkedCell);
                    
                    this.completedStep();
                });
        }

        //If the method is invoked by clients on its own (i.e. not as part of the complete Dijktra algorithm)
        //then the algorithm should be considered finished at this point. Otherwise, there are still more steps
        //that need to be done within the algorithm.  
        if(!isFullAlgorithmExecution){
            this.finished();
        }

        return distances;
    }

    /**
     * Returns the path of cells from the root cell set within the cell-distances instance and the provided end cell
     * @param cell Cell to find a path to.
     * @param distancesFromRoot Distances from a configured root-cell and all other connected cells on the grid
     * @return Path of cells from the root cell to the provided end-cell, given that one exists. If a path exists,
     * the returned list will start with the root cell (i.e. first index) and will end with the end cell. If no such
     * path exists connecting the two cells, an empty list is returned.
     */
    private List<Cell> getPathTo(final Cell cell, final CellDistances distancesFromRoot){
        final var startCell = distancesFromRoot.getRootCell();
        final var path = new LinkedList<Cell>();
        path.add(cell);

        var currentCell = cell;
        while(currentCell != startCell){
            final var cellDistance = distancesFromRoot.getDistance(currentCell);
            final var nextCell = currentCell.getLinks()
                .stream()
                .filter((var neighbor) -> distancesFromRoot.getDistance(neighbor) < cellDistance)
                .findFirst();

            //NO PATH FROM ROOT TO CELL
            if(nextCell.isEmpty()){
                return new LinkedList<Cell>();
            }

            path.push(currentCell = nextCell.get());
        }

        return path;
    }
}
