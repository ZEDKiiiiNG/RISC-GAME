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
     * @return whether the action is valid.
     */
    public String isValid(GameBoard board);

    /**
     * Conduct the current action on board
     *
     * @param board the main game board
     */
    public void apply(GameBoard board) throws InvalidActionException;

}
