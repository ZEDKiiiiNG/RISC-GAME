package edu.duke.risc.shared.board;

/**
 * @author eason
 * @date 2021/3/10 0:02
 */
public enum GameStage {

    /**
     * The server is waiting for users
     */
    WAITING_USERS,

    /**
     * The users are placing their units into assigned territories
     */
    PLACEMENT,

    /**
     * The game starts, users should input attack or move
     */
    GAME_START,

    /**
     * The game is over, server should broadcast to all client and then quit
     */
    GAME_OVER,

}
