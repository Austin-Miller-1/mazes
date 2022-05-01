package com.amw.sms.algorithms;

import com.amw.sms.algorithms.solving.OneTimeMazeAlgorithmObserver;

/**
 * Sample extension of OneTimeMazeAlgorithmObserver for testing the abstract class.
 */
public class SampleOneTimeMazeAlgorithmObserverImpl extends OneTimeMazeAlgorithmObserver {
    private boolean wasOnAlgorithmFinishCalled = false;
    
    /**
     * Constructor.
     * @param observedAlgorithm Observed algorithm
     */
    public SampleOneTimeMazeAlgorithmObserverImpl(ObservableMazeAlgorithm observedAlgorithm) {
        super(observedAlgorithm);
    }

    //Required implementations
    @Override public void algorithmStarted() {}
    @Override public void algorithmCompletedStep(){}

    /**
     * Sample implementation of onAlgorithmFinish. After being called, the wasOnAlgorithmFinishCalled method 
     * returns true.
     */
    @Override
    public void onAlgorithmFinish() {
        this.wasOnAlgorithmFinishCalled = true;
    }

    /**
     * Method for testing the class' usage of the onAlgorithmFinish method.
     * @return boolean indicating whether the onAlgorithmFinish method was called.
     */
    boolean wasOnAlgorithmFinishCalled(){
        return this.wasOnAlgorithmFinishCalled;
    }
}
