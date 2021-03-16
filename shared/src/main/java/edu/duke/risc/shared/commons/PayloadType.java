package edu.duke.risc.shared.commons;

/**
 * @author eason
 * @date 2021/3/10 16:44
 */
public enum PayloadType {

    /**
     * For sending information from the server, one-way
     */
    INFO,

    /**
     * Let client knows that the board should be updated
     */
    UPDATE,

    /**
     * Client request something, e.g. sending actions
     */
    REQUEST,

    /**
     * The game is over, client should close resources and quit
     */
    GAME_OVER,

    /**
     * Client request to disconnect from server, server response with the same type
     */
    QUIT,

    /**
     * Action requests are rejected by server
     */
    ERROR,

    /**
     * When indicating an action is successfully delivered to the server.
     */
    SUCCESS,

}
