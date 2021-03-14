package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.exceptions.InvalidActionException;

import java.io.Serializable;

/**
 * @author eason
 * @date 2021/3/11 13:57
 */
public interface Action extends Serializable {

    /**
     * check whether the action is valid.
     *
     * @param board player who conduct this action
     * @return null if the action is valid, error message if invalid
     */
    public String isValid(GameBoard board);

    /**
     * Conduct the current action on board
     *
     * @param board the main game board
     * @return action log
     * @throws InvalidActionException the action is defined as invalid
     */
    public String apply(GameBoard board) throws InvalidActionException;

    /**
     * Conduct the current action on board
     *
     * @param board the main game board
     * @return action log
     * @throws InvalidActionException the action is defined as invalid
     */
    public String simulateApply(GameBoard board) throws InvalidActionException;

    /**
     * Conduct the current action on board -- from the source
     *
     * @param board board
     * @return result
     * @throws InvalidActionException the action is defined as invalid
     */
    public default String applyBefore(GameBoard board) throws InvalidActionException {
        return null;
    }

    /**
     * Conduct the current action on board -- to the destination
     *
     * @param board board
     * @return result
     * @throws InvalidActionException the action is defined as invalid
     */
    public default String applyAfter(GameBoard board) throws InvalidActionException {
        return null;
    }

}
