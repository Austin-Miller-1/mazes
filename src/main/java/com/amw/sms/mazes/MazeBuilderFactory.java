package com.amw.sms.mazes;

import com.amw.sms.algorithms.AlgorithmFactory;
import com.amw.sms.grid.GridFactory;
import com.amw.sms.mazes.goals.MazeGoalBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory class for MazeBuilder. Abstracts out the construction process of MazeBuilder.
 */
@Component
public class MazeBuilderFactory {
    @Autowired
    private GridFactory gridFactory;

    @Autowired
    private MazeGoalBuilderFactory goalBuilderFactory;

    @Autowired
    private AlgorithmFactory algorithmFactory;

    /**
     * Constructs new MazeBuilderFactory
     */
    public MazeBuilderFactory(){}

    /**
     * Creates new MazeBuilder.
     * @return New MazeBuilder instance.
     */
    public MazeBuilder create(){
        return new MazeBuilder(gridFactory, goalBuilderFactory, algorithmFactory);
    }
}
