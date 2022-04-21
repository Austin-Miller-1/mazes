package com.amw.sms.algorithms.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.amw.sms.grid.Cell;
import com.amw.sms.grid.Grid;

import org.springframework.stereotype.Component;

/**
 * Binary tree maze-generation algorithm.
 */
@Component
public class BinaryTree implements MazeGenAlgorithm{
    private final Random rng;

    public BinaryTree(){
        rng = new Random();
    }

    public final void apply(Grid grid){
        grid.getCells()
            .stream()
            .forEach(this::visitCell);
    }

    private void visitCell(Cell cell){
        this.createPath(cell, this.getRelevantNeighbors(cell));
    }

    private List<Cell> getRelevantNeighbors(Cell cell){
        final var relevantNeighbors = new ArrayList<Cell>();
        cell.getNorth().ifPresent(relevantNeighbors::add);
        cell.getEast().ifPresent(relevantNeighbors::add);
        return relevantNeighbors;
    }

    private void createPath(Cell cell, List<Cell> neighbors){
        //No neighbors to link to
        if(neighbors.isEmpty()){
            return;
        }

        //Link to one of the neighbors, equal chance of any of the relevant neighbors
        neighbors
            .get(rng.nextInt(neighbors.size()))
            .link(cell);
    }
}
