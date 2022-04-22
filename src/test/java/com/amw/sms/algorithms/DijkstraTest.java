package com.amw.sms.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import com.amw.sms.algorithms.solving.MazeSolveAlgorithm;
import com.amw.sms.algorithms.solving.MazeSolveAlgorithmTest;
import com.amw.sms.grid.Cell;
import com.amw.sms.grid.Grid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DijkstraTest extends MazeSolveAlgorithmTest {

    @Mock
    private Grid mockGrid;

    private Dijkstra dijk;

    @BeforeEach
    void beforeEach(){
        dijk = new Dijkstra();
    }

    /**
     * Get the specific algorithm instance to test.
     * @return Instance of MazeSolveAlgorithm subtype to test.
     */
    @Override
    protected MazeSolveAlgorithm getAlgorithmUnderTest(){
        return dijk;
    }

    @Test
    void testGetDistances_whenNoLinks_returnsDistancesContainingOnlyRootCell(){
        final var cell = new Cell(1, 1);        
        final var distances = dijk.getDistances(mockGrid, cell);

        assertEquals(0, distances.getDistance(cell));
        final var indexedCells = distances.getCells(); 
        assertEquals(1, indexedCells.size());
        assertTrue(indexedCells.contains(cell));
    }

    @Test
    void testGetDistances_whenOneVerticalLink_returnsDistancesWhereLinkedCellHasDistanceOfOne(){
        //Grid of 2 vertically placed cells 
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(2, 1);
        cell1.link(cell2);
        cell1.setNorth(Optional.of(cell2));

        final var distances = dijk.getDistances(mockGrid, cell1);
        assertEquals(1, distances.getDistance(cell2));
    }

    @Test
    void testGetDistances_whenOneHorizontalLink_returnsDistancesWhereLinkedCellHasDistanceOfOne(){
        //Grid of 2 horizontally placed cells 
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(1, 2);
        cell1.link(cell2);
        cell1.setEast(Optional.of(cell2));

        final var distances = dijk.getDistances(mockGrid, cell1);
        assertEquals(1, distances.getDistance(cell2));
    }

    @Test
    void testGetDistances_whenTwoVerticalLinksAndDepthOfTwo_returnsExpectedDistances(){
        //Grid of 3 vertically placed cells 
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(2, 1);
        cell1.link(cell2);
        cell1.setNorth(Optional.of(cell2));

        final var cell3 = new Cell(3, 1);
        cell2.link(cell3);
        cell2.setNorth(Optional.of(cell3));

        final var distances = dijk.getDistances(mockGrid, cell1);
        assertEquals(1, distances.getDistance(cell2));
        assertEquals(2, distances.getDistance(cell3));
    }

    @Test
    void testGetDistances_whenTwoVerticalLinksAndDepthOfOne_returnsExpectedDistances(){
        //Grid of 3 vertically placed cells 
        final var cell1 = new Cell(2, 1);
        final var cell2 = new Cell(3, 1);
        cell1.link(cell2);
        cell1.setNorth(Optional.of(cell2));

        final var cell3 = new Cell(1, 1);
        cell1.link(cell3);
        cell1.setSouth(Optional.of(cell3));

        final var distances = dijk.getDistances(mockGrid, cell1);
        assertEquals(1, distances.getDistance(cell2));
        assertEquals(1, distances.getDistance(cell3));
    }


    @Test
    void testGetDistances_whenTwoHorizontalLinksAndDepthOfTwo_returnsExpectedDistances(){
        //Grid of 3 horizontally placed cells 
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(1, 2);
        cell1.link(cell2);
        cell1.setEast(Optional.of(cell2));

        final var cell3 = new Cell(1, 3);
        cell2.link(cell3);
        cell2.setEast(Optional.of(cell3));

        final var distances = dijk.getDistances(mockGrid, cell1);
        assertEquals(1, distances.getDistance(cell2));
        assertEquals(2, distances.getDistance(cell3));
    }

    @Test
    void testGetDistances_whenTwoHorizontalLinksAndDepthOfOne_returnsExpectedDistances(){
        //Grid of 3 horizontally placed cells 
        final var cell1 = new Cell(1, 2);
        final var cell2 = new Cell(1, 3);
        cell1.link(cell2);
        cell1.setEast(Optional.of(cell2));

        final var cell3 = new Cell(1, 1);
        cell1.link(cell3);
        cell1.setWest(Optional.of(cell3));

        final var distances = dijk.getDistances(mockGrid, cell1);
        assertEquals(1, distances.getDistance(cell2));
        assertEquals(1, distances.getDistance(cell3));
    }

    @Test
    void testGetDistances_whenOneVerticalAndOneHorizontalLink_returnsExpectedDistances(){
        //L-shaped grid of 3 cells 
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(2, 1);
        cell1.link(cell2);
        cell1.setNorth(Optional.of(cell2));

        final var cell3 = new Cell(1, 2);
        cell1.link(cell3);
        cell1.setEast(Optional.of(cell3));

        final var distances = dijk.getDistances(mockGrid, cell1);
        assertEquals(1, distances.getDistance(cell2));
        assertEquals(1, distances.getDistance(cell3));
    }

    @Test
    void testGetDistances_whenMoreComplexGrid_returnsExpectedDistances(){
        //T-shaped grid of 6 cells 
        final var cell1 = new Cell(5, 5);
        final var cell2 = new Cell(4, 5);
        final var cell3 = new Cell(6, 5);
        final var cell4 = new Cell(7, 5);
        final var cell5 = new Cell(7, 4);
        final var cell6 = new Cell(7, 6);

        cell1.link(cell2);
        cell1.setSouth(Optional.of(cell2));

        cell1.link(cell3);
        cell1.setNorth(Optional.of(cell3));

        cell3.link(cell4);
        cell3.setNorth(Optional.of(cell4));

        cell4.link(cell5);
        cell4.setWest(Optional.of(cell5));

        cell4.link(cell6);
        cell4.setEast(Optional.of(cell6));

        final var distances = dijk.getDistances(mockGrid, cell1);
        assertEquals(1, distances.getDistance(cell2));
        assertEquals(1, distances.getDistance(cell3));
        assertEquals(2, distances.getDistance(cell4));
        assertEquals(3, distances.getDistance(cell5));
        assertEquals(3, distances.getDistance(cell6));
    }

    @Test
    void testGetDistances_whenImperfectMaze_returnsSmallestDistancesOnly(){
        //Imperfect maze (loop): Multiple paths to cell3. 
        final var cell1 = new Cell(1, 1);
        final var cell2 = new Cell(1, 2);
        final var cell3 = new Cell(1, 3);
       
        cell1.link(cell2);
        cell1.setNorth(Optional.of(cell2));

        cell2.link(cell3);
        cell2.setNorth(Optional.of(cell3));

        cell1.link(cell3);
        cell1.setSouth(Optional.of(cell3));

        final var distances = dijk.getDistances(mockGrid, cell1);
        assertEquals(1, distances.getDistance(cell2));
        assertEquals(1, distances.getDistance(cell3));
    }


    @Test
    void testGetPathFromRoot(){
        //TODO?
    }

    /**
     *    @Test
    void testPathToGoal_whenNoPath_returnsEmptyList(){
        final var path = distances.pathToGoal(cell1);
        assertTrue(path.isEmpty());
    }

    @Test
    void testPathToGoal_whenTwoCellPath_returnsExpectedPathBetweenTwoCells(){
        root.link(cell1);
        root.setEast(Optional.of(cell1));
        distances.addCell(cell1, 1);

        final var expectedPath = Arrays.asList(root, cell1);
        assertEquals(expectedPath, distances.pathToGoal(cell1));
    }

    @Test
    void testPathToGoal_whenThreeCellPath_returnsExpectedPathBetweenThreeCells(){
        root.link(cell1);
        root.setEast(Optional.of(cell1));

        cell1.link(cell2);
        cell1.setSouth(Optional.of(cell2));

        distances.addCell(cell1, 1);
        distances.addCell(cell2, 2);

        final var expectedPath = Arrays.asList(root, cell1, cell2);
        assertEquals(expectedPath, distances.pathToGoal(cell2));
    }

    @Test
    void testPathToGoal_whenMutliplePathsToSameCell_returnsExpectedSmallestPath(){
        //Create simple square grid, 3 paths to same cell, 1 shorter than others
        final var cell3 = new Cell(4, 4);
      
        root.link(cell1);
        root.setEast(Optional.of(cell1));
        distances.addCell(cell1, 1);

        root.link(cell2);
        root.setEast(Optional.of(cell2));
        distances.addCell(cell2, 1);

        root.link(cell3);
        root.setEast(Optional.of(cell3));
        distances.addCell(cell3, 1);

        cell1.link(cell3);
        cell1.setSouth(Optional.of(cell3));

        cell2.link(cell3);
        cell2.setEast(Optional.of(cell3));

        final var expectedPath = Arrays.asList(root, cell3);
        assertEquals(expectedPath, distances.pathToGoal(cell3));
    }
     */

}
