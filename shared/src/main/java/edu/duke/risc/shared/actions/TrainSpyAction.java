package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Action that transform normal units into spies in a specific territory
 *
 * @author eason
 * @date 2021/4/14 14:18
 */
public class TrainSpyAction implements Action {

    /**
     * Player id of the action conductor
     */
    private final Integer playerId;

    /**
     * The action type
     */
    private final ActionType actionType;

    /**
     * The destination territory id
     */
    private final Integer destinationId;

    /**
     * amount of units to upgrade into spy
     */
    private final Integer amount;

    /**
     * Constructor
     *
     * @param playerId      id of the player
     * @param actionType    action type
     * @param destinationId destinationId
     * @param amount        amount
     */
    public TrainSpyAction(Integer playerId, ActionType actionType, Integer destinationId, Integer amount) {
        this.playerId = playerId;
        this.actionType = actionType;
        this.destinationId = destinationId;
        this.amount = amount;
    }

    @Override
    public String isValid(GameBoard board) {
        if (!board.getPlayers().containsKey(playerId)) {
            return "Does not contain user: " + playerId;
        }
        Player player = board.getPlayers().get(playerId);
        Territory territory = board.findTerritory(destinationId);

        //player should have enough resources
        String error;
        if ((error = player.hasEnoughResources(ResourceType.TECH,
                amount * Configurations.TRAIN_SPY_COSTS)) != null) {
            return error;
        }

        //territory should have enough number of units to upgrade
        int totalUnits = territory.getTotalNumberOfUnits();
        if (totalUnits < amount) {
            return "Does not have enough units (" + totalUnits + "<" + amount + ") in territory " + territory;
        }

        return null;
    }

    @Override
    public String apply(GameBoard board) throws InvalidActionException {
        String error;
        if ((error = isValid(board)) != null) {
            throw new InvalidActionException(error);
        }
        Player player = board.getPlayers().get(playerId);
        Territory territory = board.findTerritory(destinationId);
        StringBuilder builder = new StringBuilder();

        //remove previous units
        int remainder = amount;
        UnitType currentType = UnitType.SOLDIER;
        Map<UnitType, Integer> costs = new HashMap<>(10);
        while (remainder > 0) {
            int unitCount = territory.getUnitsNumber(currentType);
            if (unitCount > 0) {
                //reduce amount number of units
                if (unitCount >= amount) {
                    //has enough current unit type
                    territory.updateUnitsMap(currentType, -amount);
                    player.updateTotalUnitMap(currentType, -amount);
                    costs.put(currentType, amount);
                    break;
                } else {
                    //does not have enough current unit type
                    territory.updateUnitsMap(currentType, -unitCount);
                    player.updateTotalUnitMap(currentType, -unitCount);
                    costs.put(currentType, unitCount);
                    remainder -= unitCount;
                    currentType = UnitType.getNextLevelOfUnit(currentType);
                }
            }
            //fixme maybe consider exceeding situation here, may cause infinite loop
        }

        //logging
        builder.append("SUCCESS: player ").append(playerId)
                .append(" trains ").append(amount).append(" of spies in ").append(territory).append(" costs ");

        //print cost units information
        for (Map.Entry<UnitType, Integer> entry : costs.entrySet()) {
            builder.append(entry.getValue()).append(" ").append(entry.getKey()).append(",");
        }

        builder.append(" with costs TECH as").append(amount * Configurations.TRAIN_SPY_COSTS)
                .append(System.lineSeparator());

        //add spy units
        territory.updateSpiesMap(playerId, amount);
        player.updateSpiesMap(destinationId, amount);

        return builder.toString();
    }

    @Override
    public String simulateApply(GameBoard board) throws InvalidActionException {
        return this.apply(board);
    }

}
