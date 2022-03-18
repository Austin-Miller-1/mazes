package com.amw.sms;

import com.amw.sms.algorithms.Dijkstra;
import com.amw.sms.algorithms.generation.Sidewinder;
import com.amw.sms.mazes.InvalidMazeException;
import com.amw.sms.mazes.MazeBuilder;

import ij.IJ;

/**
 * Application class. 
 * Creates a maze using one of the implemented maze-generation algorithms, 
 * prints it to the console, displays it as an image and saves that image
 * to the folder containing the application.
 */
public class App 
{
    public static void main(final String[] args) throws InvalidMazeException
    {
        final var maze = new MazeBuilder()
            .withSize(10, 10)
            .usingLongestPath()
            .usingAlgorithm(new Sidewinder())
            .showDistances()
            .build();

        System.out.println(maze);

        final var solveAlgorithm = new Dijkstra();
        solveAlgorithm.solve(maze);
        System.out.println(maze);

        /* final var mazeImg = grid.toImage("Sidewinder", 30);
        mazeImg.show();
        IJ.save(mazeImg, "maze.tif"); */
    }
}
