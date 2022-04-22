package com.amw.sms.algorithms;

import java.util.LinkedList;
import java.util.List;

import com.amw.sms.algorithms.solving.MazeSolveAlgorithm;
import com.amw.sms.grid.Cell;
import com.amw.sms.grid.CellDistances;
import com.amw.sms.grid.Distances;
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
    public List<Cell> getSolution(Maze maze){
        if(maze.getEntrances().isEmpty()){
            return new LinkedList<Cell>();
        }
        final var startCell = maze.getStartCell().get();
        final var endCell = maze.getEndCell().get();

        return this.getPath_DEPRECATED(maze.getGrid(), startCell, endCell, this.getDistances_DEPRECATED(maze.getGrid(), startCell));
    }

    @Deprecated
    public Distances getDistances_DEPRECATED(Grid grid, Cell cell){
        final var distances = new Distances(cell);
        final var frontier = new LinkedList<Cell>();    //Frontier will contain all remaining cells to check
        frontier.add(cell);                             //Start frontier with root

        //Go through all frontier cells. 
        //1. Index distances between root and all of the frontier cell's links.
        //2. Add linked cells to frontier if they have not been indexed yet.
        while(!frontier.isEmpty()){
            var frontierCell = frontier.remove();

            frontierCell.getLinks()
                .stream()
                .forEach((var linkedCell) -> {
                    //Skip if already indexed (imperfect mazes)
                    if(distances.getDistance(linkedCell) >= 0) return;
                    
                    distances.addCell(linkedCell, distances.getDistance(frontierCell)+1);
                    frontier.add(linkedCell);
                });
        }

        return distances;
    }

    @Deprecated
    public List<Cell> getPath_DEPRECATED(Grid grid, Cell startCell, Cell endCell, Distances distancesFromStart){
        final var path = new LinkedList<Cell>();
        path.add(endCell);

        var currentCell = endCell;
        while(currentCell != startCell){
            final var cellDistance = distancesFromStart.getDistance(currentCell);
            final var nextCell = currentCell.getLinks()
                .stream()
                .filter((var neighbor) -> distancesFromStart.getDistance(neighbor) < cellDistance)
                .findFirst();

            //NO PATH FROM ROOT TO CELL
            if(nextCell.isEmpty()){
                return new LinkedList<Cell>();
            }

            path.push(currentCell = nextCell.get());
        }

        return path;
    }

    /**
     * Get the distances from the provided root cell and every other cell on the grid
     * that is connected to the root by some path.
     * @param rootCell Cell to get distances relative to.
     * @return Distances between the root cell and every other cell. If there is no
     * path connecting the root and a cell on the grid, the cell will not have a distance
     * set.
     */
    public CellDistances getDistances(Grid grid, Cell rootCell){
        final var distances = new CellDistances(grid, rootCell);

        final var frontier = new LinkedList<Cell>();
        frontier.add(rootCell);

        while(!frontier.isEmpty()){
            final var frontierCell = frontier.remove();

            frontierCell.getLinks()
                .stream()
                .forEach((var linkedCell) -> {
                    if(distances.isDistanceSet(linkedCell)){return;}

                    distances.setDistance(linkedCell, distances.getDistance(frontierCell)+1);
                    frontier.add(linkedCell);
                });
        }

        return distances;
    }

    public List<Cell> getPathFromRoot(Cell cell, CellDistances distancesFromRoot){
        return null;
    }

    public List<Cell> getPathBetween(Cell startCell, Cell endCell, CellDistances distancesFromSomeCell){
        return null;
    }
}
