package edu.duke.risc.shared.commons;

/**
 * @author eason
 * @date 2021/3/10 16:44
 */
public enum PayloadType {

    /**
     *
     */
    INFO,

    UPDATE,

    REQUEST,

    /**
     * The game is over, client should close resources and quit
     */
    GAME_OVER,

    /**
     * Client request to disconnect from server, server response with the same type
     */
    QUIT,

    ERROR,

    SUCCESS,

}
