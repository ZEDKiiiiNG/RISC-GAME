package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.UnitType;

import java.util.Map;

/**
 * Contains common fields of the action
 *
 * @author eason
 * @date 2021/3/11 14:07
 */
public abstract class AbstractAction implements Action {

    /**
     * Player id of the action conductor
     */
    protected Integer playerId;

    /**
     * The action type
     */
    protected ActionType actionType;

    /**
     * The destination territory id
     */
    protected Integer destinationId;

    /**
     * unit maps
     */
    protected Map<UnitType, Integer> unitMap;

    public AbstractAction(Integer playerId, ActionType actionType, Integer destinationId, Map<UnitType, Integer> unitMap) {
        this.playerId = playerId;
        this.actionType = actionType;
        this.destinationId = destinationId;
        this.unitMap = unitMap;
    }

}
