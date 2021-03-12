package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;

/**
 * @author eason
 * @date 2021/3/12 10:03
 */
public class AttackAction extends AbstractAction {

    private Integer sourceId;

    public AttackAction(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public AttackAction(Integer playerId, ActionType actionType, Integer destinationId,
                        UnitType unitType, Integer number, Integer sourceId) {
        super(playerId, actionType, destinationId, unitType, number);
        this.sourceId = sourceId;
    }

    @Override
    public String isValid(GameBoard board) {
        return null;
    }

    @Override
    public void apply(GameBoard board) throws InvalidActionException {

    }

}
