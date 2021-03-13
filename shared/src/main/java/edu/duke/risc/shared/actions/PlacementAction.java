package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.exceptions.InvalidInputException;
import edu.duke.risc.shared.users.Player;

import java.util.Map;
import java.util.Set;

/**
 * @author eason
 * @date 2021/3/11 13:58
 */
public class PlacementAction extends AbstractAction {

    public PlacementAction(Integer territoryId, UnitType unitType, Integer number, Integer player) {
        super(player, ActionType.PLACEMENT, territoryId, unitType, number);
    }

    @Override
    public String isValid(GameBoard board) {
        if (!board.getPlayers().containsKey(super.playerId)) {
            return "Does not contain user: " + playerId;
        }
        Player player = board.getPlayers().get(super.playerId);

        if (!player.ownsTerritory(destinationId)){
            return "You are not assigned territory with id = " + destinationId;
        }

        if (!player.getInitUnitsMap().containsKey(unitType)
                || player.getInitUnitsMap().get(unitType) < number) {
            return "Does not contain unit type or number of unit type exceed available.";
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
        player.updateTotalUnitMap(unitType, number);
        player.updateInitUnitMap(unitType, -number);
        Territory territory = board.getTerritories().get(destinationId);
        territory.updateUnitsMap(unitType, number);
        player.addOwnedTerritory(destinationId);

        return builder.toString();
    }

    @Override
    public String simulateApply(GameBoard board) throws InvalidActionException {
        return this.apply(board);
    }

}
