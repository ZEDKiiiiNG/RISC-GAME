package edu.duke.risc.shared;

/**
 * Basic configurations of the game.
 *
 * @author eason
 * @date 2021/3/10 14:03
 */
public class Configurations {

    public static final int MAX_PLAYERS = 3;
    public static final int DEFAULT_FOOD_RESOURCE = 50;
    public static final int DEFAULT_TECH_RESOURCE = 50;
    public static final int DEFAULT_TECHNOLOGY_LEVEL = 1;

    public static final int DEFAULT_SERVER_PORT = 8085;

    public static final String GAME_BOARD_STRING = "GAME_BOARD_STRING";
    public static final String PLAYER_STRING = "PLAYER_STRING";
    public static final String LOGGER_STRING = "LOGGER_STRING";
    public static final String ERR_MSG = "ERR_MSG";
    public static final String SUCCESS_MSG = "SUCCESS_MSG";

    public static final int INIT_SOLDIER_NUM = 10;
    public static final int DEFAULT_PLAYER_ID = -1;
    public static final int MASTER_ID = 0;
    public static final int DEFAULT_INIT_CLOAKING = 4;
    public static final int TRAIN_SPY_COSTS = 20;


    public static final String REQUEST_PLACEMENT_ACTIONS = "REQUEST_PLACEMENT_ACTIONS";
    /**
     * Actions that does not affect others: move, upgrade units,
     * upgrade tech, research cloaking, conduct cloaking: should conduct before attack and missile attack
     * and remain in original sequence
     */
    public static final String REQUEST_NON_AFFECT_ACTIONS = "REQUEST_NON_AFFECT_ACTIONS";
    public static final String REQUEST_ATTACK_ACTIONS = "REQUEST_ATTACK_ACTIONS";
    public static final String REQUEST_MISSILE_ATTACK_ACTIONS = "REQUEST_MISSILE_ATTACK_ACTIONS";

    public static final String STAGE_ASSIGN = "STAGE_ASSIGN";
    public static final String STAGE_MOVE = "STAGE_MOVE";
    public static final String STAGE_OBSERVE = "STAGE_OBSERVE";
    public static final String STAGE_CREATE = "STAGE_CREATE";

    public static final String SUCCESS_LOG = "SUCCESS_LOG";
    public static final String OCCUPIED_LOG = "OCCUPIED_LOG";
    public static final String CANNOTFIND_LOG = "CANNOTFIND_LOG";

    public static final String GAMENOTFOUND = "GAMENOTFOUND";
    public static final String USERNOTFOUND = "USERNOTFOUND";
    public static final String SUCCESSFOUND = "SUCCESSFOUND";

    public static final String GAMEFINISHED = "GAMEFINISHED";

}
