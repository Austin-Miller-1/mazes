package com.amw.sms.algorithms;

/**
 * Interface for all classes that observe an algorithm and react during the different steps as it 
 * is being executed. 
 */
public interface MazeAlgorithmObserver {
    /**
     * To be called when the algorithm under observation has started.
     */
    public void algorithmStarted();

    /**
     * To be called when the algorithm under observation has completed its next step.
     */
    public void algorithmCompletedStep();

    /**
     * To be called when the algorithm under observation has finished.
     */
    public void algorithmFinished();
}
