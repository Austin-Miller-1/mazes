package com.amw.sms.grid.data;

import com.amw.sms.grid.Grid;
import com.amw.sms.grid.data.GridData;

/**
 * Sample extension of GridData for testing the abstract class.
 * Since GridData doesn't require any methods to be implemented, this class is empty besides the constructor.
 */
public class SampleGridDataImpl extends GridData {
    /**
     * Constructor.
     * @param grid Grid
     * @see GridData
     */
    public SampleGridDataImpl(Grid grid){
        super(grid);
    }
}
