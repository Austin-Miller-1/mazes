package com.amw.sms.algorithms;

import java.util.Optional;

import com.amw.sms.grid.data.GridData;

/**
 * Data object containing a maze algorithm's state during (AND ONLY DURING)
 * its execution on a maze.
 */
public class MazeAlgorithmExecutionState {
    private Optional<GridData> gridData;

    /**
     * Constructs new, empty execution state. State values must be set via setters.
     */
    public MazeAlgorithmExecutionState(){
        this.gridData = Optional.empty();
    }

    /**
     * Set the grid data that the algorithm is using/building during its execution.
     * @param gridData Grid data.
     */
    void setGridData(GridData gridData){
        this.gridData = Optional.of(gridData);
    }
    
    /**
     * Clears the set state values.
     */
    void clear(){
        this.gridData = Optional.empty();
    }

    /**
     * Get the grid data that the algorithm is using/building during its execution.
     * @return Optional containing the set grid-data. If unset, returns empty Optional.
     */
    public Optional<GridData> getGridData(){
        return this.gridData;
    }
}
