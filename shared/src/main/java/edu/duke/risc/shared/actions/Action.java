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
     * @return action log
     * @param board the main game board
     * @throws InvalidActionException the action is defined as invalid
     */
    public String apply(GameBoard board) throws InvalidActionException;

    /**
     * Conduct the current action on board -- from the source
     *
     * @throws InvalidActionException the action is defined as invalid
     */
    public void applyBefore(GameBoard board) throws InvalidActionException;

    /**
     * Conduct the current action on board -- to the destination
     *
     * @throws InvalidActionException the action is defined as invalid
     */
    public void applyAfter(GameBoard board) throws InvalidActionException;

}
