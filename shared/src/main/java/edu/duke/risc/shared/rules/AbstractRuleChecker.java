package edu.duke.risc.shared.rules;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.UnitType;

import java.util.Map;

/**
 * @author eason
 * @date 2021/3/27 15:31
 */
public abstract class AbstractRuleChecker {

    private final AbstractRuleChecker nextRule;

    /**
     * Default constructor
     *
     * @param nextRule next rule in the chain
     */
    public AbstractRuleChecker(AbstractRuleChecker nextRule) {
        this.nextRule = nextRule;
    }

    /**
     * CheckRules
     *
     * @param playerId      playerId
     * @param board         board
     * @param sourceId      sourceId
     * @param destinationId destinationId
     * @param unitMap       unitMap
     * @return null if the rule is valid, error message if rule is invalid
     */
    public String checkRules(Integer playerId, GameBoard board,
                             Integer sourceId, Integer destinationId, Map<UnitType, Integer> unitMap) {
        String currentResult = checkRule(playerId, board, sourceId, destinationId, unitMap);
        if (currentResult != null) {
            return currentResult;
        }
        if (nextRule != null) {
            return nextRule.checkRules(playerId, board, sourceId, destinationId, unitMap);
        }
        return null;
    }

    /**
     * check Rule
     *
     * @param playerId      playerId
     * @param board         board
     * @param sourceId      sourceId
     * @param destinationId destinationId
     * @param unitMap       unitMap
     * @return null if the rule is valid, error message if rule is invalid
     */
    protected abstract String checkRule(Integer playerId, GameBoard board,
                                        Integer sourceId, Integer destinationId, Map<UnitType, Integer> unitMap);


}
