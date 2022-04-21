package com.amw.sms.algorithms;

import com.amw.sms.algorithms.generation.BinaryTree;
import com.amw.sms.algorithms.generation.MazeGenAlgorithm;
import com.amw.sms.algorithms.generation.MazeGenAlgorithmType;
import com.amw.sms.algorithms.generation.Sidewinder;
import com.amw.sms.algorithms.solving.MazeSolveAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory for maze algorithms. Includes creation methods for both generation and solving algorithms. 
 */
@Component
public class AlgorithmFactory {
    @Autowired
    private BinaryTree binaryTree;

    @Autowired
    private Sidewinder sidewinder;

    @Autowired
    private Dijkstra dijk;

    /**
     * Returns some maze-generation algorithm.  
     * @return A maze-generation algorithm. The exact algorithm returned is not specified. 
     */
    public MazeGenAlgorithm getGenerationAlgorithm(){
        return this.getGenerationAlgorithm(MazeGenAlgorithmType.SIDEWINDER);
    }

    /**
     * Returns the specified maze-generation algorithm.
     * @param type The exact type of the algorithm.
     * @return The maze-generation algorithm.
     */
    public MazeGenAlgorithm getGenerationAlgorithm(MazeGenAlgorithmType type){
        return switch(type){
            case BINARY_TREE -> binaryTree;
            case SIDEWINDER -> sidewinder;
            default -> sidewinder;  //TODO - should throw exception as invalid type is provided...
        };
    }

    /**
     * Returns some maze-solving algorithm.  
     * @return A maze-solving algorithm. The exact algorithm returned is not specified. 
     */
    public MazeSolveAlgorithm getSolvingAlgorithm(){
        return dijk;
    }


    /**
     * Returns Dijkstra algorithm instance.
     * @return Dijkstra algorithm.
     */
    public Dijkstra getDijkstra(){
        return dijk;
    }
}
