package com.amw.sms.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.amw.sms.grid.CellGroup;
import com.amw.sms.grid.CellPath;
import com.amw.sms.grid.GridData;

/**
 * Data object containing a maze algorithm's state during (AND ONLY DURING)
 * its execution on a maze.
 */
public class MazeAlgorithmExecutionState {
    private Optional<GridData> algorithmGridData;
    private List<CellGroup> algorithmCellGroups;
    private List<CellPath> algorithmPaths;

    /**
     * Constructs new, empty execution state. State values must be set via setters.
     */
    public MazeAlgorithmExecutionState(){
        this.algorithmGridData = Optional.empty();
        this.algorithmCellGroups = new ArrayList<CellGroup>();
        this.algorithmPaths = new ArrayList<CellPath>();
    }

    /**
     * Set the grid data that the algorithm is using/building during its execution.
     * @param gridData Grid data.
     */
    void setAlgorithmGridData(GridData gridData){
        this.algorithmGridData = Optional.of(gridData);
    }

    /**
     * Adds the cell group that the algorithm is using/building during its execution.
     * @param cellGroup Cell group.
     */
    void addAlgorithmCellGroup(CellGroup cellGroup){
        this.algorithmCellGroups.add(cellGroup);
    }

    /**
     * Adds the cell path that the algorithm is using/building during its execution.
     * @param cellPath Cell path.
     */
    void addAlgorithmPath(CellPath cellPath){
        this.algorithmPaths.add(cellPath);
    }

    /**
     * Clears the set state values.
     */
    void clear(){
        this.algorithmGridData = Optional.empty();
        this.algorithmCellGroups = new ArrayList<CellGroup>();
        this.algorithmPaths = new ArrayList<CellPath>();
    }

    /**
     * Get the grid data that the algorithm is using/building during its execution.
     * @return Optional containing the set grid-data. If unset, returns empty Optional.
     */
    public Optional<GridData> getAlgorithmGridData(){
        return this.algorithmGridData;
    }

    /**
     * Returns the cell groups that the algorithm is using/building during its execution.
     * @return List of the cells groups. If no cell group has been added, returns an empty list.
     */
    public List<CellGroup> getAlgorithmCellGroups(){
        return this.algorithmCellGroups;
    }

    /**
     * Returns the cell paths that the algorithm is using/building during its execution.
     * @return List of the path. If no path has been added, returns an empty list.
     */
    public List<CellPath> getAlgorithmPaths(){
        return this.algorithmPaths;
    }
}
