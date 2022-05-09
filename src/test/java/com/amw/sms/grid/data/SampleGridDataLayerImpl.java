package com.amw.sms.grid.data;

import java.awt.Color;
import java.util.Optional;

import com.amw.sms.grid.Cell;

/**
 * Sample implementation of the GridDataLayer class for testing.
 */
public class SampleGridDataLayerImpl extends GridDataLayer {
    private Optional<String> returnValueForDoGetCellContents = Optional.empty();
    private Optional<Color> returnValueForDoGetCellColor = Optional.empty();
    private boolean returnValueForIsCellSet = false;

    /**
     * Constucts instance. Only calls super-class constructor.
     * @param name 
     * @param layerColor
     */
    public SampleGridDataLayerImpl(final String name, final Color layerColor){
        super(name, layerColor);
    }

    /**
     * Constucts instance. Only calls super-class constructor.
     * @param name
     */
    public SampleGridDataLayerImpl(final String name){
        super(name);
    }

    /**
     * Sets the return value for the doGetCellContents abstract method. For testing
     * the template method, getCellContents.
     * @param returnValue Return value for method.
     */
    public void setReturnForDoGetCellContents(final Optional<String> returnValue){
        this.returnValueForDoGetCellContents = returnValue;
    }

    /**
     * Implementation of abtract method. Returns the value set by above setter.
     * @param cell Cell
     * @return value set by test
     */
    Optional<String> doGetCellContents(final Cell cell){
        return this.returnValueForDoGetCellContents;
    }

     /**
     * Sets the return value for the doGetCellColor abstract method. For testing
     * the template method, getCellColor.
     * @param returnValue Return value for method.
     */
    public void setReturnForDoGetCellColors(final Optional<Color> returnValue){
        this.returnValueForDoGetCellColor = returnValue;
    }

    /**
     * Implementation of abtract method. Returns the value set by above setter.
     * @param cell Cell
     * @return value set by test
     */
    Optional<Color> doGetCellColor(final Cell cell){
        return this.returnValueForDoGetCellColor;
    }

    /**
     * Sets the return value for the isCellSet abstract method. For testing
     * the template method, isCellHighlighted
     * @param returnValue Return value for method.
     */
    public void setReturnForIsCellSet(final boolean returnValue){
        this.returnValueForIsCellSet = returnValue;
    }

    /**
     * Implementation of abtract method. Returns the value set by above setter.
     * @param cell Cell
     * @return value set by test
     */
    public boolean isCellSet(final Cell cell){
        return this.returnValueForIsCellSet;
    }
}
