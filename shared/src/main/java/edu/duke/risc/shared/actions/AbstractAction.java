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

    protected Integer playerId;

    protected ActionType actionType;
    protected Integer destinationId;

    protected UnitType unitType;
    protected Integer number;

    public AbstractAction() {
    }

    public AbstractAction(Integer playerId, ActionType actionType, Integer destinationId, UnitType unitType, Integer number) {
        this.playerId = playerId;
        this.actionType = actionType;
        this.destinationId = destinationId;
        this.unitType = unitType;
        this.number = number;
    }

    public Integer getPlayer() {
        return playerId;
    }
}
