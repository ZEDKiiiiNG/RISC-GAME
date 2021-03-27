package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.UnitType;

import java.util.Map;

/**
 * Abstract action with source id
 *
 * @author eason
 * @date 2021/3/13 21:47
 */
public abstract class AbstractSourceAction extends AbstractAction {

    /**
     * Source territory id
     */
    protected Integer sourceTerritoryId;


    public AbstractSourceAction(Integer playerId, ActionType actionType, Integer destinationId,
                                Map<UnitType, Integer> unitMap, Integer sourceTerritoryId) {
        super(playerId, actionType, destinationId, unitMap);
        this.sourceTerritoryId = sourceTerritoryId;
    }

}
