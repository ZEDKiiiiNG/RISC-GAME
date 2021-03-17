package edu.duke.risc.shared.commons;

import java.io.Serializable;

/**
 * @author eason
 * @date 2021/3/11 14:18
 */
public enum ActionType implements Serializable {

    /**
     * Attack
     */
    ATTACK,

    /**
     * Client wants to move their units
     */
    MOVE,

    /**
     * Client wants to make unit placements
     */
    PLACEMENT,

}
