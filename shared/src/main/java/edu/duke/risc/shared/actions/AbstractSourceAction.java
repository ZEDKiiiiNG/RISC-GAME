package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.UnitType;

/**
 * @author eason
 * @date 2021/3/13 21:47
 */
public abstract class AbstractSourceAction extends AbstractAction {

    /**
     * Source territory id
     */
    protected Integer sourceTerritoryId;


    public AbstractSourceAction(Integer playerId, ActionType actionType, Integer destinationId,
                                UnitType unitType, Integer number, Integer sourceTerritoryId) {
        super(playerId, actionType, destinationId, unitType, number);
        this.sourceTerritoryId = sourceTerritoryId;
    }

}
