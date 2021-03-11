package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.commons.ActionType;

/**
 * @author eason
 * @date 2021/3/11 14:07
 */
public abstract class AbstractAction implements Action {

    protected Integer playerId;

    protected ActionType actionType;

    public AbstractAction() {
    }

    public AbstractAction(Integer playerId, ActionType actionType) {
        this.playerId = playerId;
        this.actionType = actionType;
    }

    public Integer getPlayer() {
        return playerId;
    }
}
