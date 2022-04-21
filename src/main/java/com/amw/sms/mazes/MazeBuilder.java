package com.amw.sms.mazes;

import java.util.AbstractMap;

import com.amw.sms.algorithms.AlgorithmFactory;
import com.amw.sms.algorithms.Dijkstra;
import com.amw.sms.algorithms.generation.MazeGenAlgorithm;
import com.amw.sms.algorithms.generation.Sidewinder;
import com.amw.sms.grid.Cell;
import com.amw.sms.grid.Grid;
import com.amw.sms.grid.GridFactory;
import com.amw.sms.mazes.goals.MazeGoal;
import com.amw.sms.mazes.goals.MazeGoalBuilder;
import com.amw.sms.mazes.goals.MazeGoalBuilderFactory;

public class MazeBuilder {
    private final static String INVALID_SIZE_MESSAGE = "Maze cannot be created with %s %s";
    private final static String NO_GRID_SIZE = "No grid size specified.";

    private final GridFactory gridFactory;
    private final MazeGoalBuilderFactory goalBuilderFactory;
    private final AlgorithmFactory algorithmFactory;

    private int rowCount = 0, colCount = 0;
    private int startRow = 0, startColumn = 0; //TODO do we use defaults still? We shouldn't leave them uninitialized but we also will never use the default values...
    private int endRow = 0, endColumn = 0;
    private MazeGenAlgorithm genAlgorithm;
    private boolean startAtFirst = true;
    private boolean endAtLast = true;
    private boolean useLongestPath = false;
    private boolean useRandomStart = false;
    private boolean useRandomEnd = false;
    private boolean showDistances = false;

    /**
     * Constructs new MazeBuilder.
     * @param gridFactory Grid factory to be used by builder.
     * @param goalBuilderFactory Maze-goal builder factory to be used by builder.
     * @param algorithmFactory Algorithm factory to be used by builder.
     */
    public MazeBuilder(GridFactory gridFactory, MazeGoalBuilderFactory goalBuilderFactory, AlgorithmFactory algorithmFactory){
        this.gridFactory = gridFactory;
        this.goalBuilderFactory = goalBuilderFactory;
        this.algorithmFactory = algorithmFactory;
        genAlgorithm = algorithmFactory.getGenerationAlgorithm();
    }

    /**
     * Set size of maze.
     * @param rowCount Number of rows the maze will have.
     * @param colCount Number of columns the maze will have.
     * @return Builder instance
     * @throws InvalidMazeException When row count or column count are either invalid (less than or equal to 0).
     */
    public MazeBuilder withSize(int rowCount, int colCount) throws InvalidMazeException{
        if(rowCount <= 0) throw new InvalidMazeException(INVALID_SIZE_MESSAGE.formatted(rowCount, "rows"));
        if(colCount <= 0) throw new InvalidMazeException(INVALID_SIZE_MESSAGE.formatted(colCount, "columns"));

        this.rowCount = rowCount;
        this.colCount = colCount;
        return this;
    }

    /**
     * Set the maze-generation algorithm to be used during the build process. This is used to generate the
     * paths of the maze.
     * @param genAlgorithm Maze generation algorithm to be used.
     * @return Builder instance
     */
    public MazeBuilder usingAlgorithm(MazeGenAlgorithm genAlgorithm){
        this.genAlgorithm = genAlgorithm;
        return this;
    }

    /**
     * Sets starting point of maze.
     * @param row Row of entrance.
     * @param column Column of entrance.
     * @return Builder instance.
     */
    public MazeBuilder startFrom(int row, int column){
        this.startAtFirst = false;

        this.startRow = row;
        this.startColumn = column;
        return this;
    }

    /**
     * Sets ending point of maze.
     * @param row Row of exit.
     * @param column Column of exit.
     * @return Builder instance.
     */
    public MazeBuilder endAt(int row, int column){
        this.endAtLast = false;

        this.endRow = row;
        this.endColumn = column;
        return this;
    }

    /**
     * Sets entrance and exit in maze such that the longest path (or at least, a quite long path)
     * within the maze is used.
     * @return Builder instance.
     */
    public MazeBuilder usingLongestPath(){
        this.startAtFirst = false;
        this.endAtLast = false;

        this.useLongestPath = true;
        return this;
    }

