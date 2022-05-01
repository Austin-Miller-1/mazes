package com.amw.sms.algorithms.solving;

import com.amw.sms.algorithms.MazeAlgorithmObserver;
import com.amw.sms.algorithms.ObservableMazeAlgorithm;

/**
 * Abstract class for all classes that observe an algorithm and react during the different steps as it 
 * is being executed, but only for a single time. The algorithm's first execution after being attached is the only
 * one that is relevant to the observer. After this run-through of the algorithm, the observer detatches itself. 
 */
public abstract class OneTimeMazeAlgorithmObserver implements MazeAlgorithmObserver {
    protected final ObservableMazeAlgorithm observedAlgorithm;
    private boolean hasAlgorithmFinished;

    /**
     * Constructs new OneTimeAlgorithmObserver. Observer is not auto-attached to algorithm on construction.
     * @param observedAlgorithm Algorithm to be observed.
     */
    public OneTimeMazeAlgorithmObserver(ObservableMazeAlgorithm observedAlgorithm){
        this.observedAlgorithm = observedAlgorithm;
        this.hasAlgorithmFinished = false;
    }

    /**
     * {@inheritDoc}
     * Since the current execution of the algorithm was the only one the observer was interested
     * in, it detaches itself from the algorithm when this method is called.
     * 
     * This is a template method that calls {@link OneTimeMazeAlgorithmObserver#onAlgorithmFinish} method,
     * which must be implemented by subclasses.
     */
    @Override
    public final void algorithmFinished(){
        this.onAlgorithmFinish();
        this.hasAlgorithmFinished = true;
        this.observedAlgorithm.detach(this);
    }

    /**
     * Indicates where the algorithm being observed has completed its execution.
     * @returnReturns true if the algorithm being observed has completed its execution. False otherwise.
     */
    public final boolean hasAlgorithmFinished(){
        return this.hasAlgorithmFinished;
    }

    /**
     * Abstract method called as part of the template method {@link OneTimeMazeAlgorithmObserver#algorithmFinished}.
     * Subclasses must implement this with any logic that must be completed when the algorithm has finished.
     * Note that subclasses do not need to detach the observer as this is handled by this class itself.
     */
    public abstract void onAlgorithmFinish();
}
