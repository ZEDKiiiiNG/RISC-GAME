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

    /**
     * Upgrade units
     */
    UPGRADE_UNIT,

    /**
     * Upgrade tech
     */
    UPGRADE_TECH,

    /**
     * Attack territory with missile
     */
    MISSILE_ATTACK,

    /**
     * conduct research cloak action
     */
    CLOAK_RESEARCH,

    /**
     * conduct cloak action
     */
    CLOAK_CONDUCT,

    /**
     * train the spies
     */
    TRAIN_SPY,

    /**
     * move the spies
     */
    MOVE_SPY,


}
