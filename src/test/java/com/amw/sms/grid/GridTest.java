package com.amw.sms.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.amw.sms.grid.griddata.GridData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ij.ImagePlus;

/**
 * Tests for Grid.
 */
@ExtendWith(MockitoExtension.class)
public class GridTest {
    @Mock private GridData mockGridData1;
    @Mock private GridData mockGridData2;

    @Mock private Cell mockCell1;
    @Mock private Cell mockCell2;
    @Mock private Cell mockCell3;
    @Mock private Cell mockCellOffPath;
    
    @Mock 
    private Grid mockGrid;

    @Mock 
    private Color mockColor; 

    private List<Cell> samplePath;
    private String sampleCellDataContents = "A";

    //Sample data for images
    private int sampleCellSize = 30;
    private Color sampleColor1 = new Color(0x123456);
    private Color sampleColor2 = new Color(0xFEDCBA);

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
        grid.setGridData(mockGridData1);

        assertEquals(mockGridData1, grid.getGridData());
    }

    @Test
    void testClearGridData_andGetGridData_removesExistingGridDataAsExpected(){
        final var grid = new Grid(9, 10);

        final var sampleGridData = new SimpleGridData(grid);
        sampleGridData.setCellContents(mockCell1, sampleCellDataContents);
        sampleGridData.setCellColor(mockCell1, mockColor);
        grid.setGridData(mockGridData1);
        
        grid.clearGridData();
        assertTrue(grid.getGridData().getCellContentsDEP(mockCell1).isBlank());
        assertNotEquals(mockColor, grid.getGridData().getCellColorDEP(mockCell1));
    }


    @Test
    void testClearGridData_whenNoDataExists_doesNotCauseException(){
        final var grid = new Grid(9, 10);
        
        //Call before any data is set - shouldn't cause problems
        grid.clearGridData();
        
        //Set data
        final var sampleGridData = new SimpleGridData(grid);
        sampleGridData.setCellContents(mockCell1, sampleCellDataContents);
        grid.setGridData(sampleGridData);

        //Double clear - second call shouldn't cause problems
        grid.clearGridData();
        grid.clearGridData();
        assertTrue(grid.getGridData().getCellContentsDEP(mockCell1).isBlank());
    }

    @Test
    void testGetGridData_whenNotSet_returnsEmptyGridDataInstance(){
        final var grid = new Grid(5, 5);
        grid.getCells()
            .stream()
            .forEach((var cell) -> {
                //TODO
                //Not the best way of checking to make sure data is empty.. For true check, would need to check getCellColor too, and any other cell elements.. 
                //OR would need new isEmpty method OR isSet(Cell) method, and both would need to consider.. if one data element is set, but others are not, is the Cell set or not?
                //This should be resolved once GridData is reorganized into GridData and GridDataLayer classes - future design change
                assertTrue(grid.getGridData().getCellContentsDEP(cell).isBlank());
            });
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
        grid.setGridData(mockGridData1);
        Mockito.when(mockGridData1.getCellContentsDEP(mockCell1))
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
        grid.setGridData(mockGridData1);

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
        grid.setGridData(mockGridData1);

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
        grid.setGridData(mockGridData1);
        Mockito.lenient()
            .when(mockGridData1.getCellContentsDEP(any()))
            .thenReturn("AAA");
        grid.setPath(samplePath);

        grid.displayPathExclusively();

        assertEquals("", grid.getCellDataDisplayString(mockCellOffPath).trim());       
    }

    @Test
    void testDisplayPathExclusively_andGetCellDataDisplayString_whenAllGridDataWasBeingDisplayedBefore_gridDataOffThePathIsReplacedWithWhitespace(){
        final var grid = new Grid(9, 10);
        grid.setGridData(mockGridData1);
        Mockito.lenient()
            .when(mockGridData1.getCellContentsDEP(any()))
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
        grid.setGridData(mockGridData1);
        Mockito.when(mockGridData1.getCellContentsDEP(any()))
            .thenReturn(sampleCellDataContents);
        grid.setPath(samplePath);

        grid.displayPathExclusively();

        assertEquals(sampleCellDataContents, grid.getCellDataDisplayString(samplePath.get(0)));       
    }

    @Test
    void testDisplayAllCells_andGetCellDataDisplayString_gridDataOffThePathIsDisplayedAsString(){
        final var grid = new Grid(9, 10);
        grid.setGridData(mockGridData1);
        Mockito.when(mockGridData1.getCellContentsDEP(any()))
            .thenReturn(sampleCellDataContents);
        grid.setPath(samplePath);

        grid.displayAllCells();

        assertEquals(sampleCellDataContents, grid.getCellDataDisplayString(mockCellOffPath));       
    }

    @Test
    void testDisplayAllCells_andGetCellDataDisplayString_whenPathDataWasDisplayedExclusivelyBefore_gridDataOffThePathIsDisplayedAsString(){
        final var grid = new Grid(9, 10);
        grid.setGridData(mockGridData1);
        Mockito.when(mockGridData1.getCellContentsDEP(any()))
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
        grid.setGridData(mockGridData1);
        Mockito.when(mockGridData1.getCellContentsDEP(any()))
            .thenReturn(sampleCellDataContents);
        grid.setPath(samplePath);

        grid.displayAllCells();

        assertEquals(sampleCellDataContents, grid.getCellDataDisplayString(samplePath.get(0)));     
    }

    @Test
    void testGetCellDataDisplayString_whenGivenGridDataParameter_returnsContentsFromProvidedGridData(){
        final var grid = new Grid(9, 10);
       
        //Mocked grid data
        grid.setGridData(mockGridData1);
        Mockito.lenient()
            .when(mockGridData2.getCellContentsDEP(any()))
            .thenReturn("test");

        //Mock to be provided as parameter
        Mockito.when(mockGridData2.getCellContentsDEP(any()))
            .thenReturn(sampleCellDataContents);

        assertEquals(sampleCellDataContents, grid.getCellDataDisplayString(mockCell1, mockGridData2));       
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

    /**
     * Asserts that pixel in the image at the specified coordinates is of the expected color.
     * @param expectedColor Color that the pixel is expected of being.
     * @param image Image to check.
     * @param x x-coordinate of the pixel.
     * @param y y-coordinate of the pixel.
     */
    private void assertColorAtPixel(Color expectedColor, ImagePlus image, int x, int y){
        final var actualCellPixel = image.getPixel(x, y);
        assertEquals(expectedColor.getRed(), actualCellPixel[0]);     //Red
        assertEquals(expectedColor.getGreen(), actualCellPixel[1]);   //Green
        assertEquals(expectedColor.getBlue(), actualCellPixel[2]);    //Blue
    }

    @Test
    void testToImage_whenOneCellMaze_returnsAnImageWithCellUsingTheExpectedBackgroundColor(){
        final var grid = new Grid(1, 1);
        
        final var gridData = new SimpleGridData(grid);
        gridData.setCellColor(grid.getFirstCell(), sampleColor1);
        grid.setGridData(gridData);

        final var image = grid.toImage("maze", sampleCellSize);
        
        final var centerCoord = Grid.IMAGE_GRID_OFFSET + sampleCellSize/2;
        assertColorAtPixel(sampleColor1, image, centerCoord, centerCoord);
    }

    @Test
    void testToImage_whenOneCellMaze_returnsAnImageWhereMazeHasEmptySpaceOnAllSides(){
        final var grid = new Grid(1, 1);
        
        final var gridData = new SimpleGridData(grid);
        gridData.setCellColor(grid.getFirstCell(), sampleColor1);
        grid.setGridData(gridData);

        final var image = grid.toImage("maze", sampleCellSize);

        final var xyCenterCoord = Grid.IMAGE_GRID_OFFSET + sampleCellSize/2;
        assertColorAtPixel(Grid.IMAGE_BACKGROUND_COLOR, image, Grid.IMAGE_GRID_OFFSET - 5, xyCenterCoord);                  //Left (5 pixels from left wall)
        assertColorAtPixel(Grid.IMAGE_BACKGROUND_COLOR, image, Grid.IMAGE_GRID_OFFSET + sampleCellSize + 5, xyCenterCoord); //Right (5 pixels from right wall)
        assertColorAtPixel(Grid.IMAGE_BACKGROUND_COLOR, image, xyCenterCoord, Grid.IMAGE_GRID_OFFSET - 5);                  //Above
        assertColorAtPixel(Grid.IMAGE_BACKGROUND_COLOR, image, xyCenterCoord, Grid.IMAGE_GRID_OFFSET + sampleCellSize + 5); //Below
    }

    @Test
    void testToImage_whenOneCell_returnsAnImageWithTheExpectedOuterWalls(){
        final var grid = new Grid(1, 1);

        final var gridData = new SimpleGridData(grid);
        gridData.setCellColor(grid.getFirstCell(), sampleColor1);
        grid.setGridData(gridData);

        final var image = grid.toImage("maze", sampleCellSize);

        final var xyCenterCoord = Grid.IMAGE_GRID_OFFSET + sampleCellSize/2;
        assertColorAtPixel(Grid.IMAGE_WALL_COLOR, image, Grid.IMAGE_GRID_OFFSET, xyCenterCoord);                  //Left wall
        assertColorAtPixel(Grid.IMAGE_WALL_COLOR, image, Grid.IMAGE_GRID_OFFSET + sampleCellSize, xyCenterCoord); //Right wall
        assertColorAtPixel(Grid.IMAGE_WALL_COLOR, image, xyCenterCoord, Grid.IMAGE_GRID_OFFSET);                  //Top wall
        assertColorAtPixel(Grid.IMAGE_WALL_COLOR, image, xyCenterCoord, Grid.IMAGE_GRID_OFFSET + sampleCellSize); //Bottom wall
    }

    @Test
    void testToImage_whenTenByTenMaze_returnsAnImageWhereMazeHasEmptySpaceOnAllSides(){
        final var grid = new Grid(10, 10);
        final var mazeWidth = sampleCellSize*10;
        final var mazeHeight = sampleCellSize*10;
 
        //Color all cells.
        final var gridData = new SimpleGridData(grid);
        grid.getCells()
            .stream()
            .forEach((var cell) -> {
                gridData.setCellColor(cell, sampleColor1);
            });
        grid.setGridData(gridData);

        final var image = grid.toImage("maze", sampleCellSize);

        assertColorAtPixel(Grid.IMAGE_BACKGROUND_COLOR, image, Grid.IMAGE_GRID_OFFSET - 5, Grid.IMAGE_GRID_OFFSET + mazeHeight/2);
        assertColorAtPixel(Grid.IMAGE_BACKGROUND_COLOR, image, Grid.IMAGE_GRID_OFFSET + mazeWidth + 5, Grid.IMAGE_GRID_OFFSET + mazeHeight/2);
        assertColorAtPixel(Grid.IMAGE_BACKGROUND_COLOR, image, Grid.IMAGE_GRID_OFFSET + mazeWidth/2, Grid.IMAGE_GRID_OFFSET - 5);
        assertColorAtPixel(Grid.IMAGE_BACKGROUND_COLOR, image, Grid.IMAGE_GRID_OFFSET + mazeWidth/2, Grid.IMAGE_GRID_OFFSET + mazeHeight + 5);
    }


    @Test
    void testToImage_whenGridHasFourCells_returnsAnImageWithTheFourCellsPositionedAsExpected(){
        final var grid = new Grid(2, 2);
        final var mazeWidth = sampleCellSize*2;
        final var mazeHeight = sampleCellSize*2;
    
        final var cell1Color = new Color(0x100000);
        final var cell2Color = new Color(0x200000);
        final var cell3Color = new Color(0x300000);
        final var cell4Color = new Color(0x400000);

        final var gridData = new SimpleGridData(grid);
        gridData.setCellColor(grid.getCell(0, 0).get(), cell1Color);
        gridData.setCellColor(grid.getCell(0, 1).get(), cell2Color);
        gridData.setCellColor(grid.getCell(1, 0).get(), cell3Color);
        gridData.setCellColor(grid.getCell(1, 1).get(), cell4Color);
        grid.setGridData(gridData);

        final var image = grid.toImage("maze", sampleCellSize);

        assertColorAtPixel(cell1Color, image, Grid.IMAGE_GRID_OFFSET + mazeWidth/4, Grid.IMAGE_GRID_OFFSET + mazeHeight/4);
        assertColorAtPixel(cell2Color, image, Grid.IMAGE_GRID_OFFSET + mazeWidth*3/4, Grid.IMAGE_GRID_OFFSET + mazeHeight/4);
        assertColorAtPixel(cell3Color, image, Grid.IMAGE_GRID_OFFSET + mazeWidth/4, Grid.IMAGE_GRID_OFFSET + mazeHeight*3/4);
        assertColorAtPixel(cell4Color, image, Grid.IMAGE_GRID_OFFSET + mazeWidth*3/4, Grid.IMAGE_GRID_OFFSET + mazeHeight*3/4);
    }

    @Test
    void testToImage_whenTwoByTwoMaze_returnsAnImageWithTheExpectedOuterWalls(){
        final var grid = new Grid(2, 2);
        final var mazeWidth = sampleCellSize*2;
        final var mazeHeight = sampleCellSize*2;

        final var gridData = new SimpleGridData(grid);
        gridData.setCellColor(grid.getFirstCell(), sampleColor1);
        grid.setGridData(gridData);

        final var image = grid.toImage("maze", sampleCellSize);

        //TODO, check for 8 walls
        // Iterate over collection of expected coords 

        final var xyCenterCoord = Grid.IMAGE_GRID_OFFSET + sampleCellSize/2;
        assertColorAtPixel(Grid.IMAGE_WALL_COLOR, image, Grid.IMAGE_GRID_OFFSET, sampleCellSize+5);               //Left wall
        assertColorAtPixel(Grid.IMAGE_WALL_COLOR, image, Grid.IMAGE_GRID_OFFSET, sampleCellSize+5);               //Right wall
        assertColorAtPixel(Grid.IMAGE_WALL_COLOR, image, xyCenterCoord, Grid.IMAGE_GRID_OFFSET);                  //Top wall
        assertColorAtPixel(Grid.IMAGE_WALL_COLOR, image, xyCenterCoord, Grid.IMAGE_GRID_OFFSET + sampleCellSize); //Bottom wall
    }

    @Test
    void testToImage_whenGridHasFourLinkedCells_returnsAnImageWithTheExpectedWallsInPlaceWithinMaze(){
        final var grid = new Grid(2, 2);

        //Link top-left to top-right
        grid.getCell(0, 0)
            .get()
            .link(grid.getCell(0, 1).get());

        //Link top-left to bottom-left
        grid.getCell(0, 0)
            .get()
            .link(grid.getCell(1, 0).get());

        final var image = grid.toImage("maze", sampleCellSize);

        //Assert no wall between top-left and bottom-left
        assertColorAtPixel(Grid.IMAGE_BACKGROUND_COLOR, image, Grid.IMAGE_GRID_OFFSET + sampleCellSize/2, Grid.IMAGE_GRID_OFFSET + sampleCellSize);

        //Assert no wall between top-left and top-right
        assertColorAtPixel(Grid.IMAGE_BACKGROUND_COLOR, image, Grid.IMAGE_GRID_OFFSET + sampleCellSize, Grid.IMAGE_GRID_OFFSET + sampleCellSize/2);

        //Assert wall between bottom-left and bottom-right
        assertColorAtPixel(Grid.IMAGE_WALL_COLOR, image, Grid.IMAGE_GRID_OFFSET + sampleCellSize, Grid.IMAGE_GRID_OFFSET + sampleCellSize*3/2);

        //Assert wall between top-right and bottom-right
        assertColorAtPixel(Grid.IMAGE_WALL_COLOR, image, Grid.IMAGE_GRID_OFFSET + sampleCellSize*3/2, Grid.IMAGE_GRID_OFFSET + sampleCellSize);
    }

    @Test
    void testToImage_whenGivenGridData_returnsAnImageUsingProvidedGridData(){
        final var grid = new Grid(1, 1);
        
        //Grid data set within grid
        final var setGridData = new SimpleGridData(grid);
        setGridData.setCellColor(grid.getFirstCell(), sampleColor1);
        grid.setGridData(setGridData);

        //Grid data to be passed to grid as
        final var passedGridData = new SimpleGridData(grid);
        passedGridData.setCellColor(grid.getFirstCell(), sampleColor2);

        //Test method
        final var image = grid.toImage("maze", sampleCellSize, passedGridData);

        //Assert
        final var centerCoord = Grid.IMAGE_GRID_OFFSET + sampleCellSize/2;
        assertColorAtPixel(sampleColor2, image, centerCoord, centerCoord);
    }
}
