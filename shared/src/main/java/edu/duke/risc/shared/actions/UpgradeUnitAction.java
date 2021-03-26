package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;

/**
 * @author eason
 * @date 2021/3/25 16:56
 */
public class UpgradeUnitAction extends AbstractAction {

    /**
     * Constructor
     *
     * @param playerId      player id
     * @param targetTerritoryId territory where units exists
     * @param unitType      original unit type
     * @param number        number of units wants to upgrade
     */
    public UpgradeUnitAction(Integer playerId, Integer targetTerritoryId, UnitType unitType, Integer number) {
        super(playerId, ActionType.UPGRADE_UNIT, targetTerritoryId, unitType, number);
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
        Territory targetTerritory = board.getTerritories().get(destinationId);

        if (!player.ownsTerritory(destinationId)) {
            return "The player does not own the target territory " + board.findTerritory(destinationId);
        }
        if (!targetTerritory.getUnitsMap().containsKey(unitType)) {
            return "The target territory does not contain the unit type " + unitType;
        }
        if (targetTerritory.getUnitsMap().get(unitType) < number) {
            return "The target territory does not have enough unit number: "
                    + targetTerritory.getUnitsMap().get(unitType) + " < " + number;
        }
        //not enough resources
        String error;
        int resourceRequired = unitType.getUpgradeResources(number);
        if ((error = player.hasEnoughResources(ResourceType.TECH, resourceRequired)) != null){
            return error;
        }

        //already at top level, cannot upgrade
        if (unitType == UnitType.getTopLevel()){
            return "Already at " + unitType + " cannot upgrade more." ;
        }

        //not enough technology level
        if ((error = player.hasEnoughTechLevel(UnitType.getTechRequiredToUpgrade(unitType))) != null){
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

        builder.append("SUCCESS: ").append(this.getActionInfo(nextUnit));
        return builder.toString();
    }

    @Override
    public String simulateApply(GameBoard board) throws InvalidActionException {
        return this.apply(board);
    }

    /**
     * getActionInfo
     * @param nextUnit getActionInfo
     * @return getActionInfo
     */
    public String getActionInfo(UnitType nextUnit) {
        StringBuilder builder = new StringBuilder();
        builder.append("UPGRADE UNITS ACTION { ")
                .append(" conducted by player ").append(playerId)
                .append(", at territory ").append(destinationId)
                .append(", from type ").append(unitType)
                .append(", to type ").append(nextUnit)
                .append(", with number ").append(number)
                .append(" }").append(System.lineSeparator());
        return builder.toString();
    }


}
