package com.amw.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * 
 * The Sidewinder algorithm can be updated to generate a maze towards a specific direction,
 * that is, such that it's biases end up in that direction. These two factors control this directional bias:
 * 1. Whether the algorithm iterates through rows versus columns.
 * 2. The direction it chooses to open the run at when the run is closed.
 * 
 * Iterating via rows will cause a north/south directional bias.
 * Iterating via columns will cause a east/west directional bias.
 * 
 * The second factor determines which of the two directions it goes in.
 * 
 * Another way of changing the bias is simply rotating the grid itself in a certain direction. This would allow
 * for a single implementation of the central algorithm (instead of one that account for all 4 directions), 
 * and instead, handle the directional change via redirection through a grid-rotation algorithm. 
 * 
 */
public class Sidewinder implements MazeGenAlgorithm{
    private final Random rng;
    private final CoinFlip coinFlip;

    public Sidewinder(){
        rng = new Random();
        coinFlip = new CoinFlip(); 
    }

    public final void apply(Grid grid){
        grid.getRows()
        //grid.getColumns() //If we wanted sidewinder to generate a maze in west/east direction
            .stream()
            .forEach(this::visitRow);
    }

    private void visitRow(List<Cell> row){
        final var currentRun = new ArrayList<Cell>();

        row.forEach((var cell) -> {
            currentRun.add(cell);
            final var atEastBound = cell.getEast().isEmpty();
            final var atNorthBound = cell.getNorth().isEmpty();
            
            //Edge-case: if at north-east corner of grid, no options so end 
            if(atEastBound && atNorthBound){
                return;
            }

            //Close run if..
            //1. We can't choose EAST OR
            //2. We can choose EAST or NORTH AND our coin-flip tells us to close it
            final var shouldCloseRun = atEastBound 
                || (!atNorthBound && coinFlip.isHeads());

            if(shouldCloseRun){
                //Link one of the cells in the run to it's northern neighbor
                //Known: All cells in the current run have a northern neighbor
                final var cellFromRun = currentRun.get(rng.nextInt(currentRun.size()));
                cellFromRun.link(cellFromRun.getNorth().get());
                currentRun.clear();
            } else {
                cell.link(cell.getEast().get());
            }
        });
    }

    private void visitColumn(List<Cell> column){
        final var currentRun = new ArrayList<Cell>();

        column.forEach((var cell) -> {
            currentRun.add(cell);
            final var atSouthBound = cell.getSouth().isEmpty();
            final var atWestBound = cell.getWest().isEmpty();
            
            if(atSouthBound && atWestBound){
                return;
            }

            final var shouldCloseRun = atSouthBound 
                || (!atWestBound && coinFlip.isHeads());

            if(shouldCloseRun){
                final var cellFromRun = currentRun.get(rng.nextInt(currentRun.size()));
                cellFromRun.link(cellFromRun.getWest().get());
                currentRun.clear();
            } else {
                cell.link(cell.getNorth().get());
            }
        });
    }
}
