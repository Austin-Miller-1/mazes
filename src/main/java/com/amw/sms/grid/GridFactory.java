package com.amw.sms.grid;

import org.springframework.stereotype.Component;

/**
 * Factory class to abstract out the Grid construction process. 
 * Construction of Grid should only be done through the factory.
 * TODO - How would I justify this without just saying I'm doing this to allow for mocks?
 */
@Component
public class GridFactory {
    /**
     * Constructs new GridFactory
     */
    public GridFactory(){}

    /**
     * Constructs new Grid instance
     * @param rowCount Number of rows in grid
     * @param colCount Number of cols in grid
     * @return New grid instance with specific number of rows and columns
     */
    public Grid createGrid(int rowCount, int colCount){
        return new Grid(rowCount, colCount);
    }
}
