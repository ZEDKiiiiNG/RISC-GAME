package edu.duke.risc.shared.board;

import java.io.Serializable;

/**
 * Interface for game board displayer.
 *
 * @author eason
 * @date 2021/3/10 21:27
 */
public interface Displayable extends Serializable {

    /**
     * Display the current board
     *
     * @param gameBoard gameBoard
     */
    public void display(GameBoard gameBoard);

    String displaySingleTerritory(GameBoard gameBoard, Territory territory);
}
