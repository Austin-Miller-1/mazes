package com.amw.mazes;

import com.amw.mazes.algorithms.generation.Sidewinder;
import com.amw.mazes.grid.Distances;
import com.amw.mazes.grid.DistancesGrid;
import com.amw.mazes.grid.Grid;

import ij.IJ;

/**
 * Application class. 
 * Creates a maze using one of the implemented maze-generation algorithms, 
 * prints it to the console, displays it as an image and saves that image
 * to the folder containing the application.
 */
public class App 
{
    public static void main(final String[] args)
    {
        final var grid = new DistancesGrid(10, 20);
        System.out.println("Empty grid");
        System.out.println(grid);
        
        final var mazeGen = new Sidewinder();
        mazeGen.apply(grid);
        System.out.println("Maze generated via Sidewinder");
        System.out.println(grid);


        final var startCell = grid.getCell(0, 0).get(); 
        final var distances = startCell.getDistances();
        grid.setDistances(distances);
        System.out.println("Maze post Dijkstra");
        System.out.println(grid);

        //TODO refactor to GridContent interface
        final var endCell = grid.getCell(9, 19).get();
        grid.setDistances(new Distances(startCell, endCell, distances));
        System.out.println("Solved maze");
        System.out.println(grid);

        /* final var mazeImg = grid.toImage("Sidewinder", 30);
        mazeImg.show();
        IJ.save(mazeImg, "test.tif"); */
    }
}