    /**
     * Sets starting point of maze to a random cell.
     * @return Builder instance
     */
    public MazeBuilder usingRandomStart(){
        this.startAtFirst = false;
        this.useRandomStart = true;
        return this;
    }

    /**
     * Sets end point of maze to a random cell.
     * @return Builder instance
     */
    public MazeBuilder usingRandomEnd(){
        this.endAtLast = false;
        this.useRandomEnd = true;
        return this;
    }

    /**
     * TODO remove
     * @return
     */
    public MazeBuilder showDistances(){
        this.showDistances = true;
        return this;
    }

    /**
     * Builds the Maze based on the internal state set by client using builder methods.
     * @return Fully constructed maze.
     * @throws InvalidMazeException When grid size is not set, or when the entrance or exits are invalid. 
     */
    public Maze build() throws InvalidMazeException{
        //Validations
        if(this.rowCount == 0) throw new InvalidMazeException(NO_GRID_SIZE);    //TODO - if grid size is required, does it make sense to require it as part of constructor? See builder pattern conventions

        //Initial grid
        final var grid = gridFactory.createDistancesGrid(this.rowCount, this.colCount);
        System.out.println("-- START MAZE BUILDER -- \n Empty grid"); //TODO - Temp print
        System.out.println(grid); //todo Temp

        //Build pathing
        this.genAlgorithm.apply(grid);

        //Goals
        final var startCell = this.getEntrance(grid);
        final var endCell = this.getExit(grid, startCell);
        final var entrances = new AbstractMap.SimpleEntry<Cell,Cell>(startCell, endCell); //TODO - rename

        //Show it
        if(showDistances){
            grid.setDistances(algorithmFactory.getDijkstra().getDistances(grid, startCell));
        }

        return new Maze(grid, entrances);
    }

    /**
     * Get entrance cell of grid depending on internal configuration of builder.
     * @param grid The grid.
     * @return The cell that the builder has determined to be the start of the maze based on the 
     * configuration of the builder.
     * @throws InvalidMazeException If the start cell that was determined does not actually exist in the grid.
     */
    private Cell getEntrance(Grid grid) throws InvalidMazeException {
        final var entranceBuilder = goalBuilderFactory.create(grid).entrance();

        MazeGoal entrance;
        if(this.useLongestPath)         entrance = entranceBuilder.farthestFrom(grid.getRandomCell()); //TODO - always use same cell instead of random
        else if(this.useRandomStart)    entrance = entranceBuilder.atRandom();
        else if(this.startAtFirst)      entrance = entranceBuilder.atStart();
        else entrance = entranceBuilder.atPosition(this.startRow, this.startColumn);

        return entrance.getCell();
    }

    /**
     * Get exit cell of grid depending on internal configuration of builder.
     * @param grid The grid.
     * @return The cell that the builder has determined to be the end/exit of the maze based on the 
     * configuration of the builder.
     * @throws InvalidMazeException If the end cell that was determined does not actually exist in the grid.
     */
    private Cell getExit(Grid grid, Cell startCell) throws InvalidMazeException {
        final var exitBuilder = goalBuilderFactory.create(grid).exit();

        MazeGoal exit;
        if(this.useLongestPath)     exit = exitBuilder.farthestFrom(startCell);
        else if(this.useRandomEnd)  exit = exitBuilder.atRandom();
        else if(this.endAtLast)     exit = exitBuilder.atEnd();
        else exit = exitBuilder.atPosition(this.endRow, this.endColumn);

        return exit.getCell();
    }

    /**
     * Get the grid factory used by this MazeBuilder.
     * @return The grid factory used.
     */
    GridFactory getGridFactory(){
        return this.gridFactory;
    }

    /**
     * Get the maze-goal builder factory used by this MazeBuilder.
     * @return The maze-goal builder factory used.
     */
    MazeGoalBuilderFactory getGoalBuilderFactory(){
        return this.goalBuilderFactory;
    }

    /**
     * Get the algorithm factory used by this MazeBuilder.
     * @return The algorithm factory used.
     */
    AlgorithmFactory getAlgorithmFactory(){
        return this.algorithmFactory;
    }
}
