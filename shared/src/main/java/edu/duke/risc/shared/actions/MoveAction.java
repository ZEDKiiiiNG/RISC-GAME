package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Move action of the player
 *
 * @author eason
 */
public class MoveAction extends AbstractSourceAction {

    /**
     * Constructor
     *
     * @param sourceTerritoryId sourceTerritoryId
     * @param destinationId     destinationId
     * @param unitMap           map of units
     * @param player            player
     */
    public MoveAction(Integer sourceTerritoryId, Integer destinationId,
                      Map<UnitType, Integer> unitMap, Integer player) {
        super(player, ActionType.MOVE, destinationId, unitMap, sourceTerritoryId);
    }

    /**
     * getPlayerTerritory
     *
     * @param board board
     * @return set of territories
     */
    public Set<Territory> getPlayerTerritory(GameBoard board) {
        Set<Territory> ans = new HashSet<>();
        Player player = board.getPlayers().get(super.playerId);
        Set<Integer> terrIds = player.getOwnedTerritories();
        for (Integer t : terrIds) {
            ans.add(board.getTerritories().get(t));
        }
        return ans;
    }

    @Override
    public String isValid(GameBoard board) {
        //check whether input has valid number
        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            if (entry.getValue() <= 0) {
                return "Invalid number " + entry.getValue() + " for unit type " + entry.getKey();
            }
        }
        if (!board.getPlayers().containsKey(super.playerId)) {
            return "Does not contain user: " + playerId;
        }
        Player player = board.getPlayers().get(super.playerId);
        Territory sourceTerritory = board.getTerritories().get(sourceTerritoryId);
        if (!player.ownsTerritory(sourceTerritoryId)) {
            return "The player does not own the source territory " + board.findTerritory(sourceTerritoryId);
        }
        if (!player.ownsTerritory(destinationId)) {
            return "The player does not own the destination territory " + board.findTerritory(destinationId);
        }

        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            UnitType unitType = entry.getKey();
            int number = entry.getValue();
            if (!sourceTerritory.containsUnitType(unitType)) {
                return "The source territory does not contain the unit type " + unitType;
            }
            if (sourceTerritory.numberOfUnits(unitType) < number) {
                return "The source territory does not have enough unit number: "
                        + sourceTerritory.getUnitsMap().get(unitType) + " < " + number;
            }
        }

        int moveCostPerUnit;
        if ((moveCostPerUnit = board.calculateMoveCost(sourceTerritoryId, destinationId, playerId)) == Integer.MAX_VALUE) {
            return "The destination territory is not reachable";
        }

        int totalCost = getResourceRequired(moveCostPerUnit);

        //whether current player has enough resource to move units
        String error;
        if ((error = player.hasEnoughResources(ResourceType.FOOD, totalCost)) != null) {
            return error;
        }

        return null;
    }

    /**
     * Get resources required for moving
     *
     * @param moveCostPerUnit moveCostPerUnit
     * @return resources required for moving
     */
    private int getResourceRequired(int moveCostPerUnit) {
        int totalCost = 0;
        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            int number = entry.getValue();
            totalCost += moveCostPerUnit * number;
        }
        return totalCost;
    }

    @Override
    public String apply(GameBoard board) throws InvalidActionException {
        String error;
        StringBuilder builder = new StringBuilder();
        if ((error = isValid(board)) != null) {
            throw new InvalidActionException(error);
        }
        Player player = board.getPlayers().get(super.playerId);

        int totalCost = 0;
        int moveCostPerUnit = board.calculateMoveCost(sourceTerritoryId, destinationId, playerId);

        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            UnitType unitType = entry.getKey();
            int number = entry.getValue();
            //update resource map
            totalCost += moveCostPerUnit * number;

            //update source territory units, if number reduced to 0, this territory should not be
            //owned by player anymore -- remove from owned territory
            Territory sourceTerritory = board.getTerritories().get(sourceTerritoryId);
            sourceTerritory.updateUnitsMap(unitType, -number);
            //update destination territory
            Territory desTerritory = board.getTerritories().get(destinationId);
            desTerritory.updateUnitsMap(unitType, number);
        }
        player.updateResourceMap(ResourceType.FOOD, -totalCost);

        builder.append("SUCCESS: ").append(printInfo(totalCost));
        return builder.toString();
    }

    @Override
    public String simulateApply(GameBoard board) throws InvalidActionException {
        return this.apply(board);
    }

    /**
     * Print action information
     * @param costs costs
     * @return action information in string
     */
    public String printInfo(int costs) {
        StringBuilder builder = new StringBuilder();
        builder.append("MOVE ACTION { ")
                .append(" conducted by player ").append(playerId)
                .append(", from ").append(sourceTerritoryId)
                .append(", to ").append(destinationId);
        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            UnitType unitType = entry.getKey();
            int number = entry.getValue();
            builder.append(", unit type ").append(unitType)
                    .append(", number of units ").append(number);
        }
        builder.append(", costs ").append(costs).append(" ").append(ResourceType.FOOD)
                .append(" }").append(System.lineSeparator());
        return builder.toString();
    }

}
