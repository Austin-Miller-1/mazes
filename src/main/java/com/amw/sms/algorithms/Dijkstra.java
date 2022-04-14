package com.amw.sms.algorithms;

import java.util.LinkedList;
import java.util.List;

import com.amw.sms.algorithms.solving.MazeSolveAlgorithm;
import com.amw.sms.grid.Cell;
import com.amw.sms.grid.Distances;
import com.amw.sms.grid.Grid;
import com.amw.sms.mazes.Maze;

//TODO - upgrade with caching mode to reduce number of calls to algorithm.
public class Dijkstra extends MazeSolveAlgorithm {
    public List<Cell> getSolution(Maze maze){
        if(maze.getEntrances().isEmpty()){
            return new LinkedList<Cell>();
        }
        final var startCell = maze.getStartCell().get();
        final var endCell = maze.getEndCell().get();

        return this.getPath(maze.getGrid(), startCell, endCell, this.getDistances(maze.getGrid(), startCell));
    }

    @Deprecated
    public Distances getDistances(Grid grid, Cell cell){
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
    public List<Cell> getPath(Grid grid, Cell startCell, Cell endCell, Distances distancesFromStart){
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
}
