package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;

import java.util.Map;

/**
 * @author eason
 * @date 2021/3/25 16:56
 */
public class UpgradeUnitAction extends AbstractAction {

    /**
     * Constructor
     *
     * @param playerId          player id
     * @param targetTerritoryId territory where units exists
     * @param unitMap           map of units
     */
    public UpgradeUnitAction(Integer playerId, Integer targetTerritoryId, Map<UnitType, Integer> unitMap) {
        super(playerId, ActionType.UPGRADE_UNIT, targetTerritoryId, unitMap);
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
        Territory targetTerritory = board.getTerritories().get(destinationId);

        if (!player.ownsTerritory(destinationId)) {
            return "The player does not own the target territory " + board.findTerritory(destinationId);
        }
        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            UnitType unitType = entry.getKey();
            int number = entry.getValue();
            if (!targetTerritory.containsUnitType(unitType)) {
                return "The target territory does not contain the unit type " + unitType;
            }
            if (targetTerritory.numberOfUnits(unitType) < number) {
                return "The target territory does not have enough unit number: "
                        + targetTerritory.numberOfUnits(unitType) + " < " + number;
            }
        }

        String error;
        //already at top level, cannot upgrade; do not have required tech level to upgrade
        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            UnitType unitType = entry.getKey();
            if (unitType == UnitType.getTopLevel()) {
                return "Already at " + unitType + " cannot upgrade more.";
            }
            //not enough technology level
            if ((error = player.hasEnoughTechLevel(UnitType.getTechRequiredToUpgrade(unitType))) != null) {
                return error;
            }
        }

        //not enough resources
        int resourceRequired = getResourceRequired();
        if ((error = player.hasEnoughResources(ResourceType.TECH, resourceRequired)) != null) {
            return error;
        }

        return null;
    }

    /**
     * Get resources required for upgrading
     *
     * @return resources required for upgrading
     */
    private int getResourceRequired() {
        int resourceRequired = 0;
        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            UnitType unitType = entry.getKey();
            int number = entry.getValue();
            resourceRequired += unitType.getUpgradeResources(number);
        }
        return resourceRequired;
    }

    @Override
    public String apply(GameBoard board) throws InvalidActionException {
        String error;
        StringBuilder builder = new StringBuilder();
        if ((error = isValid(board)) != null) {
            throw new InvalidActionException(error);
        }
        Player player = board.getPlayers().get(super.playerId);

        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            UnitType unitType = entry.getKey();
            int number = entry.getValue();
            //update resources
            int resourceRequired = unitType.getUpgradeResources(number);
            player.useResources(ResourceType.TECH, resourceRequired);

            UnitType nextUnit = UnitType.getNextLevelOfUnit(unitType);
            //update territory units
            Territory desTerritory = board.getTerritories().get(destinationId);
            desTerritory.updateUnitsMap(unitType, -number);
            desTerritory.updateUnitsMap(nextUnit, number);

            //update total units map
            player.updateTotalUnitMap(unitType, -number);
            player.updateTotalUnitMap(nextUnit, number);

            builder.append("SUCCESS: ").append(this.getActionInfo(resourceRequired));
        }

        return builder.toString();
    }

    @Override
    public String simulateApply(GameBoard board) throws InvalidActionException {
        return this.apply(board);
    }

    /**
     * getActionInfo
     *
     * @return getActionInfo
     */
    public String getActionInfo(int costs) {
        StringBuilder result = new StringBuilder("UPGRADE UNITS ACTION { " +
                " conducted by player " + playerId +
                ", at territory " + destinationId);
        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            UnitType unitType = entry.getKey();
            int number = entry.getValue();
            UnitType nextUnit = UnitType.getNextLevelOfUnit(unitType);
            result.append(", from type ").append(unitType).append(", to type ")
                    .append(nextUnit)
                    .append(", with number ").append(number);
        }
        result.append(", with costs ").append(costs).append(" }").append(System.lineSeparator());
        return result.toString();
    }


}
