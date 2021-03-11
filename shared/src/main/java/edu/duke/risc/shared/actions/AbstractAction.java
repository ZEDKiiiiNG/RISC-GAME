package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.users.Player;

/**
 * @author eason
 * @date 2021/3/11 14:07
 */
public abstract class AbstractAction implements Action {

    protected Player player;

    protected ActionType actionType;

    public AbstractAction() {
    }

    public AbstractAction(Player player, ActionType actionType) {
        this.player = player;
        this.actionType = actionType;
    }

}
