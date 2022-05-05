package com.amw.sms.algorithms;

import com.amw.sms.grid.CellGroup;
import com.amw.sms.grid.CellPath;
import com.amw.sms.grid.griddata.GridData;

/**
 * Sample extension of ObservableMazeAlgorithm for testing the abstract class.
 */
public class SampleObservableMazeAlgorithmImpl extends ObservableMazeAlgorithm{
    /**
     * Method for testing getExecutionState.
     * @param gridData Grid data.
     */
    public void setDataInExecutionState(GridData gridData, CellGroup cellGroup, CellPath path){
        this.executionState.setAlgorithmGridData(gridData);
        this.executionState.addAlgorithmCellGroup(cellGroup);
        this.executionState.addAlgorithmPath(path);
    }
}
