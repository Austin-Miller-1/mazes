package com.amw.sms.mazes.goals;

import com.amw.sms.grid.Cell;

/**
 * Indicates that a specific cell within the grid is some kind of "goal",
 * such as an entrance or an exit. Is effectively a wrapper to that cell.
 *  TODO - "goal" is fine for exits, but not for entrances.
 *  TODO - Can this be an extension of Cell?
 */
public class MazeGoal {
    private final Cell cell;
    private final MazeGoalType type;

    /**
     * Construct new maze goal.
     * @param cell Cell that is the goal.
     * @param type Type of the maze goal.
     */
    public MazeGoal(Cell cell, MazeGoalType type){
        this.cell = cell;
        this.type = type;
    }

    /**
     * Get the cell that is the goal.
     * @return The cell.
     */
    public Cell getCell() {
        return cell;
    }

    /**
     * Get the type of the goal.
     * @return The type of this goal.
     */
    public MazeGoalType getType() {
        return type;
    }
}
