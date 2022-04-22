package com.amw.sms.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for Grid.
 */
@ExtendWith(MockitoExtension.class)
public class GridTest {
    @Mock
    private GridData mockGridData;

    @Mock private Cell mockCell1;
    @Mock private Cell mockCell2;
    @Mock private Cell mockCell3;
    @Mock private Cell mockCellOffPath;

    private List<Cell> samplePath;
    private String sampleCellDataContents = "A";

    @BeforeEach
    void beforeEach(){
        samplePath = Arrays.asList(mockCell1, mockCell2, mockCell3);
    }

    @Test
    void testConstructor_usingGetCell_innerCellsAreGivenExpectedNeighbors(){
        final var grid = new Grid(10, 10);

        final var rowPos = 4;
        final var colPos = 5;
        final var cellNeighbors = grid
            .getCell(rowPos, colPos)
            .get()
            .getNeighbors();

        assertEquals(4, cellNeighbors.size());
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos-1, colPos).get()));
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos+1, colPos).get()));
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos, colPos-1).get()));
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos, colPos+1).get()));
    }

    @Test
    void testConstructor_usingGetCell_firstOuterCellIsGivenExpectedNeighbors(){
        final var grid = new Grid(10, 10);

        final var rowPos = 0;
        final var colPos = 0;
        final var cellNeighbors = grid
            .getCell(rowPos, colPos)
            .get()
            .getNeighbors();

        assertEquals(2, cellNeighbors.size());
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos+1, colPos).get()));
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos, colPos+1).get()));
    }

    @Test
    void testConstructor_usingGetCell_lastOuterCellIsGivenExpectedNeighbors(){
        final var grid = new Grid(10, 10);

        final var rowPos = 9;
        final var colPos = 9;
        final var cellNeighbors = grid
            .getCell(rowPos, colPos)
            .get()
            .getNeighbors();

        assertEquals(2, cellNeighbors.size());
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos-1, colPos).get()));
        assertTrue(cellNeighbors.contains(grid.getCell(rowPos, colPos-1).get()));
    }

    @Test
    void testGetCell_whenValidRowAndColumn_returnsExpectedCell(){
        final var grid = new Grid(10, 10);

        final var cell = grid.getCell(4, 6);

        assertTrue(cell.isPresent());
        assertEquals(4, cell.get().getRowPosition());
        assertEquals(6, cell.get().getColumnPosition());
    }

    @Test
    void testGetCell_whenFirstCell_returnsExpectedCell(){
        final var grid = new Grid(10, 10);

        final var cell = grid.getCell(0, 0);

        assertTrue(cell.isPresent());
        assertEquals(0, cell.get().getRowPosition());
        assertEquals(0, cell.get().getColumnPosition());
    }

    @Test
    void testGetCell_whenLastCell_returnsExpectedCell(){
        final var grid = new Grid(10, 10);

        final var cell = grid.getCell(9, 9);

        assertTrue(cell.isPresent());
        assertEquals(9, cell.get().getRowPosition());
        assertEquals(9, cell.get().getColumnPosition());
    }

    @Test
    void testGetCell_whenRowPosNegative_returnsEmptyOptional(){
        final var grid = new Grid(10, 10);

        assertTrue(grid.getCell(-1, 6).isEmpty());
    }

    @Test
    void testGetCell_whenRowPosTooLarge_returnsEmptyOptional(){
        final var grid = new Grid(10, 10);

        assertTrue(grid.getCell(10, 6).isEmpty());
    }

    @Test
    void testGetCell_whenColPosNegative_returnsEmptyOptional(){
        final var grid = new Grid(10, 10);

        assertTrue(grid.getCell(4, -1).isEmpty());
    }

    @Test
    void testGetCell_whenColPosTooLarge_returnsEmptyOptional(){
        final var grid = new Grid(10, 10);

        assertTrue(grid.getCell(4, 10).isEmpty());
    }

    @Test
    void testGetRandomCell_usingGetCells_returnsCellFromGrid(){
        final var grid = new Grid(10, 10);

        assertTrue(grid.getCells().contains(grid.getRandomCell()));
    }


    @Test
    void testGetRandomCell_returnsRandomCellEachTime(){
        //Get 3 random cells. Assert that at least one of them is different ((1/200)^3 chance of failure)
        final var grid = new Grid(10, 20);
        final var cell1 = grid.getRandomCell();
        final var cell2 = grid.getRandomCell();
        final var cell3 = grid.getRandomCell();

        assertTrue(!cell1.equals(cell2) || !cell1.equals(cell3));
    }

    @Test
    void testGetCellCount_returnsCorrectCount(){
        assertEquals(4*15, (new Grid(4, 15)).getCellCount());
        assertEquals(9*7, (new Grid(9, 7)).getCellCount());
    }

    @Test
    void testGetRows_returnsListOfRows(){
        final var grid = new Grid(9, 10);

        final var rows = grid.getRows();

        assertEquals(9, rows.size());
        assertEquals(10, rows.get(0).size());
        assertEquals(grid.getCell(5, 9).get(), rows.get(5).get(9));
    }

    @Test
    void testGetCells_returnsListOfAllCells(){
        final var grid = new Grid(9, 10);

        final var cells = grid.getCells();

        assertEquals(9*10, cells.size());
        assertTrue(cells.contains(grid.getCell(5, 9).get()));
    }

    @Test
    void testGetRowCount_returnsRowCount(){
        final var grid = new Grid(9, 10);
        assertEquals(9, grid.getRowCount());
    }

    @Test
    void testGetColumnCount_returnsColumnCount(){
        final var grid = new Grid(9, 10);
        assertEquals(10, grid.getColumnCount());
    }

    @Test
    void testSetGridData_andGetGridData_setsGridDataAsExpected(){
        final var grid = new Grid(9, 10);
        grid.setGridData(mockGridData);

        assertTrue(grid.getGridData().isPresent());
        assertEquals(mockGridData, grid.getGridData().get());
    }

    @Test
    void testClearGridData_andGetGridData_removesExistingGridDataAsExpected(){
        final var grid = new Grid(9, 10);
        grid.setGridData(mockGridData);
        grid.clearGridData();

        assertTrue(grid.getGridData().isEmpty());
    }


    @Test
    void testClearGridData_whenNoDataExists_doesNotCauseException(){
        final var grid = new Grid(9, 10);
        
        //Call before any data is set - shouldn't cause problems
        grid.clearGridData();
        assertTrue(grid.getGridData().isEmpty());
        
        //Set data
        grid.setGridData(mockGridData);

        //Double clear - second call shouldn't cause problems
        grid.clearGridData();
        grid.clearGridData();
        assertTrue(grid.getGridData().isEmpty());
    }

    @Test
    void testGetGridData_whenNotSet_returnsEmptyOptional(){
        final var grid = new Grid(9, 10);
        assertTrue(grid.getGridData().isEmpty());
    }

    @Test
    void testShowGridData_andGetCellDataDisplayString_whenGridDataNotSet_returnsWhitespace(){
        //Grid fixture
        final var grid = new Grid(9, 10);

        //Test method
        grid.showGridData();

        //Assert
        assertEquals("", grid.getCellDataDisplayString(mockCell1).trim());
    }

    @Test
    void testShowGridData_andGetCellDataDisplayString_whenGridDataSet_cellDataIsDisplayed(){
        //Grid fixture
        final var grid = new Grid(9, 10);
        grid.setGridData(mockGridData);
        Mockito.when(mockGridData.getCellContents(mockCell1))
            .thenReturn(sampleCellDataContents);

        //Test method
        grid.showGridData();

        //Assert
        assertEquals(sampleCellDataContents, grid.getCellDataDisplayString(mockCell1));
    }


    @Test
    void testHideGridData_andGetCellDataDisplayString_whitespaceIsDisplayed(){
        //Grid fixture
        final var grid = new Grid(9, 10);
        grid.setGridData(mockGridData);

        //Test method
        grid.hideGridData();

        //Assert
        assertEquals("", grid.getCellDataDisplayString(mockCell1).trim());
    }

    @Test
    void testHideGridData_whenNoDataSet_doesNotCauseException(){
        final var grid = new Grid(9, 10);
        grid.hideGridData();
        grid.getCellDataDisplayString(mockCell1);
    }

    @Test
    void testHideGridData_whenDataAlreadyHidden_doesNotCauseException(){
        //Grid fixture
        final var grid = new Grid(9, 10);
        grid.setGridData(mockGridData);

        //Test method --> We care about second call
        grid.hideGridData();
        grid.hideGridData();

        //Assert
        grid.getCellDataDisplayString(mockCell1);
    }

    @Test
    void testSetPath_andGetPath_setsPathAsExpected(){
        final var grid = new Grid(9, 10);
        grid.setPath(samplePath);
        assertEquals(samplePath, grid.getPath().get());
    }

    @Test
    void testGetPath_whenNoPathSet_returnsEmptyOptional(){
        final var path = new Grid(9, 10).getPath();
        assertTrue(path.isEmpty());
    }

    @Test
    void testClearPath_andSetPath_andGetPath_clearsGridsPathAsExpected(){
        final var grid = new Grid(9, 10);
        grid.setPath(samplePath);

        grid.clearPath();

        assertTrue(grid.getPath().isEmpty());
    }

    @Test
    void testClearPath_whenNoPathSet_doesNotCauseException(){
        final var grid = new Grid(9, 10);

        //Call before path is set - shouldn't cause problems
        grid.clearPath();

        //Set path
        grid.setPath(samplePath);

        //Double clear, second call shouldn't cause problems
        grid.clearPath();
        grid.clearPath();
    }

    @Test
    void testDisplayPathExclusively_andGetCellDataDisplayString_gridDataOffThePathIsReplacedWithWhitespace(){
        final var grid = new Grid(9, 10);
        grid.setGridData(mockGridData);
        Mockito.lenient()
            .when(mockGridData.getCellContents(any()))
            .thenReturn("AAA");
        grid.setPath(samplePath);

        grid.displayPathExclusively();

        assertEquals("", grid.getCellDataDisplayString(mockCellOffPath).trim());       
    }

    @Test
    void testDisplayPathExclusively_andGetCellDataDisplayString_whenAllGridDataWasBeingDisplayedBefore_gridDataOffThePathIsReplacedWithWhitespace(){
        final var grid = new Grid(9, 10);
        grid.setGridData(mockGridData);
        Mockito.lenient()
            .when(mockGridData.getCellContents(any()))
            .thenReturn("AAA");
        grid.setPath(samplePath);

        //Display all data
        grid.displayAllCells();

        //Change to only display path data
        grid.displayPathExclusively();

        assertEquals("", grid.getCellDataDisplayString(mockCellOffPath).trim());       
    }

    @Test
    void testDisplayPathExclusively_andGetCellDataDisplayString_gridDataOnThePathIsDisplayedAsString(){
        final var grid = new Grid(9, 10);
        grid.setGridData(mockGridData);
        Mockito.when(mockGridData.getCellContents(any()))
            .thenReturn(sampleCellDataContents);
        grid.setPath(samplePath);

        grid.displayPathExclusively();

        assertEquals(sampleCellDataContents, grid.getCellDataDisplayString(samplePath.get(0)));       
    }

    @Test
    void testDisplayAllCells_andGetCellDataDisplayString_gridDataOffThePathIsDisplayedAsString(){
        final var grid = new Grid(9, 10);
        grid.setGridData(mockGridData);
        Mockito.when(mockGridData.getCellContents(any()))
            .thenReturn(sampleCellDataContents);
        grid.setPath(samplePath);

        grid.displayAllCells();

        assertEquals(sampleCellDataContents, grid.getCellDataDisplayString(mockCellOffPath));       
    }

    @Test
    void testDisplayAllCells_andGetCellDataDisplayString_whenPathDataWasDisplayedExclusivelyBefore_gridDataOffThePathIsDisplayedAsString(){
        final var grid = new Grid(9, 10);
        grid.setGridData(mockGridData);
        Mockito.when(mockGridData.getCellContents(any()))
            .thenReturn(sampleCellDataContents);
        grid.setPath(samplePath);

        //Display path exclusively
        grid.displayPathExclusively();

        //Change to start displaying all cells
        grid.displayAllCells();

        assertEquals(sampleCellDataContents, grid.getCellDataDisplayString(mockCellOffPath));       
    }

    @Test
    void testDisplayAllCells_andGetCellDataDisplayString__gridDataOnThePathIsDisplayedAsString(){
        final var grid = new Grid(9, 10);
        grid.setGridData(mockGridData);
        Mockito.when(mockGridData.getCellContents(any()))
            .thenReturn(sampleCellDataContents);
        grid.setPath(samplePath);

        grid.displayAllCells();

        assertEquals(sampleCellDataContents, grid.getCellDataDisplayString(samplePath.get(0)));     
    }

    @Test
    void testShowGridData_andToString_gridDataDisplayedInGridString(){
        //TODO
                // //Grid fixture
                // final var grid = new Grid(9, 10);
                // grid.setGridData(mockGridData);
        
                // //Mock setup - integration with Cell class
                // final var someCell = grid.getFirstCell();
                // final var sampleData = "10";
                // Mockito.when(mockGridData.getCellContents(someCell))
                //     .thenReturn(sampleData);
    }

    @Test
    void testHideGridData_andToString_gridDataNotDisplayedInGridString(){
        //TODO
    }

    
    @Test
    void testDisplayPathOnly_andToString_whenDisplayingPathOnly_gridDataOffThePathIsNotDisplayedInGridString(){
        //TODO

    }

    @Test
    void testDisplayPathOnly_andToString_whenDisplayingPathOnly_gridDataOnThePathIsDisplayedInGridString(){
        //TODO

    }

    @Test
    void testDisplayPathOnly_andToString_whenDisplayingAllGridData_gridDataOffThePathIsDisplayedInGridString(){
        //TODO

    }

    @Test
    void testDisplayPathOnly_andToString_whenDisplayingAllGridData_gridDataOnThePathIsDisplayedInGridString(){
        //TODO

    }
}
