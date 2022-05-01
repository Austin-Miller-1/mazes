package com.amw.sms.mazes.goals;

import com.amw.sms.algorithms.AlgorithmFactory;
import com.amw.sms.grid.Grid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory class for MazeGoalBuilder. Abstracts out the construction process of MazeGoalBuilder.
 */
@Component
public class MazeGoalBuilderFactory {
    @Autowired
    private AlgorithmFactory algorithmFactory;

    /**
     * Constructs new MazeGoalBuilderFactory
     */
    public MazeGoalBuilderFactory(){}

    /**
     * Constructs new instance of MazeGoalBuilder for the provided grid.
     * @param grid Grid for which the MazeGoalBuilder is for.
     * @return New builder instance.
     */
    public MazeGoalBuilder create(Grid grid){
        return new MazeGoalBuilder(this.algorithmFactory, grid);
    }
}
