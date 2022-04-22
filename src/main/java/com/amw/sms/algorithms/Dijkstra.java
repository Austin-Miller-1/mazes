package com.amw.sms.algorithms;

import java.util.LinkedList;
import java.util.List;

import com.amw.sms.algorithms.solving.MazeSolveAlgorithm;
import com.amw.sms.grid.Cell;
import com.amw.sms.grid.CellDistances;
import com.amw.sms.grid.Grid;
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
        if(maze.getEntrances().isEmpty()){
            return new LinkedList<Cell>();
        }
        final var startCell = maze.getStartCell().get();
        final var endCell = maze.getEndCell().get();

        return this.getPathTo(endCell, this.getDistances(maze.getGrid(), startCell));
    }

    /**
     * Get the distances from the provided root cell and every other cell on the grid
     * that is connected to the root by some path.
     * @param rootCell Cell to get distances relative to.
     * @return Distances between the root cell and every other cell. If there is no
     * path connecting the root and a cell on the grid, the cell will not have a distance
     * set.
     */
    public CellDistances getDistances(final Grid grid, final Cell rootCell){
        final var distances = new CellDistances(grid, rootCell);

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
                });
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
