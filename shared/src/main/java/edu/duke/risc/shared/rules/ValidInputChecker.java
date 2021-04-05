package edu.duke.risc.shared.rules;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.UnitType;

import java.util.Map;

/**
 * @author eason
 * @date 2021/3/27 15:35
 */
public class ValidInputChecker extends AbstractRuleChecker {

    /**
     * Default constructor
     *
     * @param nextRule next rule in the chain
     */
    public ValidInputChecker(AbstractRuleChecker nextRule) {
        super(nextRule);
    }

    @Override
    protected String checkRule(Integer playerId, GameBoard board, Integer sourceId,
                               Integer destinationId, Map<UnitType, Integer> unitMap) {
        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            if (entry.getValue() <= 0) {
                return "Invalid number " + entry.getValue() + " for unit type " + entry.getKey();
            }
        }
        return null;
    }

}
