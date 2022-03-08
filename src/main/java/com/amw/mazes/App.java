package com.amw.mazes;

import ij.IJ;

/**
 * Hello world!
 *
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
