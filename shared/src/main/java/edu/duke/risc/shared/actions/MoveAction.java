package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 *
 */
public class MoveAction extends AbstractAction {

    /**
     * Source territory id
     */
    private Integer sourceTerritoryId;

    private UnitType unitType;

    private Integer number;

    public MoveAction(Integer sourceTerritoryId, Integer destinationId, UnitType unitType, Integer number, Integer player) {
        super(player, ActionType.MOVE, destinationId, unitType, number);
        this.sourceTerritoryId = sourceTerritoryId;
        this.unitType = unitType;
        this.number = number;
    }

    public Set<Territory> getPlayerTerritory(GameBoard board) {
        Set<Territory> ans = new HashSet<>();
        Player player = board.getPlayers().get(super.playerId);
        Set<Integer> terr_ids = player.getOwnedTerritories();
        for (Integer t : terr_ids) {
            ans.add(board.getTerritories().get(t));
        }
        return ans;
    }

    @Override
    public String isValid(GameBoard board) {
        if (!board.getPlayers().containsKey(super.playerId)) {
            return "Does not contain user: " + playerId;
        }
        Player player = board.getPlayers().get(super.playerId);
        Territory sourceTerritory = board.getTerritories().get(sourceTerritoryId);
        Territory destTerritory = board.getTerritories().get(destinationId);

        if (!player.getOwnedTerritories().contains(sourceTerritoryId)) {
            return "The player does not own the source territory " + board.findTerritory(sourceTerritoryId);
        }
        if (!player.getOwnedTerritories().contains(destinationId) && !destTerritory.isEmptyTerritory()) {
            return "The player does not own the non-empty destination territory "
                    + board.findTerritory(destinationId);
        }
        if (!sourceTerritory.getUnitsMap().containsKey(unitType)) {
            return "The source territory does not contain the unit type " + unitType;
        }
        if (sourceTerritory.getUnitsMap().get(unitType) < number) {
            return "The source territory does not have enough unit number: "
                    + sourceTerritory.getUnitsMap().get(unitType) + " < " + number;
        }
        if (!board.isReachable(sourceTerritoryId, destinationId, playerId)) {
            return "The destination territory is not reachable";
        }
        return null;
    }

    @Override
    public void apply(GameBoard board) throws InvalidActionException {
        String error;
        if ((error = isValid(board)) != null) {
            throw new InvalidActionException(error);
        }
        Player player = board.getPlayers().get(super.playerId);
        //update source territory units, if number reduced to 0, this territory should not be
        //owned by player anymore -- remove from owned territory
        Territory sourceTerritory = board.getTerritories().get(sourceTerritoryId);
        sourceTerritory.updateUnitsMap(unitType, -number);
        if (sourceTerritory.isEmptyTerritory()) {
            player.removeOwnedTerritory(sourceTerritoryId);
        }
        //update destination territory
        Territory desTerritory = board.getTerritories().get(destinationId);
        if (desTerritory.isEmptyTerritory()) {
            player.addOwnedTerritory(destinationId);
        }
        desTerritory.updateUnitsMap(unitType, number);
    }

    @Override
    public void applyBefore(GameBoard board) throws InvalidActionException {

    }

    @Override
    public void applyAfter(GameBoard board) throws InvalidActionException {

    }

}
