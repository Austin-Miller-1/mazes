package com.amw.sms.algorithms;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class for all algorithms that are to be made observable 
 */
public abstract class ObservableMazeAlgorithm {
    private final Set<MazeAlgorithmObserver> observers;
    protected final MazeAlgorithmExecutionState executionState;

    /**
     * Constructs new ObservableAlgorithm. 
     */
    protected ObservableMazeAlgorithm(){
        this.observers = new HashSet<MazeAlgorithmObserver>();
        this.executionState = new MazeAlgorithmExecutionState();
    }

    /**
     * Attaches observer to be notified when algorithm runs.
     * @param observer Observer to be notified.
     */
    public final void attach(MazeAlgorithmObserver observer){
        this.observers.add(observer);
    }

    /**
     * Detaches observer such that it is no longer notified when the algorithm runs.
     * @param observer Observer to be detached and no longer notified. If the observer is not already attached,
     * nothing happens.
     */
    public final void detach(MazeAlgorithmObserver observer){
        this.observers.remove(observer);
    }

    /**
     * Method to invoke on start of the algorithm. 
     * Notifies all observers that the algorithm has started.
     */
    protected final void started(){
        this.observers.forEach(MazeAlgorithmObserver::algorithmStarted);
    }

    /**
     * Method to invoke whenever a step of the algorithm has been completed.
     * Notifies all observers that the algorithm has completed its next significant step. 
     * A step is defined as a singular action that has changed the state of the algorithm's internal workings 
     * or the data of the object that the algorithmn is working on. Any step that causes no state change is not notified.
     * If such a notification is necessary, this method must be updated to provide such information to the observers.
     */
    protected final void completedStep(){
        this.observers.forEach(MazeAlgorithmObserver::algorithmCompletedStep);
    }

    /**
     * Method to invoke when the algorithm has finished its execution.
     * Notifies all observers that the algorithm has finished.
     * Also clears any execution-state that was being maintained.
     */
    protected final void finished(){
        this.observers.forEach(MazeAlgorithmObserver::algorithmFinished);
        this.executionState.clear();
    }

    /**
     * Returns the execution state of the maze algorithm. 
     * State must be managed by algorithm subclass to offer any value. 
     * @return The execution state.
     */
    public final MazeAlgorithmExecutionState getExecutionState(){
        return this.executionState;
    }
}
