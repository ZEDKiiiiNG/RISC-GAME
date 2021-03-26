package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;

import java.util.HashSet;
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
     * @param destinationId destinationId
     * @param unitType unitType
     * @param number number
     * @param player player
     */
    public MoveAction(Integer sourceTerritoryId, Integer destinationId, UnitType unitType, Integer number, Integer player) {
        super(player, ActionType.MOVE, destinationId, unitType, number, sourceTerritoryId);
    }

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
        if (number <= 0) {
            return "Invalid or unnecessary number " + number;
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
        if (!sourceTerritory.getUnitsMap().containsKey(unitType)) {
            return "The source territory does not contain the unit type " + unitType;
        }
        if (sourceTerritory.getUnitsMap().get(unitType) < number) {
            return "The source territory does not have enough unit number: "
                    + sourceTerritory.getUnitsMap().get(unitType) + " < " + number;
        }
        int moveCost;
        if ((moveCost = board.calculateMoveCost(sourceTerritoryId, destinationId, playerId)) == Integer.MAX_VALUE) {
            return "The destination territory is not reachable";
        }

        //whether current player has enough resource to move units
        String error;
        if ((error = player.hasEnoughResources(ResourceType.FOOD, moveCost)) != null){
            return error;
        }

        return null;
    }

    @Override
    public String apply(GameBoard board) throws InvalidActionException {
        String error;
        StringBuilder builder = new StringBuilder();
        if ((error = isValid(board)) != null) {
            throw new InvalidActionException(error);
        }
        Player player = board.getPlayers().get(super.playerId);

        //update resource map
        int moveCostPerUnit = board.calculateMoveCost(sourceTerritoryId, destinationId, playerId);
        int totalCost = moveCostPerUnit * number;
        player.updateResourceMap(ResourceType.FOOD, -totalCost);

        //update source territory units, if number reduced to 0, this territory should not be
        //owned by player anymore -- remove from owned territory
        Territory sourceTerritory = board.getTerritories().get(sourceTerritoryId);
        sourceTerritory.updateUnitsMap(unitType, -number);
        //update destination territory
        Territory desTerritory = board.getTerritories().get(destinationId);
        desTerritory.updateUnitsMap(unitType, number);
        builder.append("SUCCESS: ").append(printInfo(totalCost));
        return builder.toString();
    }

    @Override
    public String simulateApply(GameBoard board) throws InvalidActionException {
        return this.apply(board);
    }

    public String printInfo(int costs) {
        StringBuilder builder = new StringBuilder();
        builder.append("MOVE ACTION { ")
                .append(" conducted by player ").append(playerId)
                .append(", from ").append(sourceTerritoryId)
                .append(", to ").append(destinationId)
                .append(", unit type ").append(unitType)
                .append(", number of units ").append(number)
                .append(", costs ").append(costs).append(" ").append(ResourceType.FOOD)
                .append(" }").append(System.lineSeparator());
        return builder.toString();
    }

}
