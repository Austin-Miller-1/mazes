package com.amw.mazes;

import com.amw.mazes.algorithms.generation.Sidewinder;
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
    public static void main( String[] args )
    {
        var grid = new Grid(10, 20);
        System.out.println(grid);
        
        var mazeGen = new Sidewinder();
        mazeGen.apply(grid);
        System.out.println(grid);

        var mazeImg = grid.toImage("Sidewinder", 30);
        mazeImg.show();
        IJ.save(mazeImg, "test.tif");
    }
}
