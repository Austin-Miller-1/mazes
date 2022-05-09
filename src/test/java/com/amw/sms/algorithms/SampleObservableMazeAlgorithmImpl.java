package com.amw.sms.algorithms;

import com.amw.sms.grid.data.GridData;

/**
 * Sample extension of ObservableMazeAlgorithm for testing the abstract class.
 */
public class SampleObservableMazeAlgorithmImpl extends ObservableMazeAlgorithm{
    /**
     * Method for testing getExecutionState.
     * @param gridData Grid data.
     */
    public void setDataInExecutionState(GridData gridData){
        this.executionState.setGridData(gridData);
    }
}
