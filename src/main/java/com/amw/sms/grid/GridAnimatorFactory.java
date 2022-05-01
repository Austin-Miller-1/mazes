package com.amw.sms.grid;

import com.amw.sms.algorithms.ObservableMazeAlgorithm;

import org.springframework.stereotype.Component;

/**
 * Factory class to abstract out the GridAnimator construction process. 
 * Construction of GridAnimator should only be done through the factory.
 */
@Component
public class GridAnimatorFactory {
    /**
     * Constructs new GridAnimatorFactory
     */
    public GridAnimatorFactory(){}

    /**
     * Returns GridAnimator instance.
     * @param grid The grid to create the animation for.
     * @param observedAlgorithm The algorithm to create the animation of.
     * @return Grid animator instance.
     */
    public GridAnimator create(Grid grid, ObservableMazeAlgorithm observedAlgorithm){
        return new GridAnimator(grid, observedAlgorithm);
    }
}
