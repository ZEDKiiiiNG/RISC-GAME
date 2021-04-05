package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Place units on the board
 *
 * @author eason
 * @date 2021/3/11 13:58
 */
public class PlacementAction extends AbstractAction {

    /**
     * Constructor
     *
     * @param territoryId territoryId
     * @param unitMap unit map
     * @param player      player
     */
    public PlacementAction(Integer territoryId, Map<UnitType, Integer> unitMap, Integer player) {
        super(player, ActionType.PLACEMENT, territoryId, unitMap);
    }

    @Override
    public String isValid(GameBoard board) {
        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            if (entry.getValue() <= 0) {
                return "Invalid number " + entry.getValue() + " for unit type " + entry.getKey();
            }
        }
        if (!board.getPlayers().containsKey(super.playerId)) {
            return "Does not contain user: " + playerId;
        }
        Player player = board.getPlayers().get(super.playerId);

        if (!player.ownsTerritory(destinationId)) {
            return "You are not assigned territory with id = " + destinationId;
        }

        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            UnitType unitType = entry.getKey();
            int number = entry.getValue();
            if (!player.getInitUnitsMap().containsKey(unitType)
                    || player.getInitUnitsMap().get(unitType) < number) {
                return "Does not contain " + unitType + " or number of unit type " + number +
                        " exceed available " + player.getInitUnitsMap().get(unitType);
            }
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

        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            UnitType unitType = entry.getKey();
            int number = entry.getValue();
            player.updateTotalUnitMap(unitType, number);
            player.updateInitUnitMap(unitType, -number);
            Territory territory = board.getTerritories().get(destinationId);
            territory.updateUnitsMap(unitType, number);
        }

        return builder.toString();
    }

    @Override
    public String simulateApply(GameBoard board) throws InvalidActionException {
        return this.apply(board);
    }

}
